package Frontend;

import java.awt.*;
import javax.swing.*;

public class LoadingScreen extends JPanel {
    private Image backgroundImage;

    public LoadingScreen() {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/Images/Background.png")).getImage();

        setLayout(new BorderLayout());

        // Load the image as an icon
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/Images/LogoTransparent.png"));
        JLabel imageLabel = new JLabel(loadingIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel loadingLabel = new JLabel("Loading, please wait...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Create a progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Indeterminate mode for loading animation

        // Add components to the panel
        add(imageLabel, BorderLayout.CENTER);
        add(loadingLabel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image to fill the panel
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Loading Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400); // Adjust size as needed
        frame.setLocationRelativeTo(null); // Center the frame on the screen

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