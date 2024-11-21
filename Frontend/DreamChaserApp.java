package Frontend;

import java.awt.*;
import javax.swing.*;
import Backend.User;

public class DreamChaserApp extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private StudySessionScreen studySessionScreen;
    private ProgressReportScreen progressReportScreen;
    private StatisticsScreen statisticsScreen;
    private SignUpScreen signUpScreen;
    private GoalCreationScreen goalCreationScreen;

    private User currentUser;

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

    private boolean isScreenAdded(String screenName) {
        for (Component comp : mainPanel.getComponents()) {
            if (mainPanel.isAncestorOf(comp) && screenName.equals(mainPanel.getComponentZOrder(comp))) {
                return true;
            }
        }
        return false;
    }

    public StudySessionScreen getStudySessionScreen() {
        return this.studySessionScreen;
    }

    public StatisticsScreen getStatisticsScreen() {
        return this.statisticsScreen; // Correctly return the instance variable
    }

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

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DreamChaserApp app = new DreamChaserApp();
            app.setVisible(true);
        });
    }
}
