import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PeliculaTest {

    private Pelicula pelicula;
    private Actor actor;

    @Before
    public void setUp() {
        pelicula = new Pelicula("P1", "Forrest Gump", 1994);
        actor = new Actor("A1", "Tom Hanks");
    }

    @Test
    public void constructor_asignaCampos() {
        assertEquals("P1", pelicula.getId());
        assertEquals("Forrest Gump", pelicula.getTitulo());
        assertEquals(1994, pelicula.getAnioEstreno());
    }

    @Test
    public void constructor_hacetrimAlTitulo() {
        Pelicula p = new Pelicula("P2", "   Titanic  ", 1997);
        assertEquals("Titanic", p.getTitulo());
    }

    @Test
    public void constructor_tituloNullSeConvierteEnCadenaVacia() {
        Pelicula p = new Pelicula("P3", null, 2000);
        assertEquals("", p.getTitulo());
    }

    @Test
    public void constructor_iniciaConActoresVacios() {
        assertNotNull(pelicula.getActores());
        assertTrue(pelicula.getActores().isEmpty());
    }

    @Test
    public void setAnioEstreno_modificaElAnio() {
        pelicula.setAnioEstreno(2025);
        assertEquals(2025, pelicula.getAnioEstreno());
    }

    @Test
    public void addActor_anadeActor() {
        pelicula.addActor(actor);
        assertEquals(1, pelicula.getActores().size());
        assertTrue(pelicula.getActores().contains(actor));
    }

    @Test
    public void addActor_noDuplicaElMismoActor() {
        pelicula.addActor(actor);
        pelicula.addActor(actor);
        pelicula.addActor(new Actor("A1", "Otro nombre"));
        assertEquals(1, pelicula.getActores().size());
    }

    @Test
    public void addActor_ignoraNull() {
        pelicula.addActor(null);
        assertTrue(pelicula.getActores().isEmpty());
    }

    @Test
    public void removeActor_eliminaActorExistente() {
        pelicula.addActor(actor);
        pelicula.removeActor(actor);
        assertFalse(pelicula.getActores().contains(actor));
        assertTrue(pelicula.getActores().isEmpty());
    }

    @Test
    public void removeActor_conActorNoPresenteNoHaceNada() {
        pelicula.addActor(actor);
        pelicula.removeActor(new Actor("A99", "Desconocido"));
        assertEquals(1, pelicula.getActores().size());
    }

    @Test
    public void removeActor_ignoraNull() {
        pelicula.addActor(actor);
        pelicula.removeActor(null);
        assertEquals(1, pelicula.getActores().size());
    }

    @Test
    public void equals_mismoObjeto() {
        assertEquals(pelicula, pelicula);
    }

    @Test
    public void equals_mismoIdDistintoTituloYAnio() {
        Pelicula otra = new Pelicula("P1", "Otro titulo", 2020);
        assertEquals(pelicula, otra);
        assertEquals(pelicula.hashCode(), otra.hashCode());
    }

    @Test
    public void equals_distintoId() {
        Pelicula otra = new Pelicula("P2", "Forrest Gump", 1994);
        assertFalse(pelicula.equals(otra));
    }

    @Test
    public void equals_conNullYOtraClase() {
        assertFalse(pelicula.equals(null));
        assertFalse(pelicula.equals("P1"));
    }

    @Test
    public void toString_formatoEsperado() {
        assertEquals("Pelicula{id='P1', titulo='Forrest Gump', anio=1994}",
                pelicula.toString());
    }
}