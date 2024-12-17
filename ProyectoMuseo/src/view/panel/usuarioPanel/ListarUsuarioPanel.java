package view.panel.usuarioPanel;

import controller.*;
import models.*;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.*;

public class ListarUsuarioPanel extends JPanel {

    private final JTable tablaUsuarios;
    private final UsuarioManager usuarioManager;

    /**
     * Constructor que inicializa el panel y carga los datos
     * @param parentFrame
     */
    public ListarUsuarioPanel(JFrame parentFrame) {
        usuarioManager = new UsuarioManager();  

        setLayout(new BorderLayout());

        String[] columnas = {"ID", "Nombre", "Rol"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        tablaUsuarios = new JTable(modeloTabla);

        tablaUsuarios.setFont(EstiloUtil.FUENTE_CAMPO_TEXTO);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.setSelectionBackground(EstiloUtil.COLOR_SELECCIONAR);
        tablaUsuarios.setSelectionForeground(EstiloUtil.COLOR_FONDO_SELECCIONAR);
        tablaUsuarios.getTableHeader().setFont(EstiloUtil.FUENTE_TABLA);

        cargarUsuarios();

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        JButton volverButton = BotonUtil.crearBoton("Volver", "back.png", _ -> volverAlPanel(parentFrame));
        volverButton.setBackground(EstiloUtil.COLOR_VOLVER);
        volverButton.setForeground(EstiloUtil.COLOR_TEXTO);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        panelBotones.add(volverButton);
        add(panelBotones, BorderLayout.SOUTH);

        volverButton.setFocusPainted(false);
        volverButton.setPreferredSize(new Dimension(120, 35));
    }

    /**
     * Metodo que carga los usuarios desde la base de datos y los muestra en la tabla
     */
    private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioManager.obtenerUsuarios(); 
        DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
        modelo.setRowCount(0); 

        for (Usuario usuario : usuarios) {
            Object[] fila = {usuario.getId(), usuario.getNombre(), usuario.getRol()};
            modelo.addRow(fila);
        }
    }

    /**
     * Metodo que vuelve al panel anterior de gestion de usuarios
     * @param parentFrame
     */
    private void volverAlPanel(JFrame parentFrame) {
        parentFrame.setContentPane(new GestionUsuarioPanel(parentFrame)); // Cambiar al panel de gestión de usuarios
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
