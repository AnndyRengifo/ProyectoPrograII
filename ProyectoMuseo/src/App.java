/**
 * Clase principal de la aplicacion "ArtVision".
 * Este punto de entrada inicializa la base de datos, crea un usuario por defecto y
 * lanza la interfaz grafica de usuario (GUI) de la aplicacion.
 * 
 * La aplicacion está diseñada para gestionar un museo, ofreciendo funcionalidades
 * tanto para usuarios visitantes como para administradores.
 * 
 */
import java.awt.HeadlessException;
import javax.swing.*;
import service.DatabaseManager;
import view.AppMuseo;

public class App {

    /**
     * Metodo principal que inicia la aplicacion.
     * 
     * @param args Argumentos de linea de comandos (no utilizados en esta aplicación).
     */

    public static void main(String[] args) {
        try {
            System.out.println("Haz ejecutado la aplicación de ArtVision!! :)");

            DatabaseManager.initializeDatabase();
            DatabaseManager.crearUsuarioPorDefecto();

            JFrame frame = new JFrame("ArtVision");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            AppMuseo aplicacion = new AppMuseo(frame);
            aplicacion.iniciarAplicacion();

        } catch (HeadlessException e) {
            System.out.println("Ocurrió un error al iniciar la aplicación: " + e.getMessage());
        }
    }
}
