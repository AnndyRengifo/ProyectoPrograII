package view.addpanel;

import controller.UsuarioManager;
import java.awt.*;
import javax.swing.*;
import service.DatabaseConnection;
import utils.*;
import view.panel.usuarioPanel.GestionUsuarioPanel;

public class AddUsuarioPanel extends JPanel {

    private final JTextField tfNombre;
    private final JTextField tfUsername;
    private final JTextField tfId;
    private final JPasswordField tfPass;
    private final JComboBox<String> rolComboBox;
    private final UsuarioManager usuarioController;
    private final JLabel mensajeEstado;
    private final JFrame parentFrame;

    /**
     * Constructor que inicializa el panel y configura el formulario de agregar usuario
     * @param parentFrame
     */
    public AddUsuarioPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        usuarioController = new UsuarioManager();

        setLayout(new BorderLayout());
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        JPanel topPanel = EstiloUtil.crearPanelTransparente();
        topPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        topPanel.add(EstiloUtil.crearTitulo("Agregar Usuario"));
        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = EstiloUtil.crearPanelTransparente();
        formPanel.setLayout(new GridLayout(6, 2, 0, 20));

        formPanel.add(EstiloUtil.crearTextoSecundario("Nombre:"));
        tfNombre = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfNombre);

        JLabel rolLabel = new JLabel("Rol:");
        formPanel.add(rolLabel);
        rolComboBox = new JComboBox<>(new String[]{"Supervisor", "Administrador"});
        formPanel.add(rolComboBox);

        formPanel.add(EstiloUtil.crearTextoSecundario("Username:"));
        tfUsername = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfUsername);

        formPanel.add(EstiloUtil.crearTextoSecundario("Password:"));
        tfPass = EstiloUtil.crearCampoTextoPassSecundario();
        formPanel.add(tfPass);

        formPanel.add(EstiloUtil.crearTextoSecundario("ID de Credencial:"));
        tfId = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfId);


        mensajeEstado = new JLabel("", SwingConstants.CENTER);
        formPanel.add(mensajeEstado); 
        formPanel.add(new JLabel()); 

        JPanel centrarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centrarPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        centrarPanel.add(formPanel);

        add(centrarPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        JPanel volverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        volverPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        JButton volverButton = BotonUtil.crearBoton("Volver", "back.png", _ -> irGestionUsuarios());
        volverButton.setBackground(EstiloUtil.COLOR_VOLVER);
        volverButton.setForeground(EstiloUtil.COLOR_TEXTO);
        volverButton.setPreferredSize(new Dimension(120, 40));
        volverPanel.add(volverButton);

        buttonPanel.add(volverPanel, BorderLayout.SOUTH);

        JPanel agregarContainer = new JPanel();
        agregarContainer.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        JButton agregarButton = BotonUtil.crearBoton("Agregar", "usuarioadd.png", _ -> manejarAgregarUsuario());
        agregarContainer.add(agregarButton);

        buttonPanel.add(agregarContainer, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Método para manejar el evento de agregar usuario.
     */
    private void manejarAgregarUsuario() {
        String nombre = tfNombre.getText();
        String rol = (String) rolComboBox.getSelectedItem();
        String username = tfUsername.getText();
        String password = new String(tfPass.getPassword());
        String id = tfId.getText();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || id.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos.", EstiloUtil.COLOR_ERROR);
            return;
        }

        if (DatabaseConnection.connect() != null) {
            String resultado = usuarioController.agregarUsuario(id, nombre, rol, username, password);
            if (resultado.startsWith("Error:")) {
                mostrarMensaje(resultado, EstiloUtil.COLOR_ERROR);
            } else {
                mostrarMensaje(resultado, EstiloUtil.COLOR_EXITO);
                limpiarFormulario();
            }
        } else {
            mostrarMensaje("Error: No se pudo establecer conexión con la base de datos.", EstiloUtil.COLOR_ERROR);
        }
    }

    /**
     * Mostrar un mensaje dinámico en la etiqueta de estado.
     */
    private void mostrarMensaje(String mensaje, Color color) {
        mensajeEstado.setText(mensaje);
        mensajeEstado.setForeground(color);
    }

    /**
     * Limpiar los campos del formulario.
     */
    private void limpiarFormulario() {
        tfNombre.setText("");
        tfUsername.setText("");
        tfPass.setText("");
        tfId.setText("");
        rolComboBox.setSelectedIndex(0);
    }

    /**
     * Volver al panel de gestión de usuarios.
     */
    private void irGestionUsuarios() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new GestionUsuarioPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
