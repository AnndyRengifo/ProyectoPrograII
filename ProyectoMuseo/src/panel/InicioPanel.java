package view.panel;

import java.awt.*;
import javax.swing.*;
import utils.*;

public class InicioPanel extends JPanel {
    private final JFrame parentFrame;

    /**
     * Constructor que inicializa el panel principal de inicio del Museo de Arte.
     * Configura el fondo, el título, los botones para escanear pintura y el acceso al login.
     * 
     * @param parentFrame El JFrame principal al que se le añadirán los distintos paneles.
     */
    public InicioPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);
 
        JLabel titulo = EstiloUtil.crearTitulo("MUSEO DE ARTE");
        add(titulo, BorderLayout.NORTH);

        JPanel centroPanel = EstiloUtil.crearPanelTransparente();
        centroPanel.setLayout(new GridBagLayout());

        JButton visitanteButton = BotonUtil.crearBoton("Escanear Pintura", "scan.png", _ -> irModoVisitante());
        centroPanel.add(visitanteButton);
        add(centroPanel, BorderLayout.CENTER);

        JPanel loginPanel = EstiloUtil.crearPanelTransparente();
        loginPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton loginButton = BotonUtil.crearBoton("Login", "", _ -> irLogin());
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginPanel.add(loginButton);

        add(loginPanel, BorderLayout.SOUTH);
    }

    /**
     * Método que cambia la vista actual a la vista de modo visitante. Remueve el contenido
     * actual del JFrame y agrega el panel `PanelVisitante` para permitir la interacción
     * con las opciones disponibles para el visitante.
     */
    private void irModoVisitante() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new PanelVisitante(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    /**
     * Método que cambia la vista actual a la vista de login para el administrador.
     * Remueve el contenido actual del JFrame y agrega el panel `LoginAdminPanel`
     * para permitir que el administrador inicie sesión.
     */
    private void irLogin() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new IngresoAdminPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
