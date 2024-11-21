package Frontend;

import java.awt.*;
import javax.swing.*;
import Backend.User;

/**
 * <h1>DreamChaserApp Class</h1>
 * The DreamChaserApp class serves as the main application window for the Dream Chaser program.
 * It manages the initialization and navigation between different screens, such as the sign-in,
 * sign-up, study session, progress report, goal creation, and statistics screens.
 *
 * <p>Usage:
 * This class initializes the JFrame and dynamically adds/removes screens based on user interactions.
 * It also manages the current user's session and provides utility methods for screen transitions.</p>
 *
 * @author Anointiyae Beasley
 * @version 2.0
 * @since 11/20/2024
 * @package Frontend
 */
public class DreamChaserApp extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private StudySessionScreen studySessionScreen;
    private ProgressReportScreen progressReportScreen;
    private StatisticsScreen statisticsScreen;
    private SignUpScreen signUpScreen;
    private GoalCreationScreen goalCreationScreen;

    private User currentUser;

    /**
     * Constructs the main application window with default screens.
     * Initializes the JFrame with a fullscreen layout and adds the initial screens.
     */
    public DreamChaserApp() {
        setTitle("Dream Chaser");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //set the frame to fullscreen
        //setUndecorated(true); //TODO: uncomment to remove window frame if we have exit button added
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel(new CardLayout());
        cardLayout = (CardLayout) mainPanel.getLayout();

        mainPanel.add(new SignInScreen(cardLayout, mainPanel, this), "SignIn");
        mainPanel.add(new LoadingScreen(), "Loading");
        mainPanel.add(new SignUpScreen(cardLayout, mainPanel), "SignUp");

        cardLayout.show(mainPanel, "SignIn");

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Dynamically initializes screens that are accessed during runtime.
     * Adds screens for progress reporting, study sessions, goal creation, and statistics.
     */
    public void initializeScreens() {
        // Ensure ProgressReportScreen is initialized and added
        if (progressReportScreen == null) {
            progressReportScreen = new ProgressReportScreen(cardLayout, mainPanel, this);
            mainPanel.add(progressReportScreen, "ProgressReport");
        }

        // Ensure StudySessionScreen is initialized and added
        if (studySessionScreen == null) {
            studySessionScreen = new StudySessionScreen(cardLayout, mainPanel, this);
            mainPanel.add(studySessionScreen, "StudySession");
        }

        // Add GoalCreationScreen if not already added
        if (!isScreenAdded("GoalCreation")) {
            goalCreationScreen = new GoalCreationScreen(cardLayout, mainPanel, this);
            mainPanel.add(goalCreationScreen, "GoalCreation");
        }

        // Add StatisticsScreen if not already added
        if (!isScreenAdded("Statistics")) {
            statisticsScreen = new StatisticsScreen(cardLayout, mainPanel, this); // Initialize here
            mainPanel.add(statisticsScreen, "Statistics");
        }
    }

    /**
     * Checks whether a screen with the specified name has already been added to the main panel.
     *
     * @param screenName The name of the screen to check.
     * @return boolean True if the screen exists, false otherwise.
     */
    private boolean isScreenAdded(String screenName) {
        for (Component comp : mainPanel.getComponents()) {
            if (mainPanel.isAncestorOf(comp) && screenName.equals(mainPanel.getComponentZOrder(comp))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the instance of the StudySessionScreen.
     *
     * @return StudySessionScreen The study session screen instance.
     */
    public StudySessionScreen getStudySessionScreen() {
        return this.studySessionScreen;
    }

    /**
     * Returns the instance of the StatisticsScreen.
     *
     * @return StatisticsScreen The statistics screen instance.
     */
    public StatisticsScreen getStatisticsScreen() {
        return this.statisticsScreen; // Correctly return the instance variable
    }

    /**
     * Navigates to a specific screen by name.
     * Updates content dynamically for certain screens, like ProgressReport and GoalCreation.
     *
     * @param screenName The name of the screen to navigate to.
     */
    public void navigateToScreen(String screenName) {
        System.out.println("Navigating to: " + screenName); // Debug print
        if ("ProgressReport".equals(screenName)) {
            if (progressReportScreen != null) {
                progressReportScreen.refreshContent(); // Refresh content dynamically
            }
        }
        if("GoalCreation".equals(screenName))
        {
            if(goalCreationScreen != null)
            {
                mainPanel.remove(goalCreationScreen);
                goalCreationScreen = new GoalCreationScreen(cardLayout, mainPanel, this);
                mainPanel.add(goalCreationScreen, "GoalCreation");
            }
        }
        cardLayout.show(mainPanel, screenName); // Navigate to the desired screen
    }

    /**
     * Retrieves the current logged-in user.
     *
     * @return User The current user instance.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current logged-in user.
     *
     * @param user The user to set as the current user.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Launches the application by creating an instance of DreamChaserApp.
     * Displays the main application window.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DreamChaserApp app = new DreamChaserApp();
            app.setVisible(true);
        });
    }
}
