package view.panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import service.DatabaseManager;
import utils.BotonUtil;
import utils.EstiloUtil;
import view.admin.AdminPanel;

public class IngresoAdminPanel extends JPanel {
    private final JFrame parentFrame;

    /**
     * Constructor que inicializa el panel de login de administrador, configurando la interfaz gráfica
     * y los elementos necesarios, como los campos de texto y los botones.
     * 
     * @param parentFrame El JFrame que contiene este panel.
     */
    public IngresoAdminPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    
        setLayout(new BorderLayout(10, 10));
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);
    
        JLabel titulo = EstiloUtil.crearTitulo("Panel de Administrador");
        add(titulo, BorderLayout.NORTH);
    
        JPanel centroPanel = EstiloUtil.crearPanelTransparente();
        centroPanel.setLayout(new GridLayout(2, 2, 10, 10));
        centroPanel.setBorder(new EmptyBorder(130, 200, 200, 250));

        JLabel usuarioLabel = EstiloUtil.crearTituloSecundario("Usuario:");
        JTextField usernameField = EstiloUtil.crearCampoTextoUsuario();
    
        JLabel passwordLabel = EstiloUtil.crearTituloSecundario("Contraseña:");
        JPasswordField passwordField = EstiloUtil.crearCampoTextoPassword();
    
        centroPanel.add(usuarioLabel);
        centroPanel.add(usernameField);
        centroPanel.add(passwordLabel);
        centroPanel.add(passwordField);
    
        add(centroPanel, BorderLayout.CENTER);
    
        JPanel botonesPanel = EstiloUtil.crearPanelTransparente();
        botonesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    
        JButton loginButton = BotonUtil.crearBoton("Iniciar Sesión", "ingresar.png", _ -> iniciarSesion(usernameField, passwordField));
        JButton scanButton = BotonUtil.crearBoton("Escanear Credencial", "credencial.png", _ -> mostrarVentanaEscanearCredencial());
    
        JPanel volverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton volverButton = BotonUtil.crearBoton("Volver", "back.png", _ -> volverAGInicioPanel());
        volverButton.setBackground(EstiloUtil.COLOR_VOLVER);
        volverButton.setForeground(EstiloUtil.COLOR_TEXTO);
        volverButton.setPreferredSize(new Dimension(120, 40));
        volverPanel.add(volverButton);
    
        botonesPanel.add(volverPanel);
        botonesPanel.add(loginButton);
        botonesPanel.add(scanButton);
        add(botonesPanel, BorderLayout.SOUTH);
    
        passwordField.addActionListener(_ -> iniciarSesion(usernameField, passwordField));
    }
    
    /**
     * Método que valida las credenciales de usuario y contraseña ingresadas. Si las credenciales son correctas,
     * muestra un mensaje de éxito y redirige al panel de administrador.
     * Si las credenciales son incorrectas, muestra un mensaje de error.
     * 
     * @param usernameField El campo de texto donde el usuario ingresa su nombre de usuario.
     * @param passwordField El campo de texto donde el usuario ingresa su contraseña.
     */
    private void iniciarSesion(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos.", "Error");
            return;
        }

        boolean authenticated = DatabaseManager.verificarCredenciales(username, password);
        if (authenticated) {
            mostrarMensaje("Inicio de sesión exitoso.", "Éxito");
            irPanelAdmin();
        } else {
            mostrarMensaje("Usuario o contraseña incorrectos.", "Error");
        }
    }

    /**
     * Método que muestra un mensaje en un cuadro de diálogo. El mensaje y el título del cuadro de diálogo
     * se definen según los parámetros, y el color de fondo se maneja de manera estática.
     * 
     * @param mensaje El mensaje que se mostrará en el cuadro de diálogo.
     * @param titulo El título del cuadro de diálogo.
     */
    private void mostrarMensaje(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            titulo,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Método que abre una ventana de diálogo para escanear o ingresar el ID de la credencial de un administrador.
     * Cuando el ID se ingresa correctamente, se verifica si es válido y se realiza el inicio de sesión.
     */
    private void mostrarVentanaEscanearCredencial() {
        JDialog dialog = new JDialog(parentFrame, "Escanear Credencial", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JLabel titulo = EstiloUtil.crearTituloSecundario("Escanee o ingrese el ID:");
        dialog.add(titulo, BorderLayout.NORTH);

        JPanel panelCentral = EstiloUtil.crearPanelTransparente();
        JTextField idField = EstiloUtil.crearCampoTextoUsuario();
        panelCentral.add(idField);
        dialog.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = EstiloUtil.crearPanelTransparente();
        JButton cancelButton = BotonUtil.crearBoton("Cancelar", "cancelar.png", _ -> dialog.dispose());
        panelInferior.add(cancelButton);
        dialog.add(panelInferior, BorderLayout.SOUTH);

        idField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verificarYAutenticarCredencial(idField, dialog);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verificarYAutenticarCredencial(idField, dialog);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verificarYAutenticarCredencial(idField, dialog);
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Método que verifica si el ID ingresado es válido. Si es así, autentica la credencial y redirige al panel de administrador.
     * Si el ID no es válido, muestra un mensaje de error.
     * 
     * @param idField El campo de texto donde se ingresa el ID de la credencial.
     * @param dialog La ventana de diálogo donde se ingresa el ID de la credencial.
     */
    private void verificarYAutenticarCredencial(JTextField idField, JDialog dialog) {
        String id = idField.getText();
        if (id.length() == 13) {
            boolean authenticated = DatabaseManager.verificarCredencialesPorId(id);
            if (authenticated) {
                mostrarMensaje("Inicio de sesión exitoso.", "Éxito");
                dialog.dispose();
                irPanelAdmin();
            } else {
                mostrarMensaje("ID de credencial incorrecto.", "Error");
                idField.setText("");
            }
        }
    }

    /**
     * Método que redirige al panel de administrador después de un inicio de sesión exitoso.
     */
    private void irPanelAdmin() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new AdminPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    /**
     * Método que redirige al panel de inicio después de hacer clic en el botón "Volver".
     */
    private void volverAGInicioPanel() {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new InicioPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
