import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Pelicula {
    private String id;
    private String titulo;
    private int anioEstreno;
    private Set<Actor> actores;

    public Pelicula(String id, String titulo, int anioEstreno) {
        this.id = id;
        this.titulo = (titulo != null) ? titulo.trim() : "";
        this.anioEstreno = anioEstreno;
        this.actores = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAnioEstreno() {
        return anioEstreno;
    }

    public void setAnioEstreno(int anioEstreno) {
        this.anioEstreno = anioEstreno;
    }

    public Set<Actor> getActores() {
        return actores;
    }

    // --- MÉTODOS DE RELACIÓN ---
    public void addActor(Actor a) {
        if (a != null) {
            this.actores.add(a);
        }
    }

    public void removeActor(Actor a) {
        if (a != null) {
            this.actores.remove(a);
        }
    }

    // --- MÉTODOS EQUALS Y HASHCODE (según los apuntes de Tablas Hash) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(id, pelicula.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pelicula{id='" + id + "', titulo='" + titulo + "', anio=" + anioEstreno + "}";
    }
}