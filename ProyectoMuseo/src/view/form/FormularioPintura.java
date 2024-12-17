package view.form;

import java.awt.*;
import javax.swing.*;
import models.Pintura;
import service.*;
import view.panel.pinturaPanel.GestionPinturasPanel;

public abstract class FormularioPintura extends JDialog {
    private Pintura pintura;

    /**
     * Constructor principal para crear una nueva pintura.
     * 
     * @param parent        El JFrame principal desde el que se llama el formulario.
     * @param dbManager    El objeto que maneja las operaciones con la base de datos.
     * @param gestionPanel El panel de gestión de pinturas desde el que se accede al formulario.
     */
    public FormularioPintura(JFrame parent, DatabaseManager dbManager, GestionPinturasPanel gestionPanel) {
        super(parent, "Formulario de Pintura", true);
        inicializarComponentes();
    }

    /**
     * Constructor sobrecargado para editar una pintura existente.
     * 
     * @param parent        El JFrame principal desde el que se llama el formulario.
     * @param dbManager    El objeto que maneja las operaciones con la base de datos.
     * @param gestionPanel El panel de gestión de pinturas desde el que se accede al formulario.
     * @param pintura      La pintura que se desea editar.
     */
    public FormularioPintura(JFrame parent, DatabaseManager dbManager, GestionPinturasPanel gestionPanel, Pintura pintura) {
        super(parent, "Editar Pintura", true);
        this.pintura = pintura;
        inicializarComponentes();
        cargarDatosPintura();
    }

    /**
     * Método para inicializar los componentes del formulario, como los campos de texto y botones.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getParent());
    }

    /**
     * Método para cargar los datos de una pintura existente en los campos del formulario
     * si la pintura no es nula.
     */
    private void cargarDatosPintura() {
        if (pintura != null) {
            // Cargar datos de la pintura en los campos del formulario
        }
    }

    /**
     * Método que obtiene el código de barras ingresado por el usuario.
     * 
     * @return El código de barras ingresado.
     * @throws UnsupportedOperationException Si el método no está implementado.
     */
    public String getCodigoBarrasIngresado() {
        throw new UnsupportedOperationException("Unimplemented method 'getCodigoBarrasIngresado'");
    }
}
