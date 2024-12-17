package view.panel.pinturaPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Pintura;
import service.DatabaseManager;
import utils.*;
import view.addpanel.AddPinturaPanel;
import view.admin.AdminPanel;

public class GestionPinturasPanel extends JPanel {

    private DatabaseManager dbManager;
    private JTable pinturasTable;
    private DefaultTableModel tableModel;
    private JLabel imagePreview;
    private JTextArea detallesArea;
    private JButton agregarButton, modificarButton, eliminarButton, volverButton;
    private JFrame gestionFrame;

    /**
     * Constructor que inicializa el panel de gestión de pinturas. Configura la interfaz
     * de usuario, los botones, la tabla de pinturas y carga las pinturas desde la base de datos.
     * 
     * @param dbManager El manejador de base de datos para obtener las pinturas.
     * @param parentFrame El JFrame principal al que se le añadirán los paneles.
     * @param currentFrame El JFrame actual que se está utilizando en la vista.
     * @throws IllegalArgumentException Si el objeto DatabaseManager es nulo.
     */
    public GestionPinturasPanel(DatabaseManager dbManager, JFrame parentFrame, JFrame currentFrame) {
        if (dbManager == null) {
            throw new IllegalArgumentException("El objeto DatabaseManager no puede ser nulo");
        }
        this.dbManager = dbManager;
        setLayout(new BorderLayout());
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        initializeUI(parentFrame, currentFrame);
        loadPinturas();
    }

    /**
     * Método que inicializa los componentes gráficos de la interfaz de usuario, como los botones,
     * la tabla de pinturas, el área de detalles y los paneles correspondientes.
     * 
     * @param parent El JFrame principal al que se le añadirán los paneles.
     * @param currentFrame El JFrame actual que se está utilizando en la vista.
     */
    private void initializeUI(JFrame parent, JFrame currentFrame) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        agregarButton = BotonUtil.crearBoton("Agregar", "addpintura.png", _ -> abrirFormularioAgregar(parent));
        modificarButton = BotonUtil.crearBoton("Modificar", "modpintura.png", _ -> abrirFormularioModificar(parent));
        eliminarButton = BotonUtil.crearBoton("Eliminar", "trashpintura.png", _ -> eliminarPinturaSeleccionada());
        volverButton = BotonUtil.crearBoton("Volver", "back.png", _ -> volver(currentFrame));

        volverButton.setBackground(EstiloUtil.COLOR_VOLVER);
        volverButton.setForeground(EstiloUtil.COLOR_TEXTO);

        agregarButton.setFont(EstiloUtil.FUENTE_BOTON);
        modificarButton.setFont(EstiloUtil.FUENTE_BOTON);
        eliminarButton.setFont(EstiloUtil.FUENTE_BOTON);
        volverButton.setFont(EstiloUtil.FUENTE_BOTON);

        topPanel.add(agregarButton);
        topPanel.add(modificarButton);
        topPanel.add(eliminarButton);
        topPanel.add(volverButton);

