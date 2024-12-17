package view.panel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Pintura;
import service.DatabaseManager;
import utils.BotonUtil;
import utils.EstiloUtil;

/**
 * Clase PanelVisitante que representa la interfaz gráfica para los visitantes del museo.
 * Permite escanear códigos de barras de pinturas y obtener información detallada sobre ellas.
 * Además, muestra la imagen asociada si está disponible.
 */
public class PanelVisitante extends JPanel {
    private JTextArea resultadoArea; // Área de texto para mostrar detalles de la pintura
    private JLabel imagenPinturaLabel; // Etiqueta para mostrar la imagen de la pintura

    /**
     * Constructor de la clase PanelVisitante.
     * Configura la disposición y los componentes de la interfaz gráfica.
     * 
     * @param parentFrame El marco principal al que pertenece este panel.
     */
    public PanelVisitante(JFrame parentFrame) {
        setLayout(new BorderLayout(10, 10)); // Configuración del diseño con espaciado
        setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        // Configuración del panel superior con título
        JPanel topPanel = EstiloUtil.crearPanelTransparente();
        topPanel.add(EstiloUtil.crearTitulo("Escanee la Pintura"));
        add(topPanel, BorderLayout.NORTH);

        // Panel principal para detalles y la imagen
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        // Panel izquierdo: Detalles
        JPanel detallesPanel = EstiloUtil.crearPanelTransparente();
        detallesPanel.setLayout(new BorderLayout());
        detallesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        detallesPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        detallesPanel.setPreferredSize(new Dimension(300, getHeight()));

        // Campo de entrada para el código de barras
        JPanel escaneoPanel = EstiloUtil.crearPanelTransparente();
        escaneoPanel.setLayout(new BoxLayout(escaneoPanel, BoxLayout.Y_AXIS));
        escaneoPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);

        JTextField codigoInput = new JTextField();
        codigoInput.setFont(new Font("Arial", Font.PLAIN, 18));
        codigoInput.setPreferredSize(new Dimension(300, 40));
        codigoInput.setMaximumSize(codigoInput.getPreferredSize());
        escaneoPanel.add(EstiloUtil.crearTextoSecundario("Escanee la Pintura por favor:"));
        escaneoPanel.add(Box.createVerticalStrut(10));
        escaneoPanel.add(codigoInput);
        escaneoPanel.add(Box.createVerticalStrut(10));

        // Botón para buscar la pintura
        JButton buscarButton = BotonUtil.crearBoton("Buscar Pintura", "buscarpai.png", _ -> {
            String codigoBarras = codigoInput.getText();
            if (codigoBarras.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un código de barras.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar pintura en la base de datos
            var pintura = DatabaseManager.obtenerPinturaPorCodigoBarras(codigoBarras);
            if (pintura != null) {
                mostrarDetallesPintura(pintura);
            } else {
                resultadoArea.setText("No se encontró ninguna pintura con el código de barras ingresado.");
                imagenPinturaLabel.setIcon(null); // Limpia la imagen
            }
        });

        escaneoPanel.add(buscarButton);
        detallesPanel.add(escaneoPanel, BorderLayout.NORTH);

        // Área de texto para resultados
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        resultadoArea.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        detallesPanel.add(scrollPane, BorderLayout.CENTER);

        panelPrincipal.add(detallesPanel, BorderLayout.WEST);

        // Panel derecho: Imagen
        JPanel imagenPanel = new JPanel();
        imagenPanel.setLayout(new BorderLayout());
        imagenPanel.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        imagenPinturaLabel = new JLabel("", SwingConstants.CENTER);
        imagenPanel.add(imagenPinturaLabel, BorderLayout.CENTER);
        panelPrincipal.add(imagenPanel, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);

        // Agregar un KeyListener al campo de texto para detectar la entrada del código de barras
        codigoInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String codigoBarras = codigoInput.getText();
                if (codigoBarras.length() == 13) {
                    Pintura pintura = DatabaseManager.obtenerPinturaPorCodigoBarras(codigoBarras);
                    if (pintura != null) {
                        mostrarDetallesPintura(pintura);
                    } else {
                        resultadoArea.setText("No se encontró ninguna pintura con el código de barras ingresado.");
                        imagenPinturaLabel.setIcon(null);
                    }
                }
            }
        });
    }

    /**
     * Muestra los detalles de la pintura en el área de texto y carga la imagen correspondiente.
     * 
     * @param pintura Objeto Pintura con la información a mostrar.
     */
    private void mostrarDetallesPintura(Pintura pintura) {
        String detallesTexto = 
                  "Título: " + pintura.getTitulo() + "\n" 
                + "Autor: " + pintura.getAutor() + "\n"
                + "Año: " + pintura.getAnio() + "\n"
                + "Descripción: " + pintura.getDescripcion() + "\n"
                + "Ubicación: " + pintura.getUbicacion() + "\n"
                + "Código de Barras: " + pintura.getCodigoBarras();
        resultadoArea.setText(detallesTexto);

        String imagenPath = "resources/paintings/" + pintura.getCodigoBarras() + ".jpg";
        File imagenFile = new File(imagenPath);
        if (imagenFile.exists()) {
            try {
                Image img = ImageIO.read(imagenFile);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(300, 400, Image.SCALE_SMOOTH));
                imagenPinturaLabel.setIcon(icon);
            } catch (IOException e) {}
        } else {
            imagenPinturaLabel.setIcon(null);
        }
    }
}
