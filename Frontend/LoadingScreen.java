package Frontend;

import java.awt.*;
import javax.swing.*;

/**
 * <h1>LoadingScreen Class</h1>
 * The LoadingScreen class creates a graphical user interface (GUI) for a loading screen in the application.
 * It displays a background image, a logo, a progress bar, and a loading message to the user while the application initializes.
 *
 * <p>Usage:
 * This class is intended to provide a visually appealing loading screen while backend processes,
 * such as resource initialization, are being performed. After loading is complete, the main application window is launched.</p>
 *
 * @author Mai-Lisa Atis
 * @version 2.0
 * @since 11/20/2024
 * @package Frontend
 */


/**
 * The LoadingScreen class extends JPanel and provides the components
 * and behavior for displaying the loading screen.
 */
public class LoadingScreen extends JPanel {
    private Image backgroundImage;

    /**
     * Constructor for LoadingScreen.
     * Initializes the loading screen by setting up the background image, logo, loading label, and progress bar.
     */
    public LoadingScreen() {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/Images/Background.png")).getImage();

        setLayout(new BorderLayout());

        // Load the image as an icon
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/Images/LogoTransparent.png"));
        JLabel imageLabel = new JLabel(loadingIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add a loading message
        JLabel loadingLabel = new JLabel("Loading, please wait...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Create a progress bar in indeterminate mode
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Indeterminate mode for loading animation

        // Add components to the panel
        add(imageLabel, BorderLayout.CENTER);
        add(loadingLabel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.SOUTH);
    }

    /**
     * Overridden paintComponent method.
     * Renders the background image to fill the panel when it is painted.
     *
     * @param g the Graphics object used to draw the component
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image to fill the panel
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * The main method for testing the LoadingScreen class.
     * Displays the loading screen and simulates a delay to represent the loading process.
     * After the delay, the loading screen is closed, and the main application window is launched.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Create and configure the main frame for the loading screen
        JFrame frame = new JFrame("Loading Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400); // Adjust size as needed
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Add the loading screen to the frame
        LoadingScreen loadingScreen = new LoadingScreen();
        frame.add(loadingScreen);

        frame.setVisible(true);

        // Simulate a loading process (e.g., loading resources, initializing the app)
        try {
            Thread.sleep(3000); // Simulated delay (3 seconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Close the loading screen after loading is complete
        frame.dispose();

        // Launch the main application window (replace this with your main app code)
        JFrame mainApp = new JFrame("Main Application");
        mainApp.setSize(800, 600);
        mainApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainApp.setVisible(true);
    }
}