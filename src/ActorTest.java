import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ActorTest {

    private Actor actor;
    private Pelicula pelicula;

    @Before
    public void setUp() {
        actor = new Actor("A1", "Tom Hanks");
        pelicula = new Pelicula("P1", "Forrest Gump", 1994);
    }

    @Test
    public void constructor_asignaIdYNombre() {
        assertEquals("A1", actor.getId());
        assertEquals("Tom Hanks", actor.getNombre());
    }

    @Test
    public void constructor_hacetrimAlNombre() {
        Actor a = new Actor("A2", "   Meryl Streep   ");
        assertEquals("Meryl Streep", a.getNombre());
    }

    @Test
    public void constructor_nombreNullSeConvierteEnCadenaVacia() {
        Actor a = new Actor("A3", null);
        assertEquals("", a.getNombre());
    }

    @Test
    public void constructor_iniciaConListaDePeliculasVacia() {
        assertNotNull(actor.getPeliculas());
        assertTrue(actor.getPeliculas().isEmpty());
    }

    @Test
    public void addPelicula_anadePelicula() {
        actor.addPelicula(pelicula);
        assertEquals(1, actor.getPeliculas().size());
        assertTrue(actor.getPeliculas().contains(pelicula));
    }

    @Test
    public void addPelicula_noDuplicaLaMismaPelicula() {
        actor.addPelicula(pelicula);
        actor.addPelicula(pelicula);
        actor.addPelicula(new Pelicula("P1", "Otro titulo", 2000));
        assertEquals(1, actor.getPeliculas().size());
    }

    @Test
    public void addPelicula_ignoraNull() {
        actor.addPelicula(null);
        assertTrue(actor.getPeliculas().isEmpty());
    }

    @Test
    public void removePelicula_eliminaPeliculaExistente() {
        actor.addPelicula(pelicula);
        actor.removePelicula(pelicula);
        assertFalse(actor.getPeliculas().contains(pelicula));
        assertTrue(actor.getPeliculas().isEmpty());
    }

    @Test
    public void removePelicula_conPeliculaNoPresenteNoHaceNada() {
        actor.addPelicula(pelicula);
        actor.removePelicula(new Pelicula("P99", "Otra", 2010));
        assertEquals(1, actor.getPeliculas().size());
    }

    @Test
    public void removePelicula_ignoraNull() {
        actor.addPelicula(pelicula);
        actor.removePelicula(null);
        assertEquals(1, actor.getPeliculas().size());
    }

    @Test
    public void equals_mismoObjeto() {
        assertEquals(actor, actor);
    }

    @Test
    public void equals_mismoIdDistintoNombre() {
        Actor otro = new Actor("A1", "Nombre distinto");
        assertEquals(actor, otro);
        assertEquals(actor.hashCode(), otro.hashCode());
    }

    @Test
    public void equals_distintoId() {
        Actor otro = new Actor("A2", "Tom Hanks");
        assertFalse(actor.equals(otro));
    }

    @Test
    public void equals_conNullYOtraClase() {
        assertFalse(actor.equals(null));
        assertFalse(actor.equals("A1"));
    }

    @Test
    public void compareTo_ordenAlfabetico() {
        Actor ana = new Actor("1", "Ana");
        Actor luis = new Actor("2", "Luis");
        assertTrue(ana.compareTo(luis) < 0);
        assertTrue(luis.compareTo(ana) > 0);
    }

    @Test
    public void compareTo_ignoraMayusculas() {
        Actor a = new Actor("1", "ana");
        Actor b = new Actor("2", "ANA");
        assertEquals(0, a.compareTo(b));
    }

    @Test
    public void toString_formatoEsperado() {
        assertEquals("Actor{id='A1', nombre='Tom Hanks'}", actor.toString());
    }
}