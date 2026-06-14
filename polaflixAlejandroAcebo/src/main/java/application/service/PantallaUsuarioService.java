package application.service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.persona.Persona;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.serie.Serie;
import application.model.entity.serie.Temporada;
import application.model.entity.usuario.Usuario;
import application.model.enums.EstadoSerie;
import application.repository.SerieRepository;
import application.repository.UsuarioRepository;
import application.model.view.CapituloDetalleView;
import application.model.view.CargoFacturaView;
import application.model.view.CatalogoView;
import application.model.view.FacturaMensualView;
import application.model.view.SeguimientoResumenView;
import application.model.view.SerieCatalogoView;
import application.model.view.SerieDetalleView;
import application.model.view.SeriePersonalView;
import application.model.view.SerieView;
import application.model.view.TemporadaDetalleView;
import application.model.view.UsuarioHomeView;
import application.model.view.UsuarioResumenView;

@Service
public class PantallaUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SerieRepository serieRepository;

    public PantallaUsuarioService(
            UsuarioRepository usuarioRepository,
            SerieRepository serieRepository) {
        this.usuarioRepository = usuarioRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public UsuarioHomeView getHome(int usuarioId) {
        Usuario usuario = getUsuario(usuarioId);
        return new UsuarioHomeView(
                toUsuarioResumen(usuario),
                toSeriesPersonales(usuario, EstadoSerie.EMPEZADA),
                toSeriesPersonales(usuario, EstadoSerie.PENDIENTE),
                toSeriesPersonales(usuario, EstadoSerie.TERMINADA));
    }

    @Transactional(readOnly = true)
    public SerieDetalleView getSerieDetalle(int usuarioId, int serieId) {
        Usuario usuario = getUsuario(usuarioId);
        SeguimientoSerie seguimiento = usuario.seguimientoDeSerie(serieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La serie no esta en el espacio personal del usuario"));
        Serie serie = seguimiento.getSerie();

        Set<Integer> capitulosVistos = usuario.idsCapitulosVistos();

        List<TemporadaDetalleView> temporadas = serie.temporadasOrdenadas().stream()
                .map(temporada -> toTemporadaDetalle(temporada, capitulosVistos))
                .toList();

        int totalCapitulos = serie.totalCapitulos();
        int vistos = usuario.capitulosVistosDe(serie);

        return new SerieDetalleView(
                toSerieResponse(serie),
                toSeguimientoResumen(seguimiento),
                temporadas,
                vistos,
                totalCapitulos,
                getTemporadaInicial(seguimiento, temporadas));
    }

    @Transactional(readOnly = true)
    public CatalogoView getCatalogo(int usuarioId, String inicial) {
        Usuario usuario = getUsuario(usuarioId);
        String inicialNormalizada = Serie.normalizarInicialCatalogo(inicial);

        List<SerieCatalogoView> series = serieRepository.findAll().stream()
                .filter(serie -> serie.inicialCatalogo().equals(inicialNormalizada))
                .sorted(Comparator.comparing(Serie::getNombreSerie))
                .map(serie -> toSerieCatalogo(serie, usuario.tieneSerieEnEspacioPersonal(serie)))
                .toList();

        return new CatalogoView(inicialNormalizada, series);
    }

    @Transactional(readOnly = true)
    public SerieCatalogoView buscarSerieCatalogo(int usuarioId, String nombre) {
        Usuario usuario = getUsuario(usuarioId);
        String busqueda = nombre.trim().toLowerCase();

        return serieRepository.findAll().stream()
                .filter(serie -> serie.coincideConBusqueda(busqueda))
                .sorted(Comparator.comparing(Serie::getNombreSerie))
                .findFirst()
                .map(serie -> toSerieCatalogo(serie, usuario.tieneSerieEnEspacioPersonal(serie)))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se ha encontrado ninguna serie con ese nombre"));
    }

    @Transactional(readOnly = true)
    public FacturaMensualView getFacturaMensual(int usuarioId, int anio, int mes) {
        Usuario usuario = getUsuario(usuarioId);
        boolean cuotaFija = usuario.tieneCuotaFija();

        List<Visualizacion> visualizacionesMes = usuario.visualizacionesDelMes(anio, mes);

        List<CargoFacturaView> cargos = visualizacionesMes.stream()
                .map(this::toCargoFactura)
                .toList();

        double total = usuario.calcularTotalFacturaMensual(anio, mes);

        return new FacturaMensualView(anio, mes, cuotaFija, cargos, total);
    }

    private Usuario getUsuario(int usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                "No existe el usuario con id " + usuarioId));
    }

    private List<SeriePersonalView> toSeriesPersonales(Usuario usuario, EstadoSerie estado) {
        return usuario.seguimientosPorEstado(estado).stream()
                .map(seguimiento -> toSeriePersonal(usuario, seguimiento))
                .sorted(Comparator.comparing(SeriePersonalView::nombreSerie))
                .toList();
    }

    private SeriePersonalView toSeriePersonal(Usuario usuario, SeguimientoSerie seguimiento) {
        Serie serie = seguimiento.getSerie();
        int totalCapitulos = serie.totalCapitulos();
        int capitulosVistos = usuario.capitulosVistosDe(serie);
        return new SeriePersonalView(
                serie.getIdSerie(),
                serie.getNombreSerie(),
                serie.getSinopsis(),
                tipoSerie(serie),
                nombres(serie.getCreadores()),
                nombres(serie.getActores()),
                toSeguimientoResumen(seguimiento, usuario.estadoEfectivoDe(seguimiento)),
                totalCapitulos,
                capitulosVistos);
    }

    private TemporadaDetalleView toTemporadaDetalle(Temporada temporada, Set<Integer> capitulosVistos) {
        List<CapituloDetalleView> capitulos = temporada.capitulosOrdenados().stream()
                .map(capitulo -> new CapituloDetalleView(
                        capitulo.getIdCapitulo(),
                        temporada.getIdTemporada(),
                        capitulo.getNombreCapitulo(),
                        capitulo.getNumeroCapitulo(),
                        capitulo.getEnlace(),
                        capitulo.getDescripcion(),
                        capitulosVistos.contains(capitulo.getIdCapitulo())))
                .toList();

        return new TemporadaDetalleView(
                temporada.getIdTemporada(),
                temporada.getSerie().getIdSerie(),
                temporada.getNombreTemporada(),
                temporada.getNumeroTemporada(),
                capitulos);
    }

    private int getTemporadaInicial(
            SeguimientoSerie seguimiento,
            List<TemporadaDetalleView> temporadas) {
        Integer idTemporada = seguimiento.idTemporadaUltimoVistoSiEmpezada();
        if (idTemporada == null) {
            return 0;
        }

        for (int index = 0; index < temporadas.size(); index++) {
            if (temporadas.get(index).idTemporada() == idTemporada) {
                return index;
            }
        }
        return 0;
    }

    private SerieCatalogoView toSerieCatalogo(Serie serie, boolean agregada) {
        return new SerieCatalogoView(
                serie.getIdSerie(),
                serie.getNombreSerie(),
                serie.getSinopsis(),
                tipoSerie(serie),
                nombres(serie.getCreadores()),
                nombres(serie.getActores()),
                agregada);
    }

    private UsuarioResumenView toUsuarioResumen(Usuario usuario) {
        return new UsuarioResumenView(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getPlan().getIdPlan(),
                usuario.getCargos() == null ? List.of() : usuario.getCargos().stream().map(Enum::name).toList());
    }

    private SeguimientoResumenView toSeguimientoResumen(SeguimientoSerie seguimiento) {
        return toSeguimientoResumen(seguimiento, seguimiento.getEstadoSerie());
    }

    private SeguimientoResumenView toSeguimientoResumen(SeguimientoSerie seguimiento, EstadoSerie estadoSerie) {
        return new SeguimientoResumenView(
                seguimiento.getIdSeguimientoSerie(),
                estadoSerie == null ? "SIN_ESTADO" : estadoSerie.name(),
                seguimiento.getUltimoVisto() == null ? null : seguimiento.getUltimoVisto().getIdCapitulo());
    }

    private SerieView toSerieResponse(Serie serie) {
        return new SerieView(
                serie.getIdSerie(),
                serie.getNombreSerie(),
                serie.getSinopsis(),
                tipoSerie(serie));
    }

    private CargoFacturaView toCargoFactura(Visualizacion visualizacion) {
        return new CargoFacturaView(
                visualizacion.getFechaVisualizacion().toString(),
                visualizacion.nombreSerie(),
                visualizacion.episodio(),
                visualizacion.precio());
    }

    private List<String> nombres(List<Persona> personas) {
        return personas == null
                ? List.of()
                : personas.stream().map(Persona::getNombrePersona).toList();
    }

    private String tipoSerie(Serie serie) {
        return serie.getTipoSerie() == null ? "DESCONOCIDO" : serie.getTipoSerie().name();
    }

}
