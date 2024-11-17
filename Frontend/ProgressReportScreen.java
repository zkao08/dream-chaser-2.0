package Frontend;

import java.awt.*;
import javax.swing.*;

public class ProgressReportScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    //Needs to be passed string of options
    public ProgressReportScreen(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Progress Report", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Panel to hold progress information and buttons
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Example progress bars for demonstration
        JProgressBar htmlProgress = new JProgressBar(0, 100);
        htmlProgress.setValue(60);
        htmlProgress.setStringPainted(true);
        contentPanel.add(new JLabel("Complete HTML Course"));
        contentPanel.add(htmlProgress);

        JProgressBar cssProgress = new JProgressBar(0, 100);
        cssProgress.setValue(30);
        cssProgress.setStringPainted(true);
        contentPanel.add(new JLabel("Complete CSS Course"));
        contentPanel.add(cssProgress);

        JProgressBar vueProgress = new JProgressBar(0, 100);
        vueProgress.setValue(80);
        vueProgress.setStringPainted(true);
        contentPanel.add(new JLabel("Complete Vue Course"));
        contentPanel.add(vueProgress);

        add(contentPanel, BorderLayout.CENTER);

        // Button panel for navigation options
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // "Statistics" button navigates to the Statistics screen
        JButton statisticsButton = new JButton("Statistics");
        statisticsButton.addActionListener(e -> cardLayout.show(mainPanel, "Statistics"));
        buttonPanel.add(statisticsButton);

        // "Add Time" dropdown menu
        // Array of options for the dropdown menu
        String[] goalOptions = {"Goal 1", "Goal 2", "Goal 3"};

        // "Add Time" button with dropdown menu
        JButton addTimeButton = new JButton("Add Time");
        // Create a popup menu for the dropdown options
        JPopupMenu addTimeMenu = new JPopupMenu();
        // Populate the menu dynamically using the array
        for (String option : goalOptions) {
            JMenuItem menuItem = new JMenuItem(option);
            menuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, option + " selected!"));
            addTimeMenu.add(menuItem);
        }
        // Show the popup menu when the button is clicked
        addTimeButton.addActionListener(e -> addTimeMenu.show(addTimeButton, 0, addTimeButton.getHeight()));
        // Add the button to the button panel
        buttonPanel.add(addTimeButton);

        // ---"Start Study Session" button navigates to the Study Session screen after selecting appropriate goal
        JButton studySessionButton = new JButton("Start Study Session");
        // Create a popup menu for the dropdown options
        JPopupMenu studySessionMenu = new JPopupMenu();
        // Variable to store the selected option
        final String[] selectedOption = {null};

        // Populate the menu dynamically using the array
        for (String option : goalOptions) {
            JMenuItem menuItem = new JMenuItem(option);
            menuItem.addActionListener(e -> {
                selectedOption[0] = option; // Store the selected option
                JOptionPane.showMessageDialog(this, option + " selected! Now starting your study session.");

                // Pass the selected option to the target screen
                StudySessionScreen studySessionScreen = (StudySessionScreen) mainPanel.getComponent(1);
                studySessionScreen.setGoal(option);

                cardLayout.show(mainPanel, "Start Study Session"); // Navigate to Study Session screen
            });
            studySessionMenu.add(menuItem);
        }
        // Show the popup menu when the button is clicked
        studySessionButton.addActionListener(e -> studySessionMenu.show(studySessionButton, 0, studySessionButton.getHeight()));
        // Add the button to the button panel
        buttonPanel.add(studySessionButton);

        // "Goal Creation" button navigates to the Goal Creation screen
        JButton goalCreationButton = new JButton("Create Goal");
        goalCreationButton.addActionListener(e -> cardLayout.show(mainPanel, "GoalCreation"));
        buttonPanel.add(goalCreationButton);

        add(buttonPanel, BorderLayout.SOUTH); // Add button panel at the bottom
    }
}