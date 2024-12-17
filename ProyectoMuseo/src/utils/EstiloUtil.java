package utils;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class EstiloUtil {

    // Colores predefidos
    public static final Color COLOR_FONDO_CLARO = new Color(245, 245, 245);
    public static final Color COLOR_TITULO = new Color(0, 102, 204);
    public static final Color COLOR_TEXTO = Color.BLACK;
    public static final Color COLOR_ERROR = new Color(255, 69, 58);
    public static final Color COLOR_EXITO = new Color(50, 205, 50);
    public static final Color COLOR_VOLVER = new Color(255, 102, 102);
    public static final Color COLOR_SELECCIONAR = new Color(84, 153, 199);
    public static final Color COLOR_FONDO_SELECCIONAR = Color.WHITE;

    // Fuentes predefinidas
    public static final Font FUENTE_TITULO = new Font("Serif", Font.BOLD, 40);
    public static final Font FUENTE_BOTON = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FUENTE_CAMPO_TEXTO = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FUENTE_TABLA = new Font("SansSerif", Font.BOLD, 15);
    

    /**
     * crear un borde vacio utilizado principalmenta par titulos
     * @return Borde vacio con espacio superor e inferior
     */
    public static Border bordeTitulo() {
        return BorderFactory.createEmptyBorder(10, 0, 10, 0);
    }

    /**
     * Crea un JLabel estilizado para ser utilizado como titulo principal
     * @param texto Texto del titulo
     * @return JLabel configurado
     */
    public static JLabel crearTitulo(String texto) {
        JLabel titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TITULO);
        titulo.setBorder(bordeTitulo());
        return titulo;
    }

    /**
     * Crear un JLabel estilizado para ser utilizado como titulo secundario
     * @param texto Texto del titulo secundario
     * @return JLabel configurado
     */
    public static JLabel crearTituloSecundario(String texto) {
        JLabel tituloSecundario = new JLabel(texto, SwingConstants.LEFT);
        tituloSecundario.setFont(new Font("SansSerif", Font.BOLD, 20));
        tituloSecundario.setForeground(COLOR_TEXTO);
        return tituloSecundario;
    }

    /**
     * Crear un JLabel estilizado para texto secundario
     * @param texto Texto a mostrar
     * @return JLabel configurado
     */
    public static JLabel crearTextoSecundario(String texto) {
        JLabel tituloSecundario = new JLabel(texto, SwingConstants.LEFT);
        tituloSecundario.setFont(new Font("SansSerif", Font.BOLD, 14));
        tituloSecundario.setForeground(COLOR_TEXTO);
        return tituloSecundario;
    }

    /**
     * Crea un JPanel transparente con fondo claro
     * @return JPanel transparente
     */
    public static JPanel crearPanelTransparente() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO_CLARO);
        return panel;
    }

    /**
     * Crea un JTextField estilizado para ingreso de texto
     * @return JTextField configurado
     */
    public static JTextField crearCampoTextoUsuario() {
        JTextField campoTexto = new JTextField(15);
        campoTexto.setFont(FUENTE_CAMPO_TEXTO);
        campoTexto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TITULO, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campoTexto.setPreferredSize(new Dimension(200, 30));
        return campoTexto;
    }
    
    /**
     * Crea un JPasswordField estilizado para ingreso de contraseñas
     * @return JPasswordField configurado
     */
    public static JPasswordField crearCampoTextoPassword() {
        JPasswordField campoPassword = new JPasswordField(15);
        campoPassword.setFont(FUENTE_CAMPO_TEXTO);
        campoPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TITULO, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campoPassword.setPreferredSize(new Dimension(200, 30));
        return campoPassword;
    }
    
    /**
     * Crea un JTextFiel estilizado para ingreso de texto
     * @return JTextFiel configurado
     */
    public static JTextField crearCampoTextoSecundario() {
        JTextField campoTexto = new JTextField(20);
        campoTexto.setFont(FUENTE_CAMPO_TEXTO);
        campoTexto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TEXTO, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campoTexto;
    }

    /**
     * Crea un JPasswordField estilizado para la contraseña secundaria 
     * @return JPasswordField configurado
     */
    public static JPasswordField crearCampoTextoPassSecundario() {
        JPasswordField campoPassword = new JPasswordField(20);
        campoPassword.setFont(FUENTE_CAMPO_TEXTO);
        campoPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_TEXTO, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campoPassword;
    }

    /**
     * Convierte un Color a su representación hexadecimal.
     * @param color Color a convertir.
     * @return Cadena en formato hexadecimal.
     */
    public static String hexColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}