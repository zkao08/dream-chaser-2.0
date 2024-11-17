package Frontend;

import java.awt.*;
import javax.swing.*;

public class DreamChaserApp extends JFrame {
    public DreamChaserApp() {
        setTitle("Dream Chaser");

        // Get the screen dimensions
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(1000, 800); // Set size to full screen

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window (not needed for full screen)

        JPanel mainPanel = new JPanel(new CardLayout());
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

        // Add each screen to the main panel
        mainPanel.add(new ProgressReportScreen(cardLayout, mainPanel), "ProgressReport");
        mainPanel.add(new StudySessionScreen(cardLayout, mainPanel), "Start Study Session");
        mainPanel.add(new GoalCreationScreen(cardLayout, mainPanel), "GoalCreation");
        mainPanel.add(new SignInScreen(cardLayout, mainPanel), "SignIn");
        mainPanel.add(new SignUpScreen(cardLayout, mainPanel), "SignUp");
        mainPanel.add(new LoadingScreen(), "Loading"); // Add Loading Screen
        mainPanel.add(new StatisticsScreen(cardLayout, mainPanel), "Statistics");


        // Show the Sign In screen at startup
        cardLayout.show(mainPanel, "SignIn");

        add(mainPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DreamChaserApp app = new DreamChaserApp();
            app.setVisible(true);
        });
    }
}