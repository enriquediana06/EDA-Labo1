import java.util.HashMap;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Set;
public class GestorActoresPeliculas {

    private HashMap<String, Actor> actores;
    private HashMap<String, Pelicula> peliculas;

    public GestorActoresPeliculas() {
        actores = new HashMap<>();
        peliculas = new HashMap<>();
    }

    public void insertarActor(Actor actor) {
        actores.put(actor.getId(), actor);
    }

    public void insertarPelicula(Pelicula pelicula) {
        peliculas.put(pelicula.getId(), pelicula);
    }

    public Actor buscarActor(String id) {
        return actores.get(id);
    }
    
    public Set<Pelicula> obtenerPeliculasActor(String idActor) {

        Actor actor = actores.get(idActor);

        if (actor != null) {
            return actor.getPeliculas();
        }

        return null;
    }

    public void relacionarActorPelicula(String idActor, String idPelicula) {

        Actor actor = actores.get(idActor);
        Pelicula pelicula = peliculas.get(idPelicula);

        if (actor != null && pelicula != null) {
            actor.addPelicula(pelicula);
            pelicula.addActor(actor);
        }
    }

    public Set<Actor> obtenerActoresPelicula(String idPelicula) {

        Pelicula pelicula = peliculas.get(idPelicula);

        if (pelicula != null) {
            return pelicula.getActores();
        }

        return null;
    }

    public void modificarAnioPelicula(String idPelicula, int nuevoAnio) {

        Pelicula pelicula = peliculas.get(idPelicula);

        if (pelicula != null) {
            pelicula.setAnioEstreno(nuevoAnio);
        }
    }
    
    public void borrarActor(String idActor) {

        Actor actor = actores.get(idActor);

        if (actor != null) {

            for (Pelicula pelicula : actor.getPeliculas()) {
                pelicula.removeActor(actor);
            }

            actores.remove(idActor);
        }
    }
    public void guardarEnFichero(String nombreFichero) {
    	try {
    	    PrintWriter escritor = new PrintWriter(nombreFichero);

    	    for (Actor actor : actores.values()) {
    	        escritor.println(actor);
    	    }
    	    escritor.close();
    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();
    	}
    }

    public static void main(String[] args) {
       
        GestorActoresPeliculas gestor = new GestorActoresPeliculas();
     
        Actor actor1 = new Actor("Q123", "Brad Pitt");
        gestor.insertarActor(actor1);

        Pelicula pelicula1 = new Pelicula("P001", "Seven", 1995);
        gestor.insertarPelicula(pelicula1);

        gestor.relacionarActorPelicula("Q123", "P001");

        System.out.println(actor1.getPeliculas());
        System.out.println(gestor.obtenerActoresPelicula("P001"));
        System.out.println(gestor.obtenerPeliculasActor("Q123"));
        
        Actor encontrado = gestor.buscarActor("Q123");
        System.out.println(encontrado);

        gestor.modificarAnioPelicula("P001", 1996);

        System.out.println(pelicula1);
        gestor.guardarEnFichero("actores.txt");
        gestor.borrarActor("Q123");

        System.out.println(gestor.buscarActor("Q123"));
        System.out.println(pelicula1.getActores());
        
       
      
    }
}
