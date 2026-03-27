package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import application.model.enums.EstadoSerie;
import application.model.enums.TipoSerie;
import application.model.facturacion.Cargo;
import application.model.facturacion.Factura;
import application.model.persona.Persona;
import application.model.seguimientoserie.SeguimientoSerie;
import application.model.seguimientoserie.Visualizacion;
import application.model.serie.Capitulo;
import application.model.serie.Serie;
import application.model.serie.Temporada;
import application.model.usuario.Plan;
import application.model.usuario.PlanFijo;
import application.model.usuario.PlanPorDemanda;
import application.model.usuario.Usuario;
import application.repository.PersonaRepository;
import application.repository.PlanRepository;
import application.repository.SerieRepository;
import application.repository.UsuarioRepository;

@Component
public class AppFeeder implements CommandLineRunner {

    private final PlanRepository planRepository;
    private final PersonaRepository personaRepository;
    private final SerieRepository serieRepository;
    private final UsuarioRepository usuarioRepository;

    public AppFeeder(
            PlanRepository planRepository,
            PersonaRepository personaRepository,
            SerieRepository serieRepository,
            UsuarioRepository usuarioRepository) {
        this.planRepository = planRepository;
        this.personaRepository = personaRepository;
        this.serieRepository = serieRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0 || serieRepository.count() > 0) {
            return;
        }

        List<Plan> planes = planRepository.saveAll(crearPlanes());

        List<Persona> personas = personaRepository.saveAll(crearPersonas());

        List<Serie> series = serieRepository.saveAll(crearSeries(personas));

        Usuario ana = Usuario.builder()
                .nombre("Ana Acebo")
                .password("ana123")
                .cuentaBancaria("ES7620770024003102575766")
                .plan(planes.get(0))
                .facturas(new ArrayList<>())
                .series(new ArrayList<>())
                .visualizaciones(new ArrayList<>())
                .build();

        Usuario mario = Usuario.builder()
                .nombre("Mario Polo")
                .password("mario123")
                .cuentaBancaria("ES9121000418450200051332")
                .plan(planes.get(1))
                .facturas(new ArrayList<>())
                .series(new ArrayList<>())
                .visualizaciones(new ArrayList<>())
                .build();

        List<Visualizacion> visualizaciones = crearVisualizaciones(ana, mario, series);
        List<SeguimientoSerie> seguimientos = crearSeguimientos(ana, mario, series, visualizaciones);
        List<Factura> facturasAna = crearFacturasAna(series);
        List<Factura> facturasMario = crearFacturasMario(series);

        ana.setVisualizaciones(new ArrayList<>(List.of(
                visualizaciones.get(0),
                visualizaciones.get(1),
                visualizaciones.get(2))));
        mario.setVisualizaciones(new ArrayList<>(List.of(
                visualizaciones.get(3),
                visualizaciones.get(4))));
        ana.setSeries(new ArrayList<>(List.of(seguimientos.get(0), seguimientos.get(1))));
        mario.setSeries(new ArrayList<>(List.of(seguimientos.get(2), seguimientos.get(3))));
        ana.setFacturas(new ArrayList<>(facturasAna));
        mario.setFacturas(new ArrayList<>(facturasMario));

