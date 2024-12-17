package view.admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import service.DatabaseManager;
import utils.*;
import view.panel.*;
import view.panel.pinturas.GestionPinturasPanel;
import view.panel.usuarios.GestionUsuarioPanel;

/**
 * Panel de administración que contiene botones para gestionar usuarios, pinturas y salir de la sesión.
 * Este panel se presenta como la interfaz principal para los administradores de la aplicación.
 * 
 * La clase maneja la navegación a otros paneles y la acción de cerrar sesión, permitiendo al administrador
 * interactuar con los módulos de gestión de usuarios y pinturas.
 */
public class AdminPanel extends JPanel {
    private final JFrame parentFrame;
    private final DatabaseManager dbManager;

    /**
     * Constructor principal de la clase AdminPanel.
     * Inicializa el panel con la configuración básica, los botones y los eventos asociados.
     * 
     * @param parentFrame La ventana principal que contiene este panel.
     */
    public AdminPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.dbManager = new DatabaseManager();
    
        setLayout(new BorderLayout());
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);
    
        JLabel titulo = EstiloUtil.crearTitulo("Panel de Administración");
        add(titulo, BorderLayout.NORTH);
    
        JPanel accionesPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        accionesPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO); 
        accionesPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
    
        JButton gestionarUsuariosButton = BotonUtil.crearBoton("Usuarios", "usuarios.png", _ -> irGestionUsuarios());
        accionesPanel.add(gestionarUsuariosButton);
    
        JButton gestionarPinturasButton = BotonUtil.crearBoton("Pinturas", "pintura.png", _ -> irGestionPinturas());
        accionesPanel.add(gestionarPinturasButton);
    
        add(accionesPanel, BorderLayout.CENTER);
    
        JPanel salirPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        salirPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
    
        JButton cerrarSesionButton = BotonUtil.crearBoton("Salir", "salir.png", _ -> regresarAlInicio());
        cerrarSesionButton.setBackground(EstiloUtil.COLOR_VOLVER);
        cerrarSesionButton.setForeground(EstiloUtil.COLOR_TEXTO);
        cerrarSesionButton.setPreferredSize(new Dimension(120, 40)); 
        cerrarSesionButton.setFocusPainted(false); 
        cerrarSesionButton.setBorder(BorderFactory.createLineBorder(EstiloUtil.COLOR_ERROR));
    
        salirPanel.add(cerrarSesionButton); 
    
        add(salirPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Cambia a la vista de gestión de usuarios.
     * Se actualiza el contenido de la ventana principal para mostrar el panel de gestión de usuarios.
     */
    private void irGestionUsuarios() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new GestionUsuarioPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint(); 
    }

    /**
     * Cambia a la vista de gestión de pinturas.
     * Se actualiza el contenido de la ventana principal para mostrar el panel de gestión de pinturas.
     */
    private void irGestionPinturas() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new GestionPinturasPanel(dbManager, parentFrame, parentFrame));
        parentFrame.revalidate(); 
        parentFrame.repaint();
    }

    /**
     * Regresa al panel de inicio.
     * Se actualiza el contenido de la ventana principal para mostrar el panel de inicio.
     */
    private void regresarAlInicio() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new InicioPanel(parentFrame));
        parentFrame.revalidate(); 
        parentFrame.repaint();  
    }
}
