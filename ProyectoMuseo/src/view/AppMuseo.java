package view;

import javax.swing.*;
import view.panel.InicioPanel;
import view.panel.PantallaCargaPanel;

public class AppMuseo {
    private final JFrame frame;

    /**
     * Constructor de la clase AppMuseo.
     * 
     * @param parentFrame El marco principal que contendra los componentes de la aplicacion.
     */
    public AppMuseo(JFrame parentFrame) {
        this.frame = parentFrame;
    }

    /**
     * Metodo para iniciar la aplicacion mostrando primero la pantalla de carga.
     */
    public void iniciarAplicacion() {
        mostrarPantallaDeCarga();
    }

    /**
     * Metodo privado que gestiona la pantalla de carga.
     * 
     * Crea un nuevo marco para la pantalla de carga, muestra un mensaje dinamico
     * y utiliza un hilo secundario para simular el proceso de carga antes de
     * mostrar la ventana principal.
     * 
     */
    private void mostrarPantallaDeCarga() {
        JFrame loadingFrame = new JFrame("Cargando...");
        loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadingFrame.setUndecorated(true);
        loadingFrame.setSize(300, 300);  
        loadingFrame.setLocationRelativeTo(null);  

        PantallaCargaPanel loadingPanel = new PantallaCargaPanel("Bienvenido! ");
        loadingFrame.setContentPane(loadingPanel); 
        loadingFrame.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(3550);
            } catch (InterruptedException e) {
            }
            SwingUtilities.invokeLater(() -> {
                loadingPanel.stopLoadingAnimation();
                loadingFrame.dispose();
                mostrarVentanaPrincipal();
            });
        }).start();
    }

    /**
     * Metodo privado que configura y muestra la ventana principal de la aplicacion.
     * 
     * La ventana principal incluye un panel de inicio y se ajusta al tamaño y posicion deseados.
     * 
     */
    private void mostrarVentanaPrincipal() {
        frame.add(new InicioPanel(frame));  
        frame.setSize(800, 600); 
        frame.setLocationRelativeTo(null);  
        frame.setVisible(true);  
    }
}
