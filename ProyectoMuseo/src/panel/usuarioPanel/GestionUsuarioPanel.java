package view.panel.usuarioPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import service.DatabaseManager;
import utils.BotonUtil;
import utils.EstiloUtil;
import view.addpanel.*;
import view.admin.AdminPanel;

public class GestionUsuarioPanel extends JPanel {
    private final JFrame parentFrame;

    /**
     * Panel de gestión de usuarios. Este panel permite al administrador gestionar usuarios
     * mediante opciones como agregar, listar, modificar o eliminar usuarios. 
     * Además, incluye un botón para regresar al panel de administración.
     * 
     * @param parentFrame El JFrame principal al que se le añadirán los distintos paneles.
     */
    public GestionUsuarioPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    
        setLayout(new BorderLayout());
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);
    
        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(EstiloUtil.FUENTE_TITULO);
        titulo.setForeground(EstiloUtil.COLOR_TITULO);
        titulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);
    
        JPanel contenedorCentral = new JPanel();
        contenedorCentral.setLayout(new BorderLayout());
        contenedorCentral.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
    
        JPanel botonesPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        botonesPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        botonesPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
    
        JButton agregarButton = BotonUtil.crearBoton("Agregar Usuario", "agregar.png", _ -> irAgregarUsuario());
        botonesPanel.add(agregarButton);
    
        JButton listarButton = BotonUtil.crearBoton("Listar Usuarios", "listar2.png", _ -> listarUsuarios());
        botonesPanel.add(listarButton);
    
        JButton modificarEliminarButton = BotonUtil.crearBoton("Modificar/Eliminar", "modificar.png", _ -> irModificarEliminarUsuario());
        botonesPanel.add(modificarEliminarButton);
    
        contenedorCentral.add(botonesPanel, BorderLayout.CENTER);
    
        JPanel volverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        volverPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        JButton volverButton = BotonUtil.crearBoton("Volver", "back.png", _ -> volverPanelAdmin());
        volverButton.setBackground(EstiloUtil.COLOR_VOLVER);
        volverButton.setPreferredSize(new Dimension(120, 40));
        volverPanel.add(volverButton);
    
        contenedorCentral.add(volverPanel, BorderLayout.SOUTH);
    
        add(contenedorCentral, BorderLayout.CENTER);
    
    }
    
    /**
     * Método que cambia la vista actual a la pantalla para agregar un nuevo usuario. 
     * Solicita la contraseña para validar el acceso antes de mostrar el panel correspondiente.
     */
    private void irAgregarUsuario() {
        if (solicitarContraseña()) {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new AddUsuarioPanel(parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Contraseña incorrecta. Acceso denegado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que cambia la vista actual a la pantalla para modificar o eliminar un usuario.
     * Solicita la contraseña para validar el acceso antes de mostrar el panel correspondiente.
     */
    private void irModificarEliminarUsuario() {
        if (solicitarContraseña()) {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new ModificarEliminarUsuarioPanel(parentFrame, null));
            parentFrame.revalidate();
            parentFrame.repaint();
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Contraseña incorrecta. Acceso denegado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que solicita la contraseña configurada para confirmar si el administrador
     * tiene permiso para realizar acciones de gestión de usuarios. Permite hasta 3 intentos.
     * 
     * @return true si la contraseña ingresada es correcta, false si no se ingresa la contraseña correctamente
     *         después de 3 intentos.
     */
    private boolean solicitarContraseña() {
        String contraseñaAlmacenada = obtenerContraseñaConfigurada();
        JPasswordField passwordField = new JPasswordField();
        int intentos = 0;
    
        while (intentos < 3) {
            int opcion = JOptionPane.showConfirmDialog(
                parentFrame,
                passwordField,
                "Ingrese la contraseña para continuar:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );
    
            if (opcion == JOptionPane.OK_OPTION) {
                String contraseñaIngresada = new String(passwordField.getPassword());
                if (contraseñaAlmacenada != null && contraseñaAlmacenada.equals(contraseñaIngresada)) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Contraseña incorrecta. Intento " + (intentos + 1) + " de 3.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return false;
            }
            intentos++;
        }
    
        JOptionPane.showMessageDialog(parentFrame, "Número máximo de intentos alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    

    /**
     * Método que obtiene la contraseña configurada desde la base de datos.
     * 
     * @return La contraseña almacenada en la configuración de la base de datos.
     */
    private String obtenerContraseñaConfigurada() {
        DatabaseManager dbManager = new DatabaseManager();
        return dbManager.obtenerContraseñaConfig(); 
    }

    /**
     * Método que regresa al panel de administración, removiendo el contenido actual y
     * añadiendo el panel correspondiente de administración.
     */
    private void volverPanelAdmin() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new AdminPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }


    /**
     * Método que cambia la vista actual a la pantalla para listar los usuarios existentes en la base de datos.
     */
    private void listarUsuarios() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new ListarUsuarioPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
