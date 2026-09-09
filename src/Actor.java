import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Actor implements Comparable<Actor> {
    private String id;
    private String nombre;
    private Set<Pelicula> peliculas;

    public Actor(String id, String nombre) {
        this.id = id;
        this.nombre = (nombre != null) ? nombre.trim() : "";
        this.peliculas = new HashSet<>();
    }

    // --- GETTERS ---
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Set<Pelicula> getPeliculas() {
        return peliculas;
    }

    // --- MÉTODOS DE RELACIÓN ---
    public void addPelicula(Pelicula p) {
        if (p != null) {
            this.peliculas.add(p);
        }
    }

    public void removePelicula(Pelicula p) {
        if (p != null) {
            this.peliculas.remove(p);
        }
    }

    // --- MÉTODOS EQUALS Y HASHCODE (según los apuntes de Tablas Hash) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(id, actor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- ORDENACIÓN (para cumplir el requisito de devolver lista ordenada) ---
    @Override
    public int compareTo(Actor o) {
        return this.nombre.compareToIgnoreCase(o.nombre);
    }

    @Override
    public String toString() {
        return "Actor{id='" + id + "', nombre='" + nombre + "'}";
    }
}
