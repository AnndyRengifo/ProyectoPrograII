package view.panel.pinturaPanel;

import java.awt.*;
import javax.swing.*;
import models.*;
import service.DatabaseManager;
import utils.BotonUtil;
import utils.EstiloUtil;

/**
 * Clase que representa un panel para modificar los detalles de una pintura en el sistema.
 * Este panel permite al usuario editar el título, autor, año y código de barras de una pintura existente.
 *
 * @package view.panel
 */
public class ModificarPinturaPanel extends JPanel {

    private final JFrame parentFrame; // Marco padre que contiene este panel
    private final Pintura pintura; // Objeto Pintura que se está modification
    private final JTextField tituloField;
    private final JTextField autorField, anioField, codigoBarrasField; // Campos de texto para editar los atributos de la pintura
    private final DatabaseManager dbManager; // Gestor de base de datos para realizar operaciones

    /**
     * Constructor que inicializa el panel y configura la interfaz gráfica.
     *
     * @param parentFrame El JFrame que contiene este panel.
     * @param pintura La instancia de la pintura que se va a modificar.
     * @param dbManager La instancia del DatabaseManager que maneja las interacciones con la base de datos.
     */
    public ModificarPinturaPanel(JFrame parentFrame, Pintura pintura, DatabaseManager dbManager) {
        this.parentFrame = parentFrame;
        this.pintura = pintura;
        this.dbManager = new DatabaseManager();

        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel topPanel = EstiloUtil.crearPanelTransparente();
        topPanel.add(EstiloUtil.crearTitulo("Modificar Pintura"));
        add(topPanel, BorderLayout.NORTH);

        // Panel central con los campos de entrada
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos para editar la pintura
        formPanel.add(EstiloUtil.crearTextoSecundario("Título:"));
        tituloField = new JTextField(pintura.getTitulo());
        formPanel.add(tituloField);

        formPanel.add(EstiloUtil.crearTextoSecundario("Autor:"));
        autorField = new JTextField(pintura.getAutor());
        formPanel.add(autorField);

        formPanel.add(EstiloUtil.crearTextoSecundario("Año:"));
        anioField = new JTextField(String.valueOf(pintura.getAnio()));
        formPanel.add(anioField);

        formPanel.add(EstiloUtil.crearTextoSecundario("Código de Barras:"));
        codigoBarrasField = new JTextField(pintura.getCodigoBarras());
        formPanel.add(codigoBarrasField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton guardarButton = BotonUtil.crearBoton("Guardar Cambios", "guardar.png", _ -> guardarCambios());
        buttonPanel.add(guardarButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Método que guarda los cambios realizados en los campos de texto,
     * valida la entrada del usuario y actualiza la pintura en la base de datos.
     */
    private void guardarCambios() {
        String nuevoTitulo = tituloField.getText().trim();
        String nuevoAutor = autorField.getText().trim();
        String nuevoAnioStr = anioField.getText().trim();
        String nuevoCodigoBarras = codigoBarrasField.getText().trim();
    
        if (nuevoTitulo.isEmpty() || nuevoAutor.isEmpty() || nuevoAnioStr.isEmpty() || nuevoCodigoBarras.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            int nuevoAnio = Integer.parseInt(nuevoAnioStr);
            if (nuevoAnio <= 0) {
                throw new NumberFormatException("El año debe ser positivo.");
            }
    
            if (!nuevoCodigoBarras.equals(pintura.getCodigoBarras()) && DatabaseManager.obtenerPinturaPorCodigoBarras(nuevoCodigoBarras) != null) {
                JOptionPane.showMessageDialog(parentFrame, "El código de barras ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            boolean actualizado = dbManager.actualizarPintura(
                pintura.getCodigoBarras(),
                nuevoCodigoBarras,
                nuevoTitulo,
                nuevoAutor,
                nuevoAnio,
                pintura.getDescripcion(),
                pintura.getUbicacion()
            );
    
            if (actualizado) {
                JOptionPane.showMessageDialog(parentFrame, "Pintura actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                JFrame modificarPinturaFrame = (JFrame) SwingUtilities.getWindowAncestor(this);  // Obtener la ventana actual
                modificarPinturaFrame.dispose();  // Cerrar solo la ventana de modificación
            } else {
                JOptionPane.showMessageDialog(parentFrame, "No se pudo actualizar la pintura.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "El año debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
