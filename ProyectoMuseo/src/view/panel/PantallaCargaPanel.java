package view.panel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import utils.EstiloUtil;

public class PantallaCargaPanel extends JPanel {

    private int angle = 0;
    private Timer timer;
    private int progress = 0;
    private static final int MAX_ANGLE = 1080;
    private static final int MAX_PROGRESS = 100;
    private Image logo;

    /**
     * Constructor que inicializa el panel de carga con un mensaje de bienvenida y configura
     * el fondo, el logo y la animación de carga.
     * 
     * @param mensajeBienvenida El mensaje de bienvenida que se mostrará en el panel.
     */
    public PantallaCargaPanel(String mensajeBienvenida) {
        setLayout(null);
        setBackground(Color.WHITE);

        try {
            logo = ImageIO.read(new File("resources/logos/ArtVisionLogo.png"));
        } catch (IOException e) {}
        startLoadingAnimation();
    }

    /**
     * Método que inicia la animación de carga. Este método crea un temporizador que actualiza
     * el ángulo de rotación para el círculo animado y el porcentaje de carga. El progreso se
     * incrementa a medida que avanza la animación.
     */
    private void startLoadingAnimation() {
        timer = new Timer(50, _ -> {
            angle += 20;
            if (angle >= MAX_ANGLE) {
                angle = MAX_ANGLE;
                timer.stop();
            }

            progress = (int) ((double) angle / MAX_ANGLE * MAX_PROGRESS);
            repaint();
        });
        timer.start();
    }

    /**
     * Método que detiene la animación de carga cuando se llama, deteniendo el temporizador
     * y finalizando el progreso de carga.
     */
    public void stopLoadingAnimation() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Método sobrecargado de `paintComponent` que se encarga de dibujar el contenido del panel:
     * 1. Logo centrado en la pantalla.
     * 2. Un círculo animado con el progreso de carga.
     * 3. El porcentaje de carga dentro del círculo.
     * 
     * @param g El objeto `Graphics` que se utiliza para dibujar en el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int logoWidth = 360;
        int logoHeight = 360;

        int x = (getWidth() - logoWidth) / 2;
        int y = (getHeight() - logoHeight) / 2 - 30;

        g2d.drawImage(logo.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH), x, y, this);

        int circleRadius = 15;
        int centerX = getWidth() / 2;
        int centerY = getHeight() - 25;

        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(EstiloUtil.COLOR_TITULO);

        g2d.drawArc(centerX - circleRadius, centerY - circleRadius, circleRadius * 2, circleRadius * 2, angle % 360, 270);

        String percentageText = progress + "%";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(percentageText);
        int textHeight = fm.getHeight();

        g2d.setColor(Color.BLUE);
        g2d.drawString(percentageText, centerX - textWidth / 2, centerY + textHeight / 3);
    }
}
