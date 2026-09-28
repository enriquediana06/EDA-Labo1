import java.util.HashMap;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
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
    
    public int obtenerAnioPelicula(String idPelicula) {

        Pelicula pelicula = peliculas.get(idPelicula);

        if (pelicula != null) {
            return pelicula.getAnioEstreno();
        }

        return -1;
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
    
    public List<Actor> obtenerActoresOrdenados() {
        List<Actor> lista = new ArrayList<>(actores.values()); //una copia pa no modificar la original

        Collections.sort(lista); //ordenamos la copia

        return lista;
    }
    public void cargarDatos(String nombreCarpeta) {

        File carpeta = new File(nombreCarpeta);

        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            System.out.println("No se ha encontrado la carpeta");
            return;
        }

        for (File archivo : archivos) {

            if (archivo.isFile() && archivo.getName().endsWith(".txt")) {

                try {

                    Scanner lector = new Scanner(archivo);

                    String nombreArchivo = archivo.getName();

                    int anio = Integer.parseInt(
                        nombreArchivo.substring(17, 21)
                    );

                    while (lector.hasNextLine()) {

                        String linea = lector.nextLine();

                        String[] datos = linea.split("\\s+###\\s+");

                        if (datos.length == 4) {

                            // CAMBIO: el id del actor es datos[0] (antes datos[1])
                            String idActor = datos[0];
                            // NUEVO: el nombre del actor es datos[1]
                            String nombreActor = datos[1];
                            String idPelicula = datos[2];
                            String titulo = datos[3];

                            // Creamos el actor si no existe
                            if (!actores.containsKey(idActor)) {

                                // CAMBIO: le pasamos su nombre real (antes ""), 
                                // necesario para que obtenerActoresOrdenados() ordene bien
                                Actor actor = new Actor(idActor, nombreActor);

                                insertarActor(actor);
                            }

                            // Creamos la película si no existe
                            if (!peliculas.containsKey(idPelicula)) {

                                Pelicula pelicula = new Pelicula(
                                    idPelicula,
                                    titulo,
                                    anio
                                );

                                insertarPelicula(pelicula);
                            }

                            // Relacionamos el actor con la película
                            relacionarActorPelicula(
                                idActor,
                                idPelicula
                            );
                        }
                    }

                    lector.close();

                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                }
            }
        }

        System.out.println("Actores cargados: " + actores.size());

        System.out.println("Películas cargadas: " + peliculas.size());
    }
    
    public static void main(String[] args) {

        GestorActoresPeliculas gestor = new GestorActoresPeliculas();

        System.out.println("Comenzando carga de datos...");

        gestor.cargarDatos("datos");

        // CAMBIO: id de actor completo (URL de Wikidata), antes solo "Q101080945"
        String idActor = "http://www.wikidata.org/entity/Q101080945";

        // buscar un actor
        Actor actor = gestor.buscarActor(idActor);

        System.out.println("Actor encontrado: " + actor);

        // CAMBIO: comprobamos que no sea null antes de usar .size()
        if (gestor.obtenerPeliculasActor(idActor) != null) {
            System.out.println("Películas del actor: "
                    + gestor.obtenerPeliculasActor(idActor).size());
        }

        // buscar los actores de una película
        String idPelicula = "http://www.wikidata.org/entity/Q12047846";

        System.out.println("Actores de la película: "
                + gestor.obtenerActoresPelicula(idPelicula).size());

        // modificamos el año de estreno
        System.out.println("Año original: "
                + gestor.peliculas.get(idPelicula).getAnioEstreno());

        gestor.modificarAnioPelicula(idPelicula, 2025);

        System.out.println("Año modificado: "
                + gestor.peliculas.get(idPelicula).getAnioEstreno());

        // para borrar un actor
        // CAMBIO: usamos el id completo
        gestor.borrarActor(idActor);

        System.out.println("Actor después de borrar: "
                + gestor.buscarActor(idActor));

        System.out.println("Actores de la película después de borrar: "
                + gestor.obtenerActoresPelicula(idPelicula).size());

        System.out.println("Carga terminada.");

        // guardar actores en un fichero
        gestor.guardarEnFichero("actores_guardados.txt");

        System.out.println("Actores guardados correctamente.");

        // actores ordenados
        System.out.println("Primeros actores ordenados:");

        // CAMBIO: Math.min para que no falle si hay menos de 10 actores
        List<Actor> ordenados = gestor.obtenerActoresOrdenados();
        for (Actor a : ordenados.subList(0, Math.min(10, ordenados.size()))) {
            System.out.println(a);
        }
    }
    
}

