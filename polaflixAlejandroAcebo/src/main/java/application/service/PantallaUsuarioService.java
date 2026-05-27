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
import application.model.dto.serie.SerieResponseDto;
import application.model.dto.view.CapituloDetalleResponseDto;
import application.model.dto.view.CargoFacturaResponseDto;
import application.model.dto.view.CatalogoResponseDto;
import application.model.dto.view.FacturaMensualResponseDto;
import application.model.dto.view.SeguimientoResumenResponseDto;
import application.model.dto.view.SerieCatalogoResponseDto;
import application.model.dto.view.SerieDetalleResponseDto;
import application.model.dto.view.SeriePersonalResponseDto;
import application.model.dto.view.TemporadaDetalleResponseDto;
import application.model.dto.view.UsuarioHomeResponseDto;
import application.model.dto.view.UsuarioResumenResponseDto;
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
    public UsuarioHomeResponseDto getHome(int usuarioId) {
        Usuario usuario = getUsuario(usuarioId);
        List<Visualizacion> visualizaciones = visualizacionRepository.findByUsuarioIdUsuario(usuarioId);

        Map<Integer, Set<Integer>> vistosPorSerie = visualizaciones.stream()
                .collect(Collectors.groupingBy(
                        Visualizacion::idSerie,
                        Collectors.mapping(Visualizacion::idCapitulo, Collectors.toSet())));

        List<SeriePersonalResponseDto> series = seguimientoSerieRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(seguimiento -> toSeriePersonal(seguimiento, vistosPorSerie))
                .sorted(Comparator.comparing(SeriePersonalResponseDto::getNombreSerie))
                .toList();

        return new UsuarioHomeResponseDto(
                toUsuarioResumen(usuario),
                filtrarPorEstado(series, EstadoSerie.EMPEZADA),
                filtrarPorEstado(series, EstadoSerie.PENDIENTE),
                filtrarPorEstado(series, EstadoSerie.TERMINADA));
    }

    @Transactional(readOnly = true)
    public SerieDetalleResponseDto getSerieDetalle(int usuarioId, int serieId) {
        getUsuario(usuarioId);
        SeguimientoSerie seguimiento = seguimientoSerieRepository.findByUsuarioIdUsuarioAndSerieIdSerie(usuarioId, serieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La serie no esta en el espacio personal del usuario"));
        Serie serie = seguimiento.getSerie();

        Set<Integer> capitulosVistos = visualizacionRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(Visualizacion::idCapitulo)
                .collect(Collectors.toSet());

        List<TemporadaDetalleResponseDto> temporadas = serie.getTemporadas().stream()
                .sorted(Comparator.comparingInt(Temporada::getNumeroTemporada))
                .map(temporada -> toTemporadaDetalle(temporada, capitulosVistos))
                .toList();

        int totalCapitulos = serie.totalCapitulos();
        int vistos = (int) temporadas.stream()
                .flatMap(temporada -> temporada.getCapitulos().stream())
                .filter(CapituloDetalleResponseDto::isVisto)
                .count();

        return new SerieDetalleResponseDto(
                toSerieResponse(serie),
                toSeguimientoResumen(seguimiento),
                temporadas,
                vistos,
                totalCapitulos,
                getTemporadaInicial(seguimiento, temporadas));
    }

    @Transactional(readOnly = true)
    public CatalogoResponseDto getCatalogo(int usuarioId, String inicial) {
        getUsuario(usuarioId);
        String inicialNormalizada = Serie.normalizarInicialCatalogo(inicial);
        Set<Integer> seriesAgregadas = seguimientoSerieRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(seguimiento -> seguimiento.getSerie().getIdSerie())
                .collect(Collectors.toSet());

        List<SerieCatalogoResponseDto> series = serieRepository.findAll().stream()
                .filter(serie -> serie.inicialCatalogo().equals(inicialNormalizada))
                .sorted(Comparator.comparing(Serie::getNombreSerie))
                .map(serie -> toSerieCatalogo(serie, seriesAgregadas.contains(serie.getIdSerie())))
                .toList();

        return new CatalogoResponseDto(inicialNormalizada, series);
    }

    @Transactional(readOnly = true)
    public SerieCatalogoResponseDto buscarSerieCatalogo(int usuarioId, String nombre) {
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
    public FacturaMensualResponseDto getFacturaMensual(int usuarioId, int anio, int mes) {
        Usuario usuario = getUsuario(usuarioId);
        boolean cuotaFija = usuario.tieneCuotaFija();

        List<Visualizacion> visualizacionesMes = visualizacionRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .filter(visualizacion -> visualizacion.perteneceAlMes(anio, mes))
                .sorted(Comparator.comparing(Visualizacion::getFechaVisualizacion))
                .toList();

        List<CargoFacturaResponseDto> cargos = visualizacionesMes.stream()
                .map(this::toCargoFactura)
                .toList();

        double total = usuario.calcularTotalFacturaMensual(visualizacionesMes);

        return new FacturaMensualResponseDto(anio, mes, cuotaFija, cargos, total);
    }

    private Usuario getUsuario(int usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + usuarioId));
    }

    private List<SeriePersonalResponseDto> filtrarPorEstado(
            List<SeriePersonalResponseDto> series,
            EstadoSerie estado) {
        return series.stream()
                .filter(serie -> estado.name().equals(serie.getSeguimiento().getEstadoSerie()))
                .toList();
    }

    private SeriePersonalResponseDto toSeriePersonal(SeguimientoSerie seguimiento, Map<Integer, Set<Integer>> vistosPorSerie) {
        Serie serie = seguimiento.getSerie();
        int totalCapitulos = serie.totalCapitulos();
        int capitulosVistos = vistosPorSerie.getOrDefault(serie.getIdSerie(), Set.of()).size();
        return new SeriePersonalResponseDto(
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

    private TemporadaDetalleResponseDto toTemporadaDetalle(Temporada temporada, Set<Integer> capitulosVistos) {
        List<CapituloDetalleResponseDto> capitulos = temporada.getCapitulos().stream()
                .sorted(Comparator.comparingInt(Capitulo::getNumeroCapitulo))
                .map(capitulo -> new CapituloDetalleResponseDto(
                        capitulo.getIdCapitulo(),
                        temporada.getIdTemporada(),
                        capitulo.getNombreCapitulo(),
                        capitulo.getNumeroCapitulo(),
                        capitulo.getEnlace(),
                        capitulo.getDescripcion(),
                        capitulosVistos.contains(capitulo.getIdCapitulo())))
                .toList();

        return new TemporadaDetalleResponseDto(
                temporada.getIdTemporada(),
                temporada.getSerie().getIdSerie(),
                temporada.getNombreTemporada(),
                temporada.getNumeroTemporada(),
                capitulos);
    }

    private int getTemporadaInicial(
            SeguimientoSerie seguimiento,
            List<TemporadaDetalleResponseDto> temporadas) {
        Integer idTemporada = seguimiento.idTemporadaUltimoVistoSiEmpezada();
        if (idTemporada == null) {
            return 0;
        }

        for (int index = 0; index < temporadas.size(); index++) {
            if (temporadas.get(index).getIdTemporada() == idTemporada) {
                return index;
            }
        }
        return 0;
    }

    private SerieCatalogoResponseDto toSerieCatalogo(Serie serie, boolean agregada) {
        return new SerieCatalogoResponseDto(
                serie.getIdSerie(),
                serie.getNombreSerie(),
                serie.getSinopsis(),
                tipoSerie(serie),
                nombres(serie.getCreadores()),
                nombres(serie.getActores()),
                agregada);
    }

    private UsuarioResumenResponseDto toUsuarioResumen(Usuario usuario) {
        return new UsuarioResumenResponseDto(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getPlan().getIdPlan(),
                usuario.getCargos() == null ? List.of() : usuario.getCargos().stream().map(Enum::name).toList());
    }

    private SeguimientoResumenResponseDto toSeguimientoResumen(SeguimientoSerie seguimiento) {
        return toSeguimientoResumen(seguimiento, seguimiento.getEstadoSerie());
    }

    private SeguimientoResumenResponseDto toSeguimientoResumen(SeguimientoSerie seguimiento, EstadoSerie estadoSerie) {
        return new SeguimientoResumenResponseDto(
                seguimiento.getIdSeguimientoSerie(),
                estadoSerie == null ? "SIN_ESTADO" : estadoSerie.name(),
                seguimiento.getUltimoVisto() == null ? null : seguimiento.getUltimoVisto().getIdCapitulo());
    }

    private SerieResponseDto toSerieResponse(Serie serie) {
        return new SerieResponseDto(
                serie.getIdSerie(),
                serie.getNombreSerie(),
                serie.getSinopsis(),
                tipoSerie(serie));
    }

    private CargoFacturaResponseDto toCargoFactura(Visualizacion visualizacion) {
        return new CargoFacturaResponseDto(
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
