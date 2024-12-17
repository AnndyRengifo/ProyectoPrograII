/**
 * Utility class for creating and styling buttons in the application.
 * Provides methods for creating buttons with customizable text, icons, and actions.
 * Includes support for rounded borders and default icons.
 */
package utils;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class BotonUtil {

    /**
     * Path to the default icon used when no icon is provided or the specified icon is not found.
     */
    private static final String DEFAULT_ICON_PATH = "resources/icons/default-icon.png";

    /**
     * Creates a button with specified text, icon, and action listener.
     *
     * @param texto   the text to display on the button
     * @param iconPath the path to the button's icon, relative to the "resources/icons" directory
     * @param accion  the action listener to attach to the button
     * @return a configured JButton instance
     */
    public static JButton crearBoton(String texto, String iconPath, ActionListener accion) {
        JButton boton = new JButton(texto);

        // Set font and colors using EstiloUtil
        boton.setFont(EstiloUtil.FUENTE_BOTON);
        boton.setBackground(EstiloUtil.COLOR_FONDO_CLARO);
        boton.setForeground(EstiloUtil.COLOR_TEXTO);

        boton.setFocusPainted(false);
        boton.setBorder(new CompoundBorder(new RoundedBorder(10),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon icon = obtenerIcono(iconPath);
        if (icon != null) {
            boton.setIcon(icon);
            boton.setHorizontalTextPosition(SwingConstants.RIGHT);
            boton.setIconTextGap(10);
        }

        boton.addActionListener(accion);
        return boton;
    }

    /**
     * Loads an icon from the specified file path.
     *
     * @param filePath the path to the icon file
     * @return an ImageIcon instance or null if the icon could not be loaded
     */
    private static ImageIcon obtenerIcono(String filePath) {
        try {
            File iconFile = new File(filePath);
            if (!iconFile.isAbsolute()) {
                iconFile = new File("resources/icons/" + filePath);
            }

            if (iconFile.exists()) {
                Image img = ImageIO.read(iconFile).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                System.err.println("Icon not found: " + filePath);
                return obtenerIconoPredeterminado();
            }
        } catch (IOException e) {
            System.err.println("Error loading icon: " + filePath + " - " + e.getMessage());
            return obtenerIconoPredeterminado();
        }
    }

    /**
     * Loads the default icon when no icon is provided or an error occurs.
     *
     * @return an ImageIcon instance or null if the default icon could not be loaded
     */
    private static ImageIcon obtenerIconoPredeterminado() {
        try {
            File defaultIconFile = new File(DEFAULT_ICON_PATH);
            if (defaultIconFile.exists()) {
                Image img = ImageIO.read(defaultIconFile).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (IOException e) {
            System.err.println("Error loading default icon: " + e.getMessage());
        }
        return null; 
    }

    /**
     * Inner class to create rounded borders for buttons.
     */
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        /**
         * Constructor for RoundedBorder.
         *
         * @param radius the radius of the rounded corners
         */
        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 10, 10, 10);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = 5;
            insets.top = insets.bottom = 5;
            return insets;
        }
    }
}
