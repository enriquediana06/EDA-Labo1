import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class GestorActoresPeliculasTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private GestorActoresPeliculas gestor;
    private Actor tom;
    private Actor meryl;
    private Pelicula gump;
    private Pelicula sophie;

    @Before
    public void setUp() {
        gestor = new GestorActoresPeliculas();
        tom = new Actor("A1", "Tom Hanks");
        meryl = new Actor("A2", "Meryl Streep");
        gump = new Pelicula("P1", "Forrest Gump", 1994);
        sophie = new Pelicula("P2", "Sophie's Choice", 1982);
    }

    private void insertarTodo() {
        gestor.insertarActor(tom);
        gestor.insertarActor(meryl);
        gestor.insertarPelicula(gump);
        gestor.insertarPelicula(sophie);
    }

    @Test
    public void insertarYBuscarActor() {
        gestor.insertarActor(tom);
        assertSame(tom, gestor.buscarActor("A1"));
    }

    @Test
    public void buscarActor_inexistenteDevuelveNull() {
        assertNull(gestor.buscarActor("NO_EXISTE"));
    }

    @Test
    public void insertarActor_conMismoIdSobrescribe() {
        gestor.insertarActor(tom);
        Actor otro = new Actor("A1", "Otro Nombre");
        gestor.insertarActor(otro);
        assertSame(otro, gestor.buscarActor("A1"));
    }

    @Test
    public void relacionar_creaRelacionEnAmbosSentidos() {
        insertarTodo();
        gestor.relacionarActorPelicula("A1", "P1");

        assertTrue(tom.getPeliculas().contains(gump));
        assertTrue(gump.getActores().contains(tom));
    }

    @Test
    public void relacionar_conActorInexistenteNoHaceNada() {
        insertarTodo();
        gestor.relacionarActorPelicula("NO_EXISTE", "P1");
        assertTrue(gump.getActores().isEmpty());
    }

    @Test
    public void relacionar_conPeliculaInexistenteNoHaceNada() {
        insertarTodo();
        gestor.relacionarActorPelicula("A1", "NO_EXISTE");
        assertTrue(tom.getPeliculas().isEmpty());
    }

    @Test
    public void obtenerPeliculasActor_devuelveSusPeliculas() {
        insertarTodo();
        gestor.relacionarActorPelicula("A1", "P1");
        gestor.relacionarActorPelicula("A1", "P2");

        Set<Pelicula> pelis = gestor.obtenerPeliculasActor("A1");
        assertEquals(2, pelis.size());
        assertTrue(pelis.contains(gump));
        assertTrue(pelis.contains(sophie));
    }

    @Test
    public void obtenerPeliculasActor_actorInexistenteDevuelveNull() {
        assertNull(gestor.obtenerPeliculasActor("NO_EXISTE"));
    }

    @Test
    public void obtenerActoresPelicula_devuelveSusActores() {
        insertarTodo();
        gestor.relacionarActorPelicula("A1", "P1");
        gestor.relacionarActorPelicula("A2", "P1");

        Set<Actor> actores = gestor.obtenerActoresPelicula("P1");
        assertEquals(2, actores.size());
        assertTrue(actores.contains(tom));
        assertTrue(actores.contains(meryl));
    }

    @Test
    public void obtenerActoresPelicula_peliculaInexistenteDevuelveNull() {
        assertNull(gestor.obtenerActoresPelicula("NO_EXISTE"));
    }

    @Test
    public void obtenerAnioPelicula_devuelveElAnio() {
        insertarTodo();
        assertEquals(1994, gestor.obtenerAnioPelicula("P1"));
    }

    @Test
    public void obtenerAnioPelicula_inexistenteDevuelveMenosUno() {
        assertEquals(-1, gestor.obtenerAnioPelicula("NO_EXISTE"));
    }

    @Test
    public void modificarAnioPelicula_cambiaElAnio() {
        insertarTodo();
        gestor.modificarAnioPelicula("P1", 2025);
        assertEquals(2025, gestor.obtenerAnioPelicula("P1"));
    }

    @Test
    public void modificarAnioPelicula_inexistenteNoLanzaExcepcion() {
        gestor.modificarAnioPelicula("NO_EXISTE", 2025);
    }

    @Test
    public void borrarActor_loEliminaDelGestor() {
        insertarTodo();
        gestor.borrarActor("A1");
        assertNull(gestor.buscarActor("A1"));
    }

    @Test
    public void borrarActor_loQuitaDeSusPeliculas() {
        insertarTodo();
        gestor.relacionarActorPelicula("A1", "P1");
        gestor.relacionarActorPelicula("A2", "P1");

        gestor.borrarActor("A1");

        Set<Actor> actores = gestor.obtenerActoresPelicula("P1");
        assertEquals(1, actores.size());
        assertFalse(actores.contains(tom));
        assertTrue(actores.contains(meryl));
    }

    @Test
    public void borrarActor_inexistenteNoLanzaExcepcion() {
        insertarTodo();
        gestor.borrarActor("NO_EXISTE");
        assertNotNull(gestor.buscarActor("A1"));
    }

    @Test
    public void obtenerActoresOrdenados_ordenAlfabeticoPorNombre() {
        gestor.insertarActor(new Actor("1", "Zendaya"));
        gestor.insertarActor(new Actor("2", "antonio"));
        gestor.insertarActor(new Actor("3", "Maria"));

        List<Actor> ordenados = gestor.obtenerActoresOrdenados();

        assertEquals(3, ordenados.size());
        assertEquals("antonio", ordenados.get(0).getNombre());
        assertEquals("Maria", ordenados.get(1).getNombre());
        assertEquals("Zendaya", ordenados.get(2).getNombre());
    }

    @Test
    public void obtenerActoresOrdenados_devuelveCopiaNoLaOriginal() {
        insertarTodo();
        List<Actor> ordenados = gestor.obtenerActoresOrdenados();
        ordenados.clear();
        assertNotNull(gestor.buscarActor("A1"));
        assertEquals(2, gestor.obtenerActoresOrdenados().size());
    }

    @Test
    public void obtenerActoresOrdenados_sinActoresDevuelveListaVacia() {
        assertTrue(gestor.obtenerActoresOrdenados().isEmpty());
    }

    @Test
    public void guardarEnFichero_escribeUnaLineaPorActor() throws IOException {
        insertarTodo();
        File fichero = new File(carpetaTemporal.getRoot(), "actores.txt");

        gestor.guardarEnFichero(fichero.getPath());

        List<String> lineas = Files.readAllLines(fichero.toPath());
        assertEquals(2, lineas.size());
        assertTrue(lineas.contains(tom.toString()));
        assertTrue(lineas.contains(meryl.toString()));
    }

    @Test
    public void guardarEnFichero_sinActoresCreaFicheroVacio() throws IOException {
        File fichero = new File(carpetaTemporal.getRoot(), "vacio.txt");

        gestor.guardarEnFichero(fichero.getPath());

        assertTrue(fichero.exists());
        assertEquals(0, Files.readAllLines(fichero.toPath()).size());
    }

    @Test
    public void guardarEnFichero_rutaInvalidaNoLanzaExcepcion() {
        File rutaInvalida = new File(new File(carpetaTemporal.getRoot(), "no_existe"), "f.txt");

        java.io.PrintStream errOriginal = System.err;
        System.setErr(new java.io.PrintStream(new java.io.ByteArrayOutputStream()));
        try {
            gestor.guardarEnFichero(rutaInvalida.getPath());
        } finally {
            System.setErr(errOriginal);
        }

        assertFalse(rutaInvalida.exists());
    }

    // 17 caracteres antes del año, porque cargarDatos usa substring(17, 21)
    private static final String PREFIJO = "xxxxxxxxxxxxxxxxx";

    private File crearFichero(File dir, int anio, String... lineas) throws IOException {
        File f = new File(dir, PREFIJO + anio + ".txt");
        Files.write(f.toPath(), Arrays.asList(lineas));
        return f;
    }

    @Test
    public void cargarDatos_creaActoresPeliculasYRelaciones() throws IOException {
        File dir = carpetaTemporal.getRoot();
        crearFichero(dir, 2020,
                "A1 ### Tom Hanks ### P1 ### Forrest Gump",
                "A2 ### Meryl Streep ### P1 ### Forrest Gump",
                "A1 ### Tom Hanks ### P2 ### Cast Away");

        gestor.cargarDatos(dir.getPath());

        assertNotNull(gestor.buscarActor("A1"));
        assertEquals("Tom Hanks", gestor.buscarActor("A1").getNombre());
        assertEquals(2, gestor.obtenerActoresOrdenados().size());
        assertEquals(2, gestor.obtenerPeliculasActor("A1").size());
        assertEquals(2, gestor.obtenerActoresPelicula("P1").size());
        assertEquals(2020, gestor.obtenerAnioPelicula("P1"));
    }

    @Test
    public void cargarDatos_noDuplicaActoresNiPeliculas() throws IOException {
        File dir = carpetaTemporal.getRoot();
        crearFichero(dir, 2020,
                "A1 ### Tom Hanks ### P1 ### Forrest Gump",
                "A1 ### Tom Hanks ### P1 ### Forrest Gump");

        gestor.cargarDatos(dir.getPath());

        assertEquals(1, gestor.obtenerActoresOrdenados().size());
        assertEquals(1, gestor.obtenerPeliculasActor("A1").size());
        assertEquals(1, gestor.obtenerActoresPelicula("P1").size());
    }

    @Test
    public void cargarDatos_leeVariosFicherosConSuAnio() throws IOException {
        File dir = carpetaTemporal.getRoot();
        crearFichero(dir, 2019, "A1 ### Tom Hanks ### P1 ### Pelicula 2019");
        crearFichero(dir, 2021, "A2 ### Meryl Streep ### P2 ### Pelicula 2021");

        gestor.cargarDatos(dir.getPath());

        assertEquals(2019, gestor.obtenerAnioPelicula("P1"));
        assertEquals(2021, gestor.obtenerAnioPelicula("P2"));
    }

    @Test
    public void cargarDatos_ignoraLineasConFormatoIncorrecto() throws IOException {
        File dir = carpetaTemporal.getRoot();
        crearFichero(dir, 2020,
                "A1 ### Tom Hanks ### P1",
                "esto no es una linea valida",
                "A2 ### Meryl ### P2 ### Titulo ### sobra",
                "A3 ### Julia Roberts ### P3 ### Pretty Woman");

        gestor.cargarDatos(dir.getPath());

        assertEquals(1, gestor.obtenerActoresOrdenados().size());
        assertNotNull(gestor.buscarActor("A3"));
        assertNull(gestor.buscarActor("A1"));
    }

    @Test
    public void cargarDatos_ignoraFicherosQueNoSonTxt() throws IOException {
        File dir = carpetaTemporal.getRoot();
        Files.write(new File(dir, PREFIJO + "2020.csv").toPath(),
                Arrays.asList("A1 ### Tom Hanks ### P1 ### Forrest Gump"));

        gestor.cargarDatos(dir.getPath());

        assertTrue(gestor.obtenerActoresOrdenados().isEmpty());
    }

    @Test
    public void cargarDatos_carpetaInexistenteNoLanzaExcepcion() {
        File noExiste = new File(carpetaTemporal.getRoot(), "no_existe");

        gestor.cargarDatos(noExiste.getPath());

        assertTrue(gestor.obtenerActoresOrdenados().isEmpty());
    }

    @Test
    public void cargarDatos_carpetaVaciaNoCargaNada() {
        gestor.cargarDatos(carpetaTemporal.getRoot().getPath());
        assertTrue(gestor.obtenerActoresOrdenados().isEmpty());
    }
}