package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import application.model.entity.facturacion.Cargo;
import application.model.entity.facturacion.Factura;
import application.model.entity.persona.Persona;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Serie;
import application.model.entity.serie.Temporada;
import application.model.entity.usuario.Plan;
import application.model.entity.usuario.PlanFijo;
import application.model.entity.usuario.PlanPorDemanda;
import application.model.entity.usuario.Usuario;
import application.model.enums.CargoRol;
import application.model.enums.EstadoSerie;
import application.model.enums.TipoSerie;
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

        // Usuario por defecto del frontend con varios cargos para probar accesos.
        Usuario alejandro = Usuario.builder()
                .nombre("Alejandro Acebo")
                .contrasena("polaflix")
                .cuentaBancaria("ES9999999999999999999999")
                .plan(planes.get(0))
                .cargos(new ArrayList<>(List.of(CargoRol.PREMIUM)))
                .facturas(new ArrayList<>())
                .series(new ArrayList<>())
                .visualizacionesPersistidas(new ArrayList<>())
                .visualizaciones(new LinkedHashMap<>())
                .build();

        // Usuario Premium
        Usuario ana = Usuario.builder()
                .nombre("Ana Acebo")
                .contrasena("polaflix")
                .cuentaBancaria("ES7620770024003102575766")
                .plan(planes.get(0))
                .cargos(new ArrayList<>(List.of(CargoRol.PREMIUM)))
                .facturas(new ArrayList<>())
                .series(new ArrayList<>())
                .visualizacionesPersistidas(new ArrayList<>())
                .visualizaciones(new LinkedHashMap<>())
                .build();

        // Usuario Estándar
        Usuario mario = Usuario.builder()
                .nombre("Mario Polo")
                .contrasena("polaflix")
                .cuentaBancaria("ES9121000418450200051332")
                .plan(planes.get(1))
                .cargos(new ArrayList<>(List.of(CargoRol.USUARIO_ESTANDAR)))
                .facturas(new ArrayList<>())
                .series(new ArrayList<>())
                .visualizacionesPersistidas(new ArrayList<>())
                .visualizaciones(new LinkedHashMap<>())
                .build();

        List<Visualizacion> visualizaciones = crearVisualizaciones(alejandro, mario, series);
        List<SeguimientoSerie> seguimientos = crearSeguimientos(alejandro, mario, series, visualizaciones);
        List<Factura> facturasAlejandro = crearFacturasAlejandro(alejandro);
        List<Factura> facturasMario = crearFacturasMario(mario, series);

        alejandro.setVisualizaciones(crearMapaVisualizacionesUsuario(alejandro, visualizaciones));
        mario.setVisualizaciones(crearMapaVisualizacionesUsuario(mario, visualizaciones));
        ana.setVisualizaciones(new LinkedHashMap<>());

        alejandro.setVisualizacionesPersistidas(visualizacionesDe(alejandro, visualizaciones));
        mario.setVisualizacionesPersistidas(visualizacionesDe(mario, visualizaciones));
        ana.setVisualizacionesPersistidas(new ArrayList<>());
        
        alejandro.setSeries(seguimientosDe(alejandro, seguimientos));
        mario.setSeries(seguimientosDe(mario, seguimientos));
        ana.setSeries(new ArrayList<>());
        
        alejandro.setFacturas(new ArrayList<>(facturasAlejandro));
        mario.setFacturas(new ArrayList<>(facturasMario));
        ana.setFacturas(new ArrayList<>());

        usuarioRepository.saveAll(List.of(alejandro, ana, mario));
    }

    private List<Plan> crearPlanes() {
        PlanFijo planFijo = new PlanFijo();
        planFijo.setIdPlan(1);
        planFijo.setPrecio(20.0);

        PlanPorDemanda planPorDemanda = new PlanPorDemanda();
        planPorDemanda.setIdPlan(2);
        planPorDemanda.setPrecio(0.0);

        return List.of(planFijo, planPorDemanda);
    }

    private List<Persona> crearPersonas() {
        Persona persona1 = Persona.builder().nombrePersona("Vince Gilligan").build();
        Persona persona2 = Persona.builder().nombrePersona("Bryan Cranston").build();
        Persona persona3 = Persona.builder().nombrePersona("Peter Morgan").build();
        Persona persona4 = Persona.builder().nombrePersona("Claire Foy").build();
        Persona persona5 = Persona.builder().nombrePersona("Marta Kauffman").build();
        Persona persona6 = Persona.builder().nombrePersona("Jennifer Aniston").build();
        Persona persona7 = Persona.builder().nombrePersona("Christopher Nolan").build();
        Persona persona8 = Persona.builder().nombrePersona("Leonardo DiCaprio").build();
        Persona persona9 = Persona.builder().nombrePersona("Shonda Rhimes").build();
        Persona persona10 = Persona.builder().nombrePersona("Viola Davis").build();
        Persona persona11 = Persona.builder().nombrePersona("Baran bo Odar").build();
        Persona persona12 = Persona.builder().nombrePersona("Louis Hofmann").build();
        Persona persona13 = Persona.builder().nombrePersona("The Duffer Brothers").build();
        Persona persona14 = Persona.builder().nombrePersona("Millie Bobby Brown").build();
        Persona persona15 = Persona.builder().nombrePersona("Jon Favreau").build();
        Persona persona16 = Persona.builder().nombrePersona("Pedro Pascal").build();
        Persona persona17 = Persona.builder().nombrePersona("Alex Pina").build();
        Persona persona18 = Persona.builder().nombrePersona("Ursula Corbero").build();
        Persona persona19 = Persona.builder().nombrePersona("Neil Druckmann").build();
        Persona persona20 = Persona.builder().nombrePersona("Bella Ramsey").build();
        return List.of(persona1, persona2, persona3, persona4, persona5, persona6, persona7, persona8, persona9, persona10,
                persona11, persona12, persona13, persona14, persona15, persona16, persona17, persona18, persona19, persona20);
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

        // Serie adicional: Inception (película tipo serie)
        Serie inception = Serie.builder()
                .nombreSerie("Inception")
                .sinopsis("Un ladrron que roba secretos corporativos de la mente de sus víctimas mientras duermen.")
                .tipoSerie(TipoSerie.GOLD)
                .creadores(new ArrayList<>(List.of(personas.get(6))))
                .actores(new ArrayList<>(List.of(personas.get(7))))
                .build();
        Temporada inceptionT1 = Temporada.builder()
                .serie(inception)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo inceptionC1 = Capitulo.builder()
                .temporada(inceptionT1)
                .nombreCapitulo("El Sueño")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/inception/t1/e1")
                .descripcion("Cobb es contratado para una misión imposible.")
                .build();
        inceptionT1.setCapitulos(new ArrayList<>(List.of(inceptionC1)));
        inception.setTemporadas(new ArrayList<>(List.of(inceptionT1)));

        // Serie adicional: Grey's Anatomy
        Serie greysAnatomy = Serie.builder()
                .nombreSerie("Grey's Anatomy")
                .sinopsis("Médicos residentes navegan por la medicina y sus relaciones personales.")
                .tipoSerie(TipoSerie.SILVER)
                .creadores(new ArrayList<>(List.of(personas.get(8))))
                .actores(new ArrayList<>(List.of(personas.get(9))))
                .build();
        Temporada greyT1 = Temporada.builder()
                .serie(greysAnatomy)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo greyC1 = Capitulo.builder()
                .temporada(greyT1)
                .nombreCapitulo("Un Día en la Vida")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/greys/t1/e1")
                .descripcion("Los nuevos residentes llegan al hospital.")
                .build();
        greyT1.setCapitulos(new ArrayList<>(List.of(greyC1)));
        greysAnatomy.setTemporadas(new ArrayList<>(List.of(greyT1)));

        Serie dark = Serie.builder()
                .nombreSerie("Dark")
                .sinopsis("La desaparicion de un nino destapa secretos familiares y viajes en el tiempo.")
                .tipoSerie(TipoSerie.GOLD)
                .creadores(new ArrayList<>(List.of(personas.get(10))))
                .actores(new ArrayList<>(List.of(personas.get(11))))
                .build();
        Temporada darkT1 = Temporada.builder()
                .serie(dark)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo darkC1 = Capitulo.builder()
                .temporada(darkT1)
                .nombreCapitulo("Secretos")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/dark/t1/e1")
                .descripcion("Winden empieza a mostrar sus grietas.")
                .build();
        Capitulo darkC2 = Capitulo.builder()
                .temporada(darkT1)
                .nombreCapitulo("Mentiras")
                .numeroCapitulo(2)
                .enlace("https://polaflix.local/dark/t1/e2")
                .descripcion("El pasado y el presente chocan en el bosque.")
                .build();
        darkT1.setCapitulos(new ArrayList<>(List.of(darkC1, darkC2)));
        dark.setTemporadas(new ArrayList<>(List.of(darkT1)));

        Serie strangerThings = Serie.builder()
                .nombreSerie("Stranger Things")
                .sinopsis("Un grupo de amigos descubre una amenaza sobrenatural bajo su pequeno pueblo.")
                .tipoSerie(TipoSerie.SILVER)
                .creadores(new ArrayList<>(List.of(personas.get(12))))
                .actores(new ArrayList<>(List.of(personas.get(13))))
                .build();
        Temporada strangerT1 = Temporada.builder()
                .serie(strangerThings)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo strangerC1 = Capitulo.builder()
                .temporada(strangerT1)
                .nombreCapitulo("La desaparicion de Will Byers")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/strangerthings/t1/e1")
                .descripcion("Una noche de juegos termina con una desaparicion imposible.")
                .build();
        strangerT1.setCapitulos(new ArrayList<>(List.of(strangerC1)));
        strangerThings.setTemporadas(new ArrayList<>(List.of(strangerT1)));

        Serie mandalorian = Serie.builder()
                .nombreSerie("The Mandalorian")
                .sinopsis("Un cazarrecompensas recorre la galaxia protegiendo a una criatura misteriosa.")
                .tipoSerie(TipoSerie.GOLD)
                .creadores(new ArrayList<>(List.of(personas.get(14))))
                .actores(new ArrayList<>(List.of(personas.get(15))))
                .build();
        Temporada mandoT1 = Temporada.builder()
                .serie(mandalorian)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo mandoC1 = Capitulo.builder()
                .temporada(mandoT1)
                .nombreCapitulo("El Mandaloriano")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/mandalorian/t1/e1")
                .descripcion("Una recompensa cambia el rumbo de su vida.")
                .build();
        mandoT1.setCapitulos(new ArrayList<>(List.of(mandoC1)));
        mandalorian.setTemporadas(new ArrayList<>(List.of(mandoT1)));

        Serie laCasaDePapel = Serie.builder()
                .nombreSerie("La Casa de Papel")
                .sinopsis("Un grupo de atracadores ejecuta un plan milimetrico en la Fabrica de Moneda.")
                .tipoSerie(TipoSerie.SILVER)
                .creadores(new ArrayList<>(List.of(personas.get(16))))
                .actores(new ArrayList<>(List.of(personas.get(17))))
                .build();
        Temporada casaT1 = Temporada.builder()
                .serie(laCasaDePapel)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo casaC1 = Capitulo.builder()
                .temporada(casaT1)
                .nombreCapitulo("Efectuar lo acordado")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/lacasadepapel/t1/e1")
                .descripcion("El Profesor pone en marcha el atraco.")
                .build();
        casaT1.setCapitulos(new ArrayList<>(List.of(casaC1)));
        laCasaDePapel.setTemporadas(new ArrayList<>(List.of(casaT1)));

        Serie theLastOfUs = Serie.builder()
                .nombreSerie("The Last of Us")
                .sinopsis("Dos supervivientes cruzan un mundo devastado buscando una posibilidad de futuro.")
                .tipoSerie(TipoSerie.GOLD)
                .creadores(new ArrayList<>(List.of(personas.get(18))))
                .actores(new ArrayList<>(List.of(personas.get(19))))
                .build();
        Temporada tlouT1 = Temporada.builder()
                .serie(theLastOfUs)
                .nombreTemporada("Temporada 1")
                .numeroTemporada(1)
                .build();
        Capitulo tlouC1 = Capitulo.builder()
                .temporada(tlouT1)
                .nombreCapitulo("Cuando estes perdido en la oscuridad")
                .numeroCapitulo(1)
                .enlace("https://polaflix.local/thelastofus/t1/e1")
                .descripcion("Joel recibe una mision que no esperaba.")
                .build();
        tlouT1.setCapitulos(new ArrayList<>(List.of(tlouC1)));
        theLastOfUs.setTemporadas(new ArrayList<>(List.of(tlouT1)));

        Serie arcane = crearSerieCatalogo(
                "Arcane",
                "Dos hermanas quedan atrapadas en el choque entre Piltover y Zaun.",
                TipoSerie.GOLD,
                personas.get(12),
                personas.get(13),
                "arcane",
                "Bienvenidos al patio",
                "Algunos misterios estan mejor sin resolver",
                "La violencia necesaria");

        Serie severance = crearSerieCatalogo(
                "Severance",
                "Trabajadores de Lumon separan su memoria laboral de su vida personal.",
                TipoSerie.GOLD,
                personas.get(6),
                personas.get(7),
                "severance",
                "Buenas noticias sobre el infierno",
                "Media vuelta",
                "In perpetuity");

        Serie theBear = crearSerieCatalogo(
                "The Bear",
                "Un chef vuelve a Chicago para levantar el restaurante familiar.",
                TipoSerie.SILVER,
                personas.get(4),
                personas.get(5),
                "thebear",
                "System",
                "Hands",
                "Brigade");

        Serie chernobyl = crearSerieCatalogo(
                "Chernobyl",
                "La catastrofe nuclear obliga a cientificos y autoridades a enfrentar la verdad.",
                TipoSerie.GOLD,
                personas.get(2),
                personas.get(3),
                "chernobyl",
                "1:23:45",
                "Please Remain Calm",
                "Open Wide, O Earth");

        Serie betterCallSaul = crearSerieCatalogo(
                "Better Call Saul",
                "Jimmy McGill construye el camino que le convertira en Saul Goodman.",
                TipoSerie.SILVER,
                personas.get(0),
                personas.get(1),
                "bettercallsaul",
                "Uno",
                "Mijo",
                "Nacho");

        Serie blackMirror = crearSerieCatalogo(
                "Black Mirror",
                "Relatos independientes exploran el lado mas oscuro de la tecnologia.",
                TipoSerie.SILVER,
                personas.get(8),
                personas.get(9),
                "blackmirror",
                "The National Anthem",
                "Fifteen Million Merits",
                "The Entire History of You");

        Serie andor = crearSerieCatalogo(
                "Andor",
                "Cassian Andor descubre el coste real de una rebelion naciente.",
                TipoSerie.GOLD,
                personas.get(14),
                personas.get(15),
                "andor",
                "Kassa",
                "That Would Be Me",
                "Reckoning");

        Serie succession = crearSerieCatalogo(
                "Succession",
                "Una familia poderosa lucha por el control de su imperio mediatico.",
                TipoSerie.GOLD,
                personas.get(18),
                personas.get(19),
                "succession",
                "Celebration",
                "Shit Show at the Fuck Factory",
                "Lifeboats");

        Serie theOffice = crearSerieCatalogo(
                "The Office",
                "La vida diaria de una oficina se convierte en una comedia documental.",
                TipoSerie.ESTANDAR,
                personas.get(4),
                personas.get(5),
                "theoffice",
                "Pilot",
                "Diversity Day",
                "Health Care");

        Serie peakyBlinders = crearSerieCatalogo(
                "Peaky Blinders",
                "La familia Shelby expande su poder en el Birmingham de posguerra.",
                TipoSerie.SILVER,
                personas.get(16),
                personas.get(17),
                "peakyblinders",
                "Episode 1",
                "Episode 2",
                "Episode 3");

        Serie westworld = crearSerieCatalogo(
                "Westworld",
                "Un parque futurista cuestiona los limites de la conciencia artificial.",
                TipoSerie.GOLD,
                personas.get(6),
                personas.get(7),
                "westworld",
                "The Original",
                "Chestnut",
                "The Stray");

        Serie fargo = crearSerieCatalogo(
                "Fargo",
                "Crimen, azar y humor negro se cruzan en historias aparentemente pequenas.",
                TipoSerie.SILVER,
                personas.get(10),
                personas.get(11),
                "fargo",
                "The Crocodile's Dilemma",
                "The Rooster Prince",
                "A Muddy Road");

        return List.of(breakingBad, theCrown, friends, inception, greysAnatomy, dark,
                strangerThings, mandalorian, laCasaDePapel, theLastOfUs, arcane, severance,
                theBear, chernobyl, betterCallSaul, blackMirror, andor, succession,
                theOffice, peakyBlinders, westworld, fargo);
    }

    private Serie crearSerieCatalogo(
            String nombre,
            String sinopsis,
            TipoSerie tipoSerie,
            Persona creador,
            Persona actor,
            String slug,
            String... capitulos) {
        Serie serie = Serie.crear(nombre, sinopsis, tipoSerie);
        serie.setCreadores(new ArrayList<>(List.of(creador)));
        serie.setActores(new ArrayList<>(List.of(actor)));

        Temporada temporada = Temporada.crear(serie, "Temporada 1", 1);
        List<Capitulo> capitulosSerie = new ArrayList<>();
        for (int index = 0; index < capitulos.length; index++) {
            capitulosSerie.add(Capitulo.crear(
                    temporada,
                    capitulos[index],
                    index + 1,
                    "https://polaflix.local/" + slug + "/t1/e" + (index + 1),
                    "Episodio " + (index + 1) + " de " + nombre + "."));
        }
        temporada.setCapitulos(capitulosSerie);
        serie.setTemporadas(new ArrayList<>(List.of(temporada)));
        return serie;
    }

    private List<Visualizacion> crearVisualizaciones(Usuario alejandro, Usuario mario, List<Serie> series) {
        Capitulo bbPilot = series.get(0).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo bbEpisode2 = series.get(0).getTemporadas().get(0).getCapitulos().get(1);
        Capitulo crownEpisode1 = series.get(1).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo friendsEpisode1 = series.get(2).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo friendsEpisode2 = series.get(2).getTemporadas().get(0).getCapitulos().get(1);
        Capitulo inceptionEpisode1 = series.get(3).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo darkEpisode1 = series.get(5).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo darkEpisode2 = series.get(5).getTemporadas().get(0).getCapitulos().get(1);

        Visualizacion v1 = visualizacion(alejandro, bbPilot, 2026, 3, 3);
        Visualizacion v2 = visualizacion(alejandro, bbEpisode2, 2026, 3, 4);
        Visualizacion v3 = visualizacion(alejandro, crownEpisode1, 2026, 3, 6);
        Visualizacion v4 = visualizacion(alejandro, inceptionEpisode1, 2026, 3, 10);
        Visualizacion v5 = visualizacion(alejandro, darkEpisode1, 2026, 3, 12);
        Visualizacion v6 = visualizacion(alejandro, darkEpisode2, 2026, 3, 13);
        Visualizacion v7 = visualizacion(mario, friendsEpisode1, 2026, 3, 8);
        Visualizacion v8 = visualizacion(mario, friendsEpisode2, 2026, 3, 9);
        Visualizacion v9 = visualizacion(alejandro, capitulo(series, 6, 0), 2026, 3, 16);
        Visualizacion v10 = visualizacion(alejandro, capitulo(series, 7, 0), 2026, 3, 18);
        Visualizacion v11 = visualizacion(alejandro, capitulo(series, 8, 0), 2026, 3, 22);
        Visualizacion v12 = visualizacion(alejandro, capitulo(series, 9, 0), 2026, 3, 25);
        Visualizacion v13 = visualizacion(alejandro, capitulo(series, 10, 0), 2026, 4, 2);
        Visualizacion v14 = visualizacion(alejandro, capitulo(series, 10, 1), 2026, 4, 3);
        Visualizacion v15 = visualizacion(alejandro, capitulo(series, 11, 0), 2026, 4, 5);
        Visualizacion v16 = visualizacion(alejandro, capitulo(series, 11, 1), 2026, 4, 6);
        Visualizacion v17 = visualizacion(alejandro, capitulo(series, 12, 0), 2026, 4, 9);
        Visualizacion v18 = visualizacion(alejandro, capitulo(series, 13, 0), 2026, 4, 12);
        Visualizacion v19 = visualizacion(alejandro, capitulo(series, 13, 1), 2026, 4, 13);
        Visualizacion v20 = visualizacion(alejandro, capitulo(series, 14, 0), 2026, 4, 17);
        Visualizacion v21 = visualizacion(alejandro, capitulo(series, 15, 0), 2026, 4, 21);
        Visualizacion v22 = visualizacion(alejandro, capitulo(series, 16, 0), 2026, 5, 1);
        Visualizacion v23 = visualizacion(alejandro, capitulo(series, 16, 1), 2026, 5, 2);
        Visualizacion v24 = visualizacion(alejandro, capitulo(series, 17, 0), 2026, 5, 5);
        Visualizacion v25 = visualizacion(alejandro, capitulo(series, 18, 0), 2026, 5, 8);
        Visualizacion v26 = visualizacion(alejandro, capitulo(series, 18, 1), 2026, 5, 9);
        Visualizacion v27 = visualizacion(alejandro, capitulo(series, 19, 0), 2026, 5, 13);
        Visualizacion v28 = visualizacion(alejandro, capitulo(series, 20, 0), 2026, 5, 16);
        Visualizacion v29 = visualizacion(alejandro, capitulo(series, 20, 1), 2026, 5, 17);
        Visualizacion v30 = visualizacion(alejandro, capitulo(series, 21, 0), 2026, 5, 22);

        return List.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10,
                v11, v12, v13, v14, v15, v16, v17, v18, v19, v20,
                v21, v22, v23, v24, v25, v26, v27, v28, v29, v30);
    }

    private Visualizacion visualizacion(Usuario usuario, Capitulo capitulo, int anio, int mes, int dia) {
        return Visualizacion.builder()
                .fechaVisualizacion(LocalDate.of(anio, mes, dia))
                .usuario(usuario)
                .capitulo(capitulo)
                .build();
    }

    private Capitulo capitulo(List<Serie> series, int indiceSerie, int indiceCapitulo) {
        return series.get(indiceSerie).getTemporadas().get(0).getCapitulos().get(indiceCapitulo);
    }

    private List<SeguimientoSerie> crearSeguimientos(
            Usuario alejandro,
            Usuario mario,
            List<Serie> series,
            List<Visualizacion> visualizaciones) {
        SeguimientoSerie s1 = SeguimientoSerie.builder()
                .serie(series.get(0))
                .ultimoVisto(visualizaciones.get(1).getCapitulo())
                .usuario(alejandro)
                .estadoSerie(EstadoSerie.EMPEZADA)
                .build();
        SeguimientoSerie s2 = SeguimientoSerie.builder()
                .serie(series.get(1))
                .ultimoVisto(visualizaciones.get(2).getCapitulo())
                .usuario(alejandro)
                .estadoSerie(EstadoSerie.TERMINADA)
                .build();
        SeguimientoSerie s3 = SeguimientoSerie.builder()
                .serie(series.get(3))
                .ultimoVisto(visualizaciones.get(3).getCapitulo())
                .usuario(alejandro)
                .estadoSerie(EstadoSerie.TERMINADA)
                .build();
        SeguimientoSerie s4 = SeguimientoSerie.builder()
                .serie(series.get(4))
                .ultimoVisto(null)
                .usuario(alejandro)
                .estadoSerie(EstadoSerie.PENDIENTE)
                .build();
        SeguimientoSerie s5 = SeguimientoSerie.builder()
                .serie(series.get(5))
                .ultimoVisto(visualizaciones.get(5).getCapitulo())
                .usuario(alejandro)
                .estadoSerie(EstadoSerie.TERMINADA)
                .build();
        SeguimientoSerie s6 = SeguimientoSerie.builder()
                .serie(series.get(2))
                .ultimoVisto(visualizaciones.get(7).getCapitulo())
                .usuario(mario)
                .estadoSerie(EstadoSerie.TERMINADA)
                .build();
        SeguimientoSerie s7 = SeguimientoSerie.builder()
                .serie(series.get(0))
                .ultimoVisto(null)
                .usuario(mario)
                .estadoSerie(EstadoSerie.PENDIENTE)
                .build();

        SeguimientoSerie s8 = seguimiento(alejandro, series.get(6), visualizaciones.get(8), EstadoSerie.TERMINADA);
        SeguimientoSerie s9 = seguimiento(alejandro, series.get(7), visualizaciones.get(9), EstadoSerie.TERMINADA);
        SeguimientoSerie s10 = seguimiento(alejandro, series.get(8), visualizaciones.get(10), EstadoSerie.TERMINADA);
        SeguimientoSerie s11 = seguimiento(alejandro, series.get(9), visualizaciones.get(11), EstadoSerie.TERMINADA);
        SeguimientoSerie s12 = seguimiento(alejandro, series.get(10), visualizaciones.get(13), EstadoSerie.EMPEZADA);
        SeguimientoSerie s13 = seguimiento(alejandro, series.get(11), visualizaciones.get(15), EstadoSerie.EMPEZADA);
        SeguimientoSerie s14 = seguimiento(alejandro, series.get(12), visualizaciones.get(16), EstadoSerie.EMPEZADA);
        SeguimientoSerie s15 = seguimiento(alejandro, series.get(13), visualizaciones.get(18), EstadoSerie.EMPEZADA);
        SeguimientoSerie s16 = seguimiento(alejandro, series.get(14), visualizaciones.get(19), EstadoSerie.EMPEZADA);
        SeguimientoSerie s17 = seguimiento(alejandro, series.get(15), visualizaciones.get(20), EstadoSerie.EMPEZADA);
        SeguimientoSerie s18 = seguimiento(alejandro, series.get(16), visualizaciones.get(22), EstadoSerie.EMPEZADA);
        SeguimientoSerie s19 = seguimiento(alejandro, series.get(17), visualizaciones.get(23), EstadoSerie.EMPEZADA);
        SeguimientoSerie s20 = seguimiento(alejandro, series.get(18), visualizaciones.get(25), EstadoSerie.EMPEZADA);
        SeguimientoSerie s21 = seguimiento(alejandro, series.get(19), visualizaciones.get(26), EstadoSerie.EMPEZADA);
        SeguimientoSerie s22 = seguimiento(alejandro, series.get(20), visualizaciones.get(28), EstadoSerie.EMPEZADA);
        SeguimientoSerie s23 = seguimiento(alejandro, series.get(21), visualizaciones.get(29), EstadoSerie.EMPEZADA);

        return List.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10,
                s11, s12, s13, s14, s15, s16, s17, s18, s19, s20,
                s21, s22, s23);
    }

    private SeguimientoSerie seguimiento(
            Usuario usuario,
            Serie serie,
            Visualizacion ultimaVisualizacion,
            EstadoSerie estadoSerie) {
        return SeguimientoSerie.builder()
                .serie(serie)
                .ultimoVisto(ultimaVisualizacion.getCapitulo())
                .usuario(usuario)
                .estadoSerie(estadoSerie)
                .build();
    }

    private Map<Serie, List<Visualizacion>> crearMapaVisualizacionesUsuario(
            Usuario usuario,
            List<Visualizacion> visualizaciones) {
        Map<Serie, List<Visualizacion>> visualizacionesUsuario = new LinkedHashMap<>();
        for (Visualizacion visualizacion : visualizaciones) {
            if (visualizacion.getUsuario() != usuario) {
                continue;
            }
            Serie serie = visualizacion.getCapitulo().getTemporada().getSerie();
            visualizacionesUsuario
                    .computeIfAbsent(serie, key -> new ArrayList<>())
                    .add(visualizacion);
        }
        return visualizacionesUsuario;
    }

    private ArrayList<Visualizacion> visualizacionesDe(
            Usuario usuario,
            List<Visualizacion> visualizaciones) {
        ArrayList<Visualizacion> visualizacionesUsuario = new ArrayList<>();
        for (Visualizacion visualizacion : visualizaciones) {
            if (visualizacion.getUsuario() == usuario) {
                visualizacionesUsuario.add(visualizacion);
            }
        }
        return visualizacionesUsuario;
    }

    private ArrayList<SeguimientoSerie> seguimientosDe(
            Usuario usuario,
            List<SeguimientoSerie> seguimientos) {
        ArrayList<SeguimientoSerie> seguimientosUsuario = new ArrayList<>();
        for (SeguimientoSerie seguimiento : seguimientos) {
            if (seguimiento.getUsuario() == usuario) {
                seguimientosUsuario.add(seguimiento);
            }
        }
        return seguimientosUsuario;
    }

    private List<Factura> crearFacturasAlejandro(Usuario alejandro) {
        Factura facturaMarzo = Factura.builder()
                .fecha(LocalDate.of(2026, 3, 31))
                .usuario(alejandro)
                .cargos(List.of(
                        new Cargo(LocalDate.of(2026, 3, 31), 20.0,
                                "Cuota fija mensual",
                                0,
                                0)))
                .build();

        Factura facturaAbril = Factura.builder()
                .fecha(LocalDate.of(2026, 4, 30))
                .usuario(alejandro)
                .cargos(List.of(
                        new Cargo(LocalDate.of(2026, 4, 30), 20.0,
                                "Cuota fija mensual",
                                0,
                                0)))
                .build();

        Factura facturaMayo = Factura.builder()
                .fecha(LocalDate.of(2026, 5, 31))
                .usuario(alejandro)
                .cargos(List.of(
                        new Cargo(LocalDate.of(2026, 5, 31), 20.0,
                                "Cuota fija mensual",
                                0,
                                0)))
                .build();

        return List.of(facturaMarzo, facturaAbril, facturaMayo);
   }

    private List<Factura> crearFacturasMario(Usuario mario, List<Serie> series) {
        Capitulo friendsEpisode1 = series.get(2).getTemporadas().get(0).getCapitulos().get(0);
        Capitulo friendsEpisode2 = series.get(2).getTemporadas().get(0).getCapitulos().get(1);

        Factura facturaMarzo = Factura.builder()
                .fecha(LocalDate.of(2026, 3, 31))
                .usuario(mario)
                .cargos(List.of(
                        new Cargo(LocalDate.of(2026, 3, 8), 0.5,
                                series.get(2).getNombreSerie(),
                                friendsEpisode1.getIdCapitulo(),
                                friendsEpisode1.getTemporada().getNumeroTemporada()),
                        new Cargo(LocalDate.of(2026, 3, 9), 0.5,
                                series.get(2).getNombreSerie(),
                                friendsEpisode2.getIdCapitulo(),
                                friendsEpisode2.getTemporada().getNumeroTemporada())))
                .build();

        return List.of(facturaMarzo);
    }
}
