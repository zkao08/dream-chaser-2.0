package Frontend;

import java.awt.*;
import javax.swing.*;

/**
 * <h1>GradientPanel Class</h1>
 * The GradientPanel class extends JPanel to create a custom panel with a gradient background.
 * It allows specifying two colors to generate a vertical gradient, enhancing the visual aesthetics of the UI.
 *
 * <p>Usage:
 * This class is used to create panels with a gradient background, by providing start and end colors.
 * It can be used in various parts of the application where a visually appealing background is required.</p>
 *
 * @author Anointiyae Beasley
 * @version 2.0
 * @since 11/20/2024
 * @package Frontend
 */

public class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;

    /**
     * Constructor for GradientPanel.
     * Initializes the panel with the specified start and end colors for the gradient.
     *
     * @param startColor The starting color of the gradient.
     * @param endColor   The ending color of the gradient.
     */
    public GradientPanel(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    /**
     * Paints the panel with a gradient background.
     * This method is called automatically when the panel is rendered.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Create a gradient from startColor to endColor
        GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }
}