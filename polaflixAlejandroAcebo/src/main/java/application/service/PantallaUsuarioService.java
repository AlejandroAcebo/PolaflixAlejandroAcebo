package application.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.BadRequestException;
import application.exception.ResourceNotFoundException;
import application.model.entity.persona.Persona;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Serie;
import application.model.entity.serie.Temporada;
import application.model.entity.usuario.Usuario;
import application.model.enums.EstadoSerie;
import application.repository.SeguimientoSerieRepository;
import application.repository.SerieRepository;
import application.repository.UsuarioRepository;
import application.repository.VisualizacionRepository;
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
    private final SeguimientoSerieRepository seguimientoSerieRepository;
    private final VisualizacionRepository visualizacionRepository;

    public PantallaUsuarioService(
            UsuarioRepository usuarioRepository,
            SerieRepository serieRepository,
            SeguimientoSerieRepository seguimientoSerieRepository,
            VisualizacionRepository visualizacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.serieRepository = serieRepository;
        this.seguimientoSerieRepository = seguimientoSerieRepository;
        this.visualizacionRepository = visualizacionRepository;
    }

    @Transactional(readOnly = true)
    public UsuarioHomeView getHome(int usuarioId) {
        Usuario usuario = getUsuario(usuarioId);
        List<Visualizacion> visualizaciones = visualizacionRepository.findByUsuarioIdUsuario(usuarioId);

        Map<Integer, Set<Integer>> vistosPorSerie = visualizaciones.stream()
                .collect(Collectors.groupingBy(
                        Visualizacion::idSerie,
                        Collectors.mapping(Visualizacion::idCapitulo, Collectors.toSet())));

        List<SeriePersonalView> series = seguimientoSerieRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(seguimiento -> toSeriePersonal(seguimiento, vistosPorSerie))
                .sorted(Comparator.comparing(SeriePersonalView::nombreSerie))
                .toList();

        return new UsuarioHomeView(
                toUsuarioResumen(usuario),
                filtrarPorEstado(series, EstadoSerie.EMPEZADA),
                filtrarPorEstado(series, EstadoSerie.PENDIENTE),
                filtrarPorEstado(series, EstadoSerie.TERMINADA));
    }

    @Transactional(readOnly = true)
    public SerieDetalleView getSerieDetalle(int usuarioId, int serieId) {
        getUsuario(usuarioId);
        SeguimientoSerie seguimiento = seguimientoSerieRepository.findByUsuarioIdUsuarioAndSerieIdSerie(usuarioId, serieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La serie no esta en el espacio personal del usuario"));
        Serie serie = seguimiento.getSerie();

        Set<Integer> capitulosVistos = visualizacionRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(Visualizacion::idCapitulo)
                .collect(Collectors.toSet());

        List<TemporadaDetalleView> temporadas = serie.getTemporadas().stream()
                .sorted(Comparator.comparingInt(Temporada::getNumeroTemporada))
                .map(temporada -> toTemporadaDetalle(temporada, capitulosVistos))
                .toList();

        int totalCapitulos = serie.totalCapitulos();
        int vistos = (int) temporadas.stream()
                .flatMap(temporada -> temporada.capitulos().stream())
                .filter(CapituloDetalleView::visto)
                .count();

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
        getUsuario(usuarioId);
        String inicialNormalizada = Serie.normalizarInicialCatalogo(inicial);
        Set<Integer> seriesAgregadas = seguimientoSerieRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(seguimiento -> seguimiento.getSerie().getIdSerie())
                .collect(Collectors.toSet());

        List<SerieCatalogoView> series = serieRepository.findAll().stream()
                .filter(serie -> serie.inicialCatalogo().equals(inicialNormalizada))
                .sorted(Comparator.comparing(Serie::getNombreSerie))
                .map(serie -> toSerieCatalogo(serie, seriesAgregadas.contains(serie.getIdSerie())))
                .toList();

        return new CatalogoView(inicialNormalizada, series);
    }

    @Transactional(readOnly = true)
    public SerieCatalogoView buscarSerieCatalogo(int usuarioId, String nombre) {
        getUsuario(usuarioId);
        String busqueda = nombre == null ? "" : nombre.trim().toLowerCase();
        if (busqueda.isBlank()) {
            throw new BadRequestException("Debe indicarse un nombre de serie");
        }

        Set<Integer> seriesAgregadas = seguimientoSerieRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(seguimiento -> seguimiento.getSerie().getIdSerie())
                .collect(Collectors.toSet());

        return serieRepository.findAll().stream()
                .filter(serie -> serie.coincideConBusqueda(busqueda))
                .sorted(Comparator.comparing(Serie::getNombreSerie))
                .findFirst()
                .map(serie -> toSerieCatalogo(serie, seriesAgregadas.contains(serie.getIdSerie())))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se ha encontrado ninguna serie con ese nombre"));
    }

    @Transactional(readOnly = true)
    public FacturaMensualView getFacturaMensual(int usuarioId, int anio, int mes) {
        Usuario usuario = getUsuario(usuarioId);
        boolean cuotaFija = usuario.tieneCuotaFija();

        List<Visualizacion> visualizacionesMes = visualizacionRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .filter(visualizacion -> visualizacion.perteneceAlMes(anio, mes))
                .sorted(Comparator.comparing(Visualizacion::getFechaVisualizacion).reversed())
                .toList();

        List<CargoFacturaView> cargos = visualizacionesMes.stream()
                .map(this::toCargoFactura)
                .toList();

        double total = usuario.calcularTotalFacturaMensual(visualizacionesMes);

        return new FacturaMensualView(anio, mes, cuotaFija, cargos, total);
    }

    private Usuario getUsuario(int usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + usuarioId));
    }

    private List<SeriePersonalView> filtrarPorEstado(
            List<SeriePersonalView> series,
            EstadoSerie estado) {
        return series.stream()
                .filter(serie -> estado.name().equals(serie.seguimiento().estadoSerie()))
                .toList();
    }

    private SeriePersonalView toSeriePersonal(SeguimientoSerie seguimiento, Map<Integer, Set<Integer>> vistosPorSerie) {
        Serie serie = seguimiento.getSerie();
        int totalCapitulos = serie.totalCapitulos();
        int capitulosVistos = vistosPorSerie.getOrDefault(serie.getIdSerie(), Set.of()).size();
        return new SeriePersonalView(
                serie.getIdSerie(),
                serie.getNombreSerie(),
                serie.getSinopsis(),
                tipoSerie(serie),
                nombres(serie.getCreadores()),
                nombres(serie.getActores()),
                toSeguimientoResumen(seguimiento, seguimiento.estadoEfectivo(capitulosVistos)),
                totalCapitulos,
                capitulosVistos);
    }

    private TemporadaDetalleView toTemporadaDetalle(Temporada temporada, Set<Integer> capitulosVistos) {
        List<CapituloDetalleView> capitulos = temporada.getCapitulos().stream()
                .sorted(Comparator.comparingInt(Capitulo::getNumeroCapitulo))
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
