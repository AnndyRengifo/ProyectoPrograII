package view.form;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Clase abstracta que representa un panel base para formularios.
 * Los formularios que extiendan esta clase deben definir los campos específicos para cada formulario.
 */
public abstract class FormularioPanel extends JPanel {
    public JFrame parentFrame;

    /**
     * Constructor para inicializar el panel con el marco padre y configurar su diseño.
     * 
     * @param parentFrame El JFrame principal desde el que se llama el panel.
     */
    public FormularioPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new GridLayout(8, 2));
    }

    /**
     * Método abstracto que debe ser implementado por las clases hijas para agregar los campos específicos del formulario.
     * Cada formulario debe definir sus propios campos, como los de texto, etiquetas, etc.
     */
    protected abstract void agregarCampos();

    /**
     * Método para agregar un botón común al formulario con un nombre y un ActionListener.
     * 
     * @param nombreBoton El nombre que tendrá el botón.
     * @param listener    El ActionListener que manejará el evento de clic del botón.
     */
    protected void agregarBotones(String nombreBoton, ActionListener listener) {
        JButton boton = new JButton(nombreBoton);
        boton.addActionListener(listener);
        add(boton);
    }

    /**
     * Método para agregar un botón "Volver" al formulario, con un ActionListener que manejará la acción de volver.
     * 
     * @param listener El ActionListener que manejará el evento de clic del botón "Volver".
     */
    protected void agregarBotonVolver(ActionListener listener) {
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(listener);
        add(volverButton);
    }

    /**
     * Método para cambiar el panel que se muestra en el JFrame principal.
     * Este método elimina el panel actual y agrega el nuevo panel.
     * 
     * @param nuevoPanel El nuevo JPanel que se desea mostrar.
     */
    protected void cambiarDePanel(JPanel nuevoPanel) {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(nuevoPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }


}
