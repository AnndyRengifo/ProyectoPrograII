package view.panel.usuarioPanel;

import controller.UsuarioManager;
import models.Usuario;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

import utils.BotonUtil;
import utils.EstiloUtil;

/**
 * Panel que permite modificar o eliminar un usuario existente.
 * Este panel incluye funcionalidad para buscar un usuario por su ID o username,
 * modificar sus datos (nombre, rol, username) y eliminarlo de la base de datos.
 * 
 * El panel se organiza en varias secciones:
 * - Un campo de búsqueda para encontrar usuarios por su ID o username.
 * - Campos de texto para modificar la información del usuario seleccionado.
 * - Botones para realizar acciones como guardar cambios, eliminar el usuario o volver al panel anterior.
 */
public class ModificarEliminarUsuarioPanel extends JPanel {

    private final JTextField searchField, idField, nombreField, usernameField;
    private final JComboBox<String> rolComboBox;
    private final JLabel mensajeEstado;
    private final UsuarioManager usuarioController;
    
    /**
     * Constructor para inicializar el panel de modificación y eliminación de usuarios.
     * 
     * @param parentFrame El marco principal donde se mostrará este panel.
     * @param conn La conexión activa a la base de datos.
     */
    public ModificarEliminarUsuarioPanel(JFrame parentFrame, Connection conn) {
        this.usuarioController = new UsuarioManager();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 10));
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        tituloPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        JLabel titulo = new JLabel("Modificar Usuarios", SwingConstants.CENTER);
        titulo.setFont(EstiloUtil.FUENTE_TITULO);
        titulo.setForeground(EstiloUtil.COLOR_TITULO);
        tituloPanel.add(titulo);
        add(tituloPanel);

        mensajeEstado = new JLabel("");
        mensajeEstado.setFont(new Font("Arial", Font.BOLD, 14));
        mensajeEstado.setForeground(EstiloUtil.COLOR_ERROR); 
        add(mensajeEstado);

        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        panelBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        panelBusqueda.add(new JLabel("Buscar por ID o Username:"));
        searchField = new JTextField(20);
        panelBusqueda.add(searchField);
        JButton buscarButton = BotonUtil.crearBoton("Buscar", "buscar.png", _ -> buscarUsuario());
        panelBusqueda.add(buscarButton);
        panelBusqueda.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        add(panelBusqueda);

        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(4, 2, 15, 20)); 
        panelCampos.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        panelCampos.setAlignmentX(CENTER_ALIGNMENT);
        
        panelCampos.add(new JLabel("ID de la Credencial:"));
        idField = new JTextField();
        idField.setEnabled(false);
        panelCampos.add(idField);

        panelCampos.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        panelCampos.add(nombreField);

        panelCampos.add(new JLabel("Rol:"));
        String[] roles = {"Supervisor", "Administrador"};
        rolComboBox = new JComboBox<>(roles);
        panelCampos.add(rolComboBox);

        panelCampos.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setEnabled(true);
        panelCampos.add(usernameField);

        add(panelCampos);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        panelBotones.add(Box.createVerticalStrut(55)); 

        JPanel volverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        volverPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        JButton volverButton = BotonUtil.crearBoton("Volver", "back.png", _ -> volver(parentFrame));
        volverButton.setPreferredSize(new Dimension(120, 40));
        volverButton.setBackground(EstiloUtil.COLOR_VOLVER);
        volverPanel.add(volverButton);

        JPanel botonesCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonesCentro.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        JButton guardarButton = BotonUtil.crearBoton("Guardar Cambios", "guardar.png", _ -> modificarUsuario());
        botonesCentro.add(guardarButton);

        JButton eliminarButton = BotonUtil.crearBoton("Eliminar Usuario", "usuarioeliminado.png", _ -> eliminarUsuario());
        botonesCentro.add(eliminarButton);

        panelBotones.add(botonesCentro);
        panelBotones.add(Box.createVerticalStrut(50));
        panelBotones.add(volverPanel, BorderLayout.SOUTH);

        add(panelBotones); 
    }

     /**
     * Método que busca un usuario en la base de datos utilizando el ID o username.
     */
    private void buscarUsuario() {
        String criterio = searchField.getText();
        if (criterio.isEmpty()) {
            mostrarMensaje("Por favor, ingrese un ID o Username.", EstiloUtil.COLOR_ERROR);
            return;
        }

        Usuario usuario = usuarioController.buscarUsuario(criterio);
        if (usuario == null) {
            mostrarMensaje("Usuario no encontrado.", EstiloUtil.COLOR_ERROR);
        } else {
            idField.setText(usuario.getId());
            nombreField.setText(usuario.getNombre());
            rolComboBox.setSelectedItem(usuario.getRol());
            usernameField.setText(usuario.getUsername());
            mostrarMensaje("Usuario cargado correctamente.", EstiloUtil.COLOR_EXITO);
        }
    }

    /**
     * Método para modificar la información del usuario.
     */
    private void modificarUsuario() {
        String id = idField.getText();
        String nombre = nombreField.getText();
        String rol = (String) rolComboBox.getSelectedItem();
        String username = usernameField.getText();
    
        if (id.equals("0000000000001") || username.equals("Anndy")) {
            mostrarMensaje("No se puede modificar datos del Administrador Principal!.", EstiloUtil.COLOR_ERROR);
            return;
        }
    
        if (id.isEmpty() || nombre.isEmpty() || rol.isEmpty() || username.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios.", EstiloUtil.COLOR_ERROR);
            return;
        }
    
        boolean exito = usuarioController.modificarUsuario(id, nombre, rol, username);
        if (exito) {
            mostrarMensaje("Usuario modificado correctamente.", EstiloUtil.COLOR_EXITO);
        } else {
            mostrarMensaje("Error al modificar el usuario.", EstiloUtil.COLOR_ERROR);
        }
    }
    
    /**
     * Método para eliminar un usuario de la base de datos.
     */
    private void eliminarUsuario() {
        String id = idField.getText();
        String username = usernameField.getText();
    
        if (username.equals("Anndy")) {
            mostrarMensaje("No se puede eliminar al Administrador Principal!.", EstiloUtil.COLOR_ERROR);
            return;
        }
    
        if (id.isEmpty()) {
            mostrarMensaje("ID no válido para eliminar.", EstiloUtil.COLOR_ERROR);
            return;
        }
    
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar este usuario?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
    
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = usuarioController.eliminarUsuario(id);
            if (exito) {
                mostrarMensaje("Usuario eliminado correctamente.", EstiloUtil.COLOR_EXITO);
                limpiarCampos();
            } else {
                mostrarMensaje("Error al eliminar el usuario.", EstiloUtil.COLOR_ERROR);
            }
        }
    }

    /**
     * Limpia los campos de entrada después de eliminar un usuario.
     */
    private void limpiarCampos() {
        searchField.setText("");
        idField.setText("");
        nombreField.setText("");
        usernameField.setText("");
        rolComboBox.setSelectedIndex(0);
    }

    /**
     * Muestra un mensaje en la etiqueta de estado.
     * 
     * @param mensaje El mensaje a mostrar.
     * @param color El color del mensaje (normalmente de error o éxito).
     */
    private void mostrarMensaje(String mensaje, Color color) {
        mensajeEstado.setText(mensaje);
        mensajeEstado.setForeground(color);
    }

    /**
     * Regresa al panel de gestión de usuarios.
     */
    private void volver(JFrame parentFrame) {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new GestionUsuarioPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
