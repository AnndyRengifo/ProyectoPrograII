package view.addpanel;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import service.DatabaseManager;
import utils.*;
import view.panel.pinturas.GestionPinturasPanel;

public class AddPinturaPanel extends JPanel {
    private final JTextField tfTitulo, tfAutor, tfAnio, tfDescripcion, tfUbicacion, tfCodigoBarras;
    private final JLabel lblPrevisualizacion;
    private final JButton agregarButton, volverButton;
    private final DatabaseManager dbManager;
    private final JFrame parentFrame;

    /**
     * Constructor que inicializa el panel y configura el formulario de agregar pintura
     * @param parentFrame
     */
    public AddPinturaPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        dbManager = DatabaseManager.getInstance();

        JPanel topPanel = EstiloUtil.crearPanelTransparente();
        topPanel.add(EstiloUtil.crearTitulo("Agregar Nueva Pintura"));
        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = EstiloUtil.crearPanelTransparente();
        formPanel.setLayout(new GridLayout(7, 2, 20, 20));

        formPanel.add(EstiloUtil.crearTextoSecundario("Código de Barras:"));
        tfCodigoBarras = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfCodigoBarras);

        formPanel.add(EstiloUtil.crearTextoSecundario("Título:"));
        tfTitulo = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfTitulo);

        formPanel.add(EstiloUtil.crearTextoSecundario("Autor:"));
        tfAutor = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfAutor);

        formPanel.add(EstiloUtil.crearTextoSecundario("Año:"));
        tfAnio = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfAnio);

        formPanel.add(EstiloUtil.crearTextoSecundario("Descripción:"));
        tfDescripcion = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfDescripcion);

        formPanel.add(EstiloUtil.crearTextoSecundario("Ubicación:"));
        tfUbicacion = EstiloUtil.crearCampoTextoSecundario();
        formPanel.add(tfUbicacion);

        add(formPanel, BorderLayout.CENTER);

        lblPrevisualizacion = new JLabel("Previsualización de la Imagen", SwingConstants.CENTER);
        lblPrevisualizacion.setPreferredSize(new Dimension(400, 400));
        lblPrevisualizacion.setOpaque(true);
        lblPrevisualizacion.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        add(lblPrevisualizacion, BorderLayout.EAST);

        JPanel buttonPanel = EstiloUtil.crearPanelTransparente();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        agregarButton = BotonUtil.crearBoton("Agregar Pintura", "addpintura.png", _ -> agregarPintura());
        volverButton = BotonUtil.crearBoton("Volver", "back.png", _ -> volverAGestionPinturas());
        volverButton.setBackground(EstiloUtil.COLOR_VOLVER); 
        volverButton.setForeground(EstiloUtil.COLOR_TEXTO);

        buttonPanel.add(agregarButton);
        buttonPanel.add(volverButton);

        add(buttonPanel, BorderLayout.SOUTH);

        tfCodigoBarras.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mostrarPrevisualizacionImagen();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mostrarPrevisualizacionImagen();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mostrarPrevisualizacionImagen();
            }
        });
    }

    /**
     * Metodo para mostrar la previsualizacion de la imagen de la pintura
     */
    private void mostrarPrevisualizacionImagen() {
        String codigoBarras = tfCodigoBarras.getText();
        if (codigoBarras.length() == 13) {
            File imageFile = new File("resources/paintings/" + codigoBarras + ".jpg");
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(imageFile.getAbsolutePath()).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
                lblPrevisualizacion.setIcon(icon);
                lblPrevisualizacion.setText("");
            } else {
                lblPrevisualizacion.setText("Imagen no encontrada");
                lblPrevisualizacion.setIcon(null);
            }
        } else {
            lblPrevisualizacion.setText("Previsualización de la Imagen");
            lblPrevisualizacion.setIcon(null);
        }
    }

    /**
     * Metodo para agregar una nueva pintura a la base de datos
     */
    private void agregarPintura() {
        try {
            String titulo = tfTitulo.getText();
            String autor = tfAutor.getText();
            int anio = Integer.parseInt(tfAnio.getText());
            String descripcion = tfDescripcion.getText();
            String ubicacion = tfUbicacion.getText();
            String codigoBarras = tfCodigoBarras.getText();

            if (titulo.isEmpty() || autor.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty() || codigoBarras.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            File imageFile = new File("resources/paintings/" + codigoBarras + ".jpg");
            if (!imageFile.exists()) {
                throw new IllegalArgumentException("La imagen no existe.");
            }

            dbManager.insertarPintura(titulo, autor, anio, descripcion, codigoBarras, ubicacion, imageFile.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Pintura agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } catch (HeadlessException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para limpiar los campos del formulario despues de agregar una pintura
     */
    private void limpiarCampos() {
        tfTitulo.setText("");
        tfAutor.setText("");
        tfAnio.setText("");
        tfDescripcion.setText("");
        tfUbicacion.setText("");
        tfCodigoBarras.setText("");
        lblPrevisualizacion.setText("Previsualización de la Imagen");
        lblPrevisualizacion.setIcon(null);
    }

    /**
     * Metodo para volver al panel de gestion de pinturas
     */
    private void volverAGestionPinturas() {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new GestionPinturasPanel(dbManager, parentFrame, parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