        usuarioRepository.saveAll(List.of(ana, mario));
    }

    private List<Plan> crearPlanes() {
        PlanFijo planFijo = new PlanFijo();
        planFijo.setIdPlan(1);
        planFijo.setPrecio(19.99);

        PlanPorDemanda planPorDemanda = new PlanPorDemanda();
        planPorDemanda.setIdPlan(2);
        planPorDemanda.setPrecio(3.99);

        return List.of(planFijo, planPorDemanda);
    }

    private List<Persona> crearPersonas() {
        Persona persona1 = Persona.builder().nombrePersona("Vince Gilligan").build();
        Persona persona2 = Persona.builder().nombrePersona("Bryan Cranston").build();
        Persona persona3 = Persona.builder().nombrePersona("Peter Morgan").build();
        Persona persona4 = Persona.builder().nombrePersona("Claire Foy").build();
        Persona persona5 = Persona.builder().nombrePersona("Marta Kauffman").build();
        Persona persona6 = Persona.builder().nombrePersona("Jennifer Aniston").build();
        return List.of(persona1, persona2, persona3, persona4, persona5, persona6);
    }

    private List<Serie> crearSeries(List<Persona> personas) {
        Serie breakingBad = Serie.builder()
                .nombreSerie("Breaking Bad")
                .sinopsis("Un profesor de quimica comienza a fabricar metanfetamina para sostener a su familia.")
                .tipoSerie(TipoSerie.GOLD)
                .creadores(new ArrayList<>(List.of(personas.get(0))))
                .actores(new ArrayList<>(List.of(personas.get(1))))
                .build();

        Temporada bbT1 = Temporada.builder()
                .serie(breakingBad)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo bbT1C1 = Capitulo.builder()
                .temporada(bbT1)
                .nombreCapitulo("Pilot")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/breakingbad/t1/e1")
                .descripcion("Walter White inicia su doble vida.")
                .build();
        Capitulo bbT1C2 = Capitulo.builder()
                .temporada(bbT1)
                .nombreCapitulo("Cat's in the Bag...")
                .numeroCapitulo(2)
                .enlace("https://polaflix.local/breakingbad/t1/e2")
                .descripcion("Walter y Jesse intentan encubrir sus primeros errores.")
                .build();
        bbT1.setCapitulos(new ArrayList<>(List.of(bbT1C1, bbT1C2)));

        Temporada bbT2 = Temporada.builder()
                .serie(breakingBad)
                .nombreTemporada("Temporada 2")
                .numeroTemporada(2)
                .build();
        Capitulo bbT2C1 = Capitulo.builder()
                .temporada(bbT2)
                .nombreCapitulo("Seven Thirty-Seven")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/breakingbad/t2/e1")
                .descripcion("El peligro alrededor del negocio aumenta.")
                .build();
        bbT2.setCapitulos(new ArrayList<>(List.of(bbT2C1)));
        breakingBad.setTemporadas(new ArrayList<>(List.of(bbT1, bbT2)));

        Serie theCrown = Serie.builder()
                .nombreSerie("The Crown")
                .sinopsis("Drama historico centrado en el reinado de Isabel II.")
                .tipoSerie(TipoSerie.SILVER)
                .creadores(new ArrayList<>(List.of(personas.get(2))))
                .actores(new ArrayList<>(List.of(personas.get(3))))
                .build();

        Temporada crownT1 = Temporada.builder()
                .serie(theCrown)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo crownT1C1 = Capitulo.builder()
                .temporada(crownT1)
                .nombreCapitulo("Wolferton Splash")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/thecrown/t1/e1")
                .descripcion("Isabel afronta sus primeras crisis como heredera.")
                .build();
        crownT1.setCapitulos(new ArrayList<>(List.of(crownT1C1)));
        theCrown.setTemporadas(new ArrayList<>(List.of(crownT1)));

        Serie friends = Serie.builder()
                .nombreSerie("Friends")
                .sinopsis("Seis amigos comparten vida, humor y problemas en Nueva York.")
                .tipoSerie(TipoSerie.ESTANDAR)
                .creadores(new ArrayList<>(List.of(personas.get(4))))
                .actores(new ArrayList<>(List.of(personas.get(5))))
                .build();

        Temporada friendsT1 = Temporada.builder()
                .serie(friends)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo friendsT1C1 = Capitulo.builder()
                .temporada(friendsT1)
                .nombreCapitulo("The One Where Monica Gets a Roommate")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/friends/t1/e1")
                .descripcion("Rachel aparece en Central Perk y cambia la rutina del grupo.")
                .build();
        Capitulo friendsT1C2 = Capitulo.builder()
                .temporada(friendsT1)
                .nombreCapitulo("The One with the Sonogram at the End")
                .numeroCapitulo(2)
                .enlace("https://polaflix.local/friends/t1/e2")
                .descripcion("Ross comparte con sus amigos una noticia complicada.")
                .build();
        friendsT1.setCapitulos(new ArrayList<>(List.of(friendsT1C1, friendsT1C2)));
        friends.setTemporadas(new ArrayList<>(List.of(friendsT1)));

        return List.of(breakingBad, theCrown, friends);
    }

    private List<Visualizacion> crearVisualizaciones(Usuario ana, Usuario mario, List<Serie> series) {
        Capitulo bbPilot = series.get(0).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo bbEpisode2 = series.get(0).getTemporadas().get(0).getCapitulos().get(1);
        Capitulo crownEpisode1 = series.get(1).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo friendsEpisode1 = series.get(2).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo friendsEpisode2 = series.get(2).getTemporadas().get(0).getCapitulos().get(1);

        Visualizacion v1 = Visualizacion.builder()
                .idVisualizacion(1)
                .fechaVisualizacion(LocalDate.of(2026, 3, 3))
                .usuario(ana)
                .capitulo(bbPilot)
                .build();
        Visualizacion v2 = Visualizacion.builder()
                .idVisualizacion(2)
                .fechaVisualizacion(LocalDate.of(2026, 3, 4))
                .usuario(ana)
                .capitulo(bbEpisode2)
                .build();
        Visualizacion v3 = Visualizacion.builder()
                .idVisualizacion(3)
                .fechaVisualizacion(LocalDate.of(2026, 3, 6))
                .usuario(ana)
                .capitulo(crownEpisode1)
                .build();
        Visualizacion v4 = Visualizacion.builder()
                .idVisualizacion(4)
                .fechaVisualizacion(LocalDate.of(2026, 3, 8))
                .usuario(mario)
                .capitulo(friendsEpisode1)
                .build();
        Visualizacion v5 = Visualizacion.builder()
                .idVisualizacion(5)
                .fechaVisualizacion(LocalDate.of(2026, 3, 9))
                .usuario(mario)
                .capitulo(friendsEpisode2)
                .build();

        return List.of(v1, v2, v3, v4, v5);
    }

    private List<SeguimientoSerie> crearSeguimientos(
            Usuario ana,
            Usuario mario,
            List<Serie> series,
            List<Visualizacion> visualizaciones) {
        SeguimientoSerie s1 = SeguimientoSerie.builder()
                .idSeguimientoSerie(1)
                .serie(series.get(0))
                .ultimoVisto(visualizaciones.get(1).getCapitulo())
                .usuario(ana)
                .estadoSerie(EstadoSerie.EMPEZADA)
                .build();
        SeguimientoSerie s2 = SeguimientoSerie.builder()
                .idSeguimientoSerie(2)
                .serie(series.get(1))
                .ultimoVisto(visualizaciones.get(2).getCapitulo())
                .usuario(ana)
                .estadoSerie(EstadoSerie.PENDIENTE)
                .build();
        SeguimientoSerie s3 = SeguimientoSerie.builder()
                .idSeguimientoSerie(3)
                .serie(series.get(2))
                .ultimoVisto(visualizaciones.get(4).getCapitulo())
                .usuario(mario)
                .estadoSerie(EstadoSerie.TERMINADA)
                .build();
        SeguimientoSerie s4 = SeguimientoSerie.builder()
                .idSeguimientoSerie(4)
                .serie(series.get(0))
                .ultimoVisto(null)
                .usuario(mario)
                .estadoSerie(EstadoSerie.PENDIENTE)
                .build();

        return List.of(s1, s2, s3, s4);
    }

    private List<Factura> crearFacturasAna(List<Serie> series) {
        Capitulo bbPilot = series.get(0).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo bbEpisode2 = series.get(0).getTemporadas().get(0).getCapitulos().get(1);

        Factura facturaMarzo = Factura.builder()
                .fecha(LocalDate.of(2026, 3, 31))
                .cargos(List.of(
                        new Cargo(LocalDate.of(2026, 3, 3), 9.99, series.get(0).getNombreSerie(),
                                bbPilot.getIdCapitulo(), bbPilot.getTemporada().getNumeroTemporada()),
                        new Cargo(LocalDate.of(2026, 3, 4), 10.00, series.get(0).getNombreSerie(),
                                bbEpisode2.getIdCapitulo(), bbEpisode2.getTemporada().getNumeroTemporada())))
                .build();

        return List.of(facturaMarzo);
    }

    private List<Factura> crearFacturasMario(List<Serie> series) {
        Capitulo friendsEpisode1 = series.get(2).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo friendsEpisode2 = series.get(2).getTemporadas().get(0).getCapitulos().get(1);

        Factura facturaMarzo = Factura.builder()
                .fecha(LocalDate.of(2026, 3, 31))
                .cargos(List.of(
                        new Cargo(LocalDate.of(2026, 3, 8), 3.99, series.get(2).getNombreSerie(),
                                friendsEpisode1.getIdCapitulo(), friendsEpisode1.getTemporada().getNumeroTemporada()),
                        new Cargo(LocalDate.of(2026, 3, 9), 3.99, series.get(2).getNombreSerie(),
                                friendsEpisode2.getIdCapitulo(), friendsEpisode2.getTemporada().getNumeroTemporada())))
                .build();

        return List.of(facturaMarzo);
    }
}