        tableModel = new DefaultTableModel(new Object[]{"Codigo Barras", "Título"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        pinturasTable = new JTable(tableModel);

        pinturasTable.setFont(EstiloUtil.FUENTE_CAMPO_TEXTO);
        pinturasTable.setRowHeight(25);
        pinturasTable.setSelectionBackground(EstiloUtil.COLOR_SELECCIONAR);
        pinturasTable.setSelectionForeground(EstiloUtil.COLOR_FONDO_SELECCIONAR);
        pinturasTable.getTableHeader().setFont(EstiloUtil.FUENTE_TABLA);
        pinturasTable.getSelectionModel().addListSelectionListener(_ -> mostrarDetallesPinturaSeleccionada());

        JScrollPane tableScrollPane = new JScrollPane(pinturasTable);

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        imagePreview = new JLabel("Previsualización de la Imagen", JLabel.CENTER);
        imagePreview.setPreferredSize(new Dimension(350, 350));
        imagePreview.setFont(EstiloUtil.FUENTE_CAMPO_TEXTO);
        imagePreview.setForeground(EstiloUtil.COLOR_TEXTO);

        detallesArea = new JTextArea(5, 20);
        detallesArea.setEditable(false);
        detallesArea.setLineWrap(true);
        detallesArea.setWrapStyleWord(true);
        detallesArea.setFont(EstiloUtil.FUENTE_CAMPO_TEXTO);
        detallesArea.setForeground(EstiloUtil.COLOR_TEXTO);
        detallesArea.setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        detailsPanel.add(imagePreview, BorderLayout.NORTH);
        detailsPanel.add(new JScrollPane(detallesArea), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.EAST);
    }

    /**
     * Método para cargar las pinturas desde la base de datos y mostrar sus detalles en la tabla.
     * Se verifica la existencia de la imagen asociada con cada pintura.
     */
    private void loadPinturas() {
        if (dbManager == null) {
            JOptionPane.showMessageDialog(this, "El manejador de base de datos no está inicializado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setRowCount(0);
        List<Pintura> pinturas = dbManager.obtenerTodasLasPinturas();
        for (Pintura pintura : pinturas) {
            File imageFile = obtenerArchivoImagen(pintura.getCodigoBarras());
            if (imageFile.exists()) {
                tableModel.addRow(new Object[]{pintura.getCodigoBarras(), pintura.getTitulo()});
            } else {
                tableModel.addRow(new Object[]{pintura.getCodigoBarras(), pintura.getTitulo() + " (Imagen no disponible)"});
            }
        }
    }

    /**
     * Método para mostrar los detalles de la pintura seleccionada en la tabla.
     * Muestra la información de autor, año, descripción, ubicación y la imagen asociada a la pintura.
     */
    private void mostrarDetallesPinturaSeleccionada() {
        int selectedRow = pinturasTable.getSelectedRow();
        if (selectedRow == -1) {
            detallesArea.setText("");
            imagePreview.setIcon(null);
            imagePreview.setText("Previsualización de la Imagen");
            return;
        }

        String codigoBarrasSeleccionado = (String) tableModel.getValueAt(selectedRow, 0);
        Pintura pintura = DatabaseManager.obtenerPinturaPorCodigoBarras(codigoBarrasSeleccionado);

        if (pintura != null) {
            detallesArea.setText(
                "Autor: " + pintura.getAutor() + "\n" +
                "Año: " + pintura.getAnio() + "\n" +
                "Descripción: " + pintura.getDescripcion() + "\n" +
                "Ubicación: " + pintura.getUbicacion());

            mostrarImagen(pintura.getCodigoBarras(), imagePreview);
        }
    }

    /**
     * Método para mostrar la imagen de una pintura en el JLabel de previsualización.
     * Si no se encuentra la imagen, muestra un mensaje de error.
     * 
     * @param codigoBarras El código de barras de la pintura cuya imagen se debe mostrar.
     * @param label El JLabel donde se mostrará la imagen.
     */
    private void mostrarImagen(String codigoBarras, JLabel label) {
        File imageFile = obtenerArchivoImagen(codigoBarras);

        if (!imageFile.exists()) {
            label.setIcon(null);
            label.setText("Imagen no encontrada");
            return;
        }

        try {
            BufferedImage img = ImageIO.read(imageFile);
            label.setIcon(new ImageIcon(img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH)));
            label.setText("");
        } catch (IOException e) {
            label.setIcon(null);
            label.setText("Error al cargar imagen");
        }
    }

    /**
     * Método para obtener el archivo de imagen correspondiente a una pintura, dada su
     * código de barras. El archivo se busca en la carpeta "resources/paintings".
     * 
     * @param codigoBarras El código de barras de la pintura.
     * @return El archivo de imagen correspondiente a la pintura.
     */
    private File obtenerArchivoImagen(String codigoBarras) {
        String ruta = "resources/paintings/" + codigoBarras + ".jpg";
        return new File(ruta);
    }

    /**
     * Método que abre el formulario para agregar una nueva pintura. Carga el panel de 
     * formulario de agregar pintura y lo muestra en el JFrame principal.
     * 
     * @param parentFrame El JFrame principal donde se añadirá el panel.
     */
    private void abrirFormularioAgregar(JFrame parentFrame) {
        AddPinturaPanel addPinturaPanel = new AddPinturaPanel(parentFrame);
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(addPinturaPanel, BorderLayout.CENTER);
        parentFrame.revalidate();
        parentFrame.repaint();
    }


    /**
     * Método que abre el formulario para modificar una pintura existente. Si no se selecciona
     * una pintura, muestra un mensaje de error. De lo contrario, abre el formulario de modificación
     * para la pintura seleccionada.
     * 
     * @param parent El JFrame principal desde donde se abre el formulario de modificación.
     */
    private void abrirFormularioModificar(JFrame parent) {
        int selectedRow = pinturasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una pintura para modificar.");
            return;
        }

        String codigoBarrasSeleccionado = (String) tableModel.getValueAt(selectedRow, 0);
        Pintura pintura = DatabaseManager.obtenerPinturaPorCodigoBarras(codigoBarrasSeleccionado);

        if (pintura != null) {
            ModificarPinturaPanel modificarPinturaPanel = new ModificarPinturaPanel(gestionFrame, pintura, dbManager);
            JFrame modificarPinturaFrame = new JFrame("Modificar Pintura");

            modificarPinturaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            modificarPinturaFrame.setSize(600, 400);
            modificarPinturaFrame.setLocationRelativeTo(parent);
            modificarPinturaFrame.add(modificarPinturaPanel);
            modificarPinturaFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la pintura seleccionada.");
        }
    }

    /**
     * Método que elimina la pintura seleccionada de la tabla y la base de datos. Si no se selecciona
     * ninguna pintura, muestra un mensaje de error. Si la eliminación es exitosa, recarga la lista de pinturas.
     */
    private void eliminarPinturaSeleccionada() {
        int selectedRow = pinturasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una pintura para eliminar.");
            return;
        }

        String codigoBarrasSeleccionado = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta pintura?", "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = dbManager.eliminarPintura(codigoBarrasSeleccionado);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Pintura eliminada correctamente.");
                loadPinturas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la pintura.");
            }
        }
    }

    /**
     * Método que vuelve al panel de administración, eliminando el panel actual y añadiendo el panel `AdminPanel`.
     * 
     * @param currentFrame El JFrame actual desde el cual se regresa al panel de administración.
     */
    private void volver(JFrame currentFrame) {
        currentFrame.getContentPane().removeAll();
        currentFrame.add(new AdminPanel(currentFrame));
        currentFrame.revalidate();
        currentFrame.repaint();
    }
}
