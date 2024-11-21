package Frontend;

import java.awt.*;
import javax.swing.*;
import Backend.Goal;
import Backend.Task;
import Backend.CsvEditor;
import Backend.StatisticsService;
import Backend.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <h1>ProgressReportScreen Class</h1>
 * The ProgressReportScreen class is responsible for managing and displaying the progress report interface of the application.
 * It presents the user's goals, tasks, and the corresponding progress bars, alongside various action buttons for additional features.
 * The screen includes functionalities for viewing statistics, adding time to tasks, starting a study session, and creating new goals.
 *
 * <p>Usage:
 * This class is used within the application's main user interface to display progress on the user's goals and tasks.
 * It provides options for tracking progress, viewing statistics, adding time, and starting new study sessions.</p>
 *
 * @author Anointiyae Beasley
 * @version 2.0
 * @since 11/20/2024
 * @package Frontend
 */

public class ProgressReportScreen extends JPanel {

    //attributes
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DreamChaserApp app; // Reference to the app
    private User user;
    private JPanel contentPanel; // Panel holding progress bars'
    private Image backgroundImage; // To hold the background image

    private StudySessionScreen studySessionScreen;
    private Component buttonPanelWrapper;

    StatisticsService statsService = new StatisticsService();

    /**
     * Constructor for ProgressReportScreen.
     * Sets up the main layout, background, buttons, and initial content for the screen.
     *
     * @param cardLayout The CardLayout manager for navigating screens.
     * @param mainPanel  The main JPanel container for the application.
     * @param app        The instance of the DreamChaserApp controlling the application flow.
     */
    public ProgressReportScreen(CardLayout cardLayout, JPanel mainPanel, DreamChaserApp app) {
        //create the screen layout
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.app = app;
        this.setOpaque(true);

        //load background image from file
        backgroundImage = new ImageIcon(getClass().getResource("/Images/AllPageBackground.png")).getImage();
        setLayout(new BorderLayout());

        //create the header label
        JLabel title = new JLabel("Progress Report", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.decode("#021f37"));
        add(title, BorderLayout.NORTH);

        // Content panel to hold progress bars
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        add(contentPanel, BorderLayout.CENTER);

        // Initial population of progress bars
        updateProgressBars();
        JPanel buttonPanelWrapper = new JPanel(new BorderLayout());
        buttonPanelWrapper.setOpaque(false);
        buttonPanelWrapper.setBorder(BorderFactory.createEmptyBorder(50, 0, 300, 0)); // Adjust the bottom margin to move it higher

        // Button panel for navigation
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons for main functions

        //statistics
        JButton statisticsButton = createStyledButton("Statistics");
        statisticsButton.addActionListener(e -> showStatisticsMenu(statisticsButton));
        buttonPanel.add(statisticsButton);

        //add time
        JButton addTimeButton = createStyledButton("Add Time");
        addTimeButton.addActionListener(e -> showAddTimeMenu(addTimeButton));
        buttonPanel.add(addTimeButton);

        //study sessions
        JButton studySessionButton = createStyledButton("Start Study Session");
        studySessionButton.addActionListener(e -> showStudySessionMenu(studySessionButton));
        buttonPanel.add(studySessionButton);

        //create goal
        JButton goalCreationButton = createStyledButton("Create Goal");
        goalCreationButton.addActionListener(e -> app.navigateToScreen("GoalCreation")); // Use navigateToScreen
        buttonPanel.add(goalCreationButton);
        goalCreationButton.addActionListener(e -> {
            System.out.println("Goal Creation button clicked."); // Debug print
            app.navigateToScreen("GoalCreation");
        });

        //add the button panel to the layout
        buttonPanelWrapper.add(buttonPanel, BorderLayout.CENTER);
        add(buttonPanelWrapper, BorderLayout.SOUTH);
    }

    /**
     * Custom paint method to draw the background image for the panel.
     *
     * @param g The Graphics object used to draw the component.
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
     * Creates a styled JButton with rounded corners and a specific color scheme.
     *
     * @param text The text displayed on the button.
     * @return JButton The styled button.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background color
                g2.setColor(Color.decode("#021f37"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Text color
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50)); // Set button size
        button.setFont(new Font("Verdana", Font.BOLD, 16)); // Set font to bold and larger size
        return button;
    }

    /**
     * Updates the progress bars by re-fetching goals and recalculating percentages.
     */
    public void updateProgressBars() {
        contentPanel.removeAll(); // Clear previous progress bars
        user = app.getCurrentUser();
        user.setGoalsAndTasks();
        List<Goal> goals = user.getGoals();
        System.out.println("PROGRESS BAR GOALS:" + goals);

        // Use a HashSet to track displayed goal names
        Set<String> displayedGoalNames = new HashSet<>();

        //use grid bag layout for alignment
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        // Add spacing between components
        gridBagConstraints.insets = new Insets(20, 0, 20, 0);
        // No automatic resizing
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.gridx = 0;

        //only display progress bars if there is a goal to display
        if (goals.isEmpty()) {
            JLabel noGoalsLabel = new JLabel("No goals available.", SwingConstants.CENTER);
            noGoalsLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
            noGoalsLabel.setForeground(Color.decode("#021f37"));
            gridBagConstraints.gridy = 0;
            contentPanel.add(noGoalsLabel, gridBagConstraints);
        } else {
            //track rows for alignment
            int row = 0;
            for (Goal goal : goals) {
                // Skip duplicate goal names
                if (displayedGoalNames.contains(goal.getGoalName())) {
                    continue;
                }
                displayedGoalNames.add(goal.getGoalName());

                int totalTasks = goal.getTasks().size();
                int completedTasks = (int) goal.getTasks().stream().filter(Task::isComplete).count();

                // Calculate completion percentage
                int progressValue;
                if (totalTasks > 0) {
                    progressValue = (completedTasks * 100) / totalTasks;
                } else {
                    progressValue = 0;
                }
                // Calculate days left
                String dueDate = goal.getDueDate();
                long daysLeft = statsService.calculateDaysLeft(dueDate);
                String daysLeftText = (daysLeft >= 0) ? daysLeft + " Days Left" : "Due Date Passed";

                // Goal title label
                JLabel goalLabel = new JLabel(goal.getGoalName(), SwingConstants.CENTER);
                goalLabel.setFont(new Font("Verdana", Font.BOLD, 24)); // Larger font for the goal title
                goalLabel.setForeground(Color.decode("#021f37"));
                gridBagConstraints.gridy = row++;
                contentPanel.add(goalLabel, gridBagConstraints);

                // Days left label under the goal title
                JLabel daysLeftLabel = new JLabel(daysLeftText, SwingConstants.CENTER);
                daysLeftLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
                daysLeftLabel.setForeground(Color.decode("#021f37"));
                gridBagConstraints.gridy = row++;
                contentPanel.add(daysLeftLabel, gridBagConstraints);

                // Custom-styled progress bar
                JProgressBar progressBar = new JProgressBar(0, 100) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Fill the off-white background
                        g2.setColor(Color.decode("#f5f5f5")); // Off-white color
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                        // Fill the progress portion
                        int progressWidth = (int) (getWidth() * ((double) getValue() / getMaximum()));
                        g2.setColor(Color.decode("#021f37")); // Navy blue color
                        g2.fillRoundRect(0, 0, progressWidth, getHeight(), 20, 20);
                    }
                };

                //set size and value of progress bar
                progressBar.setValue(progressValue);
                progressBar.setPreferredSize(new Dimension(600, 25));
                progressBar.setBorder(BorderFactory.createEmptyBorder());
                gridBagConstraints.gridy = row++;

                //add the progress bar to the grid
                contentPanel.add(progressBar, gridBagConstraints);
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Displays a dropdown menu for selecting a goal to view statistics.
     *
     * @param button The button triggering the statistics menu.
     */
    private void showStatisticsMenu(JButton button) {
        JPopupMenu statisticsMenu = new JPopupMenu();
        user = app.getCurrentUser();
        List<Goal> goals = user.getGoals();

        if (!goals.isEmpty()) {
            for (Goal goal : goals) {
                JMenuItem goalItem = new JMenuItem(goal.getGoalName());
                goalItem.addActionListener(e -> {
                    // Pass the selected goal to the Statistics screen
                    StatisticsScreen statisticsScreen = app.getStatisticsScreen();
                    statisticsScreen.setGoal(goal.getGoalName());

                    // Navigate to the Statistics screen
                    cardLayout.show(mainPanel, "Statistics");
                });
                statisticsMenu.add(goalItem);
            }
        } else {
            JMenuItem noGoalsItem = new JMenuItem("No goals available");
            noGoalsItem.setEnabled(false);
            statisticsMenu.add(noGoalsItem);
        }

        statisticsMenu.show(button, 0, button.getHeight());
    }

    /**
     * Displays a dropdown menu for selecting a goal to add time to.
     *
     * @param button The button triggering the add-time menu.
     */
    private void showAddTimeMenu(JButton button) {
        SwingUtilities.invokeLater(() -> {
            if (button.isShowing()) { // Ensure the button is fully visible
                JPopupMenu studySessionMenu = new JPopupMenu();
                user = app.getCurrentUser();
                List<Goal> goals = user.getGoals();

                if (!goals.isEmpty()) {
                    for (Goal goal : goals) {
                        JMenu goalMenu = new JMenu(goal.getGoalName());

                        List<Task> tasks = goal.getTasks();
                        if (!tasks.isEmpty()) {
                            for (Task task : tasks) {
                                //don't show for tasks that are complete
                                if(!task.isComplete())
                                {
                                    JMenuItem taskItem = new JMenuItem(task.getTaskName());
                                    taskItem.addActionListener(e -> showAddTimePopup(goal, task));
                                    goalMenu.add(taskItem);
                                }
                            }
                        } else {
                            JMenuItem noTasksItem = new JMenuItem("No tasks available");
                            noTasksItem.setEnabled(false);
                            goalMenu.add(noTasksItem);
                        }

                        studySessionMenu.add(goalMenu);
                    }
                } else {
                    JMenuItem noGoalsItem = new JMenuItem("No goals available");
                    noGoalsItem.setEnabled(false);
                    studySessionMenu.add(noGoalsItem);
                }

                // Show the popup menu
                studySessionMenu.show(button, 0, button.getHeight());
            } else {
                System.err.println("Button is not fully visible on screen.");
            }
        });
    }

    /**
     * Displays a context menu for selecting tasks of a specific goal.
     *
     * @param goal The Goal object for which tasks are being displayed.
     */
    private void showTasksForGoal(Goal goal) {
        JPopupMenu taskMenu = new JPopupMenu();
        List<Task> tasks = goal.getTasks();

        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                //don't show menu if task is complete
                if(!task.isComplete())
                {
                    JMenuItem taskItem = new JMenuItem(task.getTaskName());
                    taskItem.addActionListener(e -> showAddTimePopup(goal, task));
                    taskMenu.add(taskItem);
                }
            }
        } else {
            JMenuItem noTasksItem = new JMenuItem("No tasks available");
            noTasksItem.setEnabled(false);
            taskMenu.add(noTasksItem);
        }

        taskMenu.show(this, getWidth() / 2, getHeight() / 2);
    }

    /**
     * Displays a popup dialog for adding time to a specific task within a goal.
     *
     * @param goal The Goal object to which the task belongs.
     * @param task The Task object to which time will be added.
     */
    private void showAddTimePopup(Goal goal, Task task) {
        JDialog addTimeDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Time", true);
        addTimeDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel goalLabel = new JLabel("Goal: " + goal.getGoalName());
        JLabel taskLabel = new JLabel("Task: " + task.getTaskName());

        JTextField hoursField = new JTextField(5);
        JTextField minutesField = new JTextField(5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addTimeDialog.add(goalLabel, gbc);

        gbc.gridy = 1;
        addTimeDialog.add(taskLabel, gbc);

        gbc.gridy = 2;
        addTimeDialog.add(new JLabel("Hours:"), gbc);

        gbc.gridx = 1;
        addTimeDialog.add(hoursField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addTimeDialog.add(new JLabel("Minutes:"), gbc);

        gbc.gridx = 1;
        addTimeDialog.add(minutesField, gbc);

        JButton addButton = new JButton("Add Time");
        addButton.addActionListener(e -> {
            try {
                int hours = Integer.parseInt(hoursField.getText().trim());
                int minutes = Integer.parseInt(minutesField.getText().trim());

                if (hours < 0 || minutes < 0 || minutes >= 60) {
                    JOptionPane.showMessageDialog(addTimeDialog, "Invalid time entered. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                CsvEditor.logTimeToTask(app.getCurrentUser().getUsername(), goal.getGoalName(), task.getTaskName(), hours, minutes);
                JOptionPane.showMessageDialog(addTimeDialog, "Time added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                refreshContent();
                addTimeDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addTimeDialog, "Please enter valid numbers for hours and minutes.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addTimeDialog.add(addButton, gbc);

        addTimeDialog.pack();
        addTimeDialog.setLocationRelativeTo(this);
        addTimeDialog.setVisible(true);
    }

    /**
     * Refreshes the content of the screen by re-fetching and updating goal progress details.
     */
    public void refreshContent() {
        updateProgressBars(); // Re-fetch and update goal progress details
    }

    /**
     * Refreshes the styles dynamically, updating components like background and fonts.
     */
    public void refreshStyles() {
        // Update the styles dynamically (e.g., background, fonts, etc.)
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Displays a dropdown menu for navigating to the study session screen for a specific task.
     *
     * @param button The button triggering the study session menu.
     */
    private void showStudySessionMenu(JButton button) {
        SwingUtilities.invokeLater(() -> {
            if (button.isShowing()) { // Ensure the button is fully visible
                JPopupMenu studySessionMenu = new JPopupMenu();
                user = app.getCurrentUser();

                List<Goal> goals = user.getGoals();

                if (!goals.isEmpty()) {
                    for (Goal goal : goals) {
                        JMenu goalMenu = new JMenu(goal.getGoalName());

                        List<Task> tasks = goal.getTasks();
                        if (!tasks.isEmpty()) {
                            for (Task task : tasks) {
                                //don't show for tasks that are complete
                                if(!task.isComplete())
                                {
                                    JMenuItem taskItem = new JMenuItem(task.getTaskName());
                                    taskItem.addActionListener(e -> {
                                        studySessionScreen = app.getStudySessionScreen();
                                        // Send goal and task info to the StudySessionScreen
                                        studySessionScreen.setGoalAndTask(
                                                user.getUsername(), goal.getGoalName(), task.getTaskName());
                                        app.navigateToScreen("StudySession");
                                    });
                                    goalMenu.add(taskItem);
                                }
                            }
                        } else {
                            JMenuItem noTasksItem = new JMenuItem("No tasks available");
                            noTasksItem.setEnabled(false);
                            goalMenu.add(noTasksItem);
                        }

                        studySessionMenu.add(goalMenu);
                    }
                } else {
                    JMenuItem noGoalsItem = new JMenuItem("No goals available");
                    noGoalsItem.setEnabled(false);
                    studySessionMenu.add(noGoalsItem);
                }

                // Show the popup menu
                studySessionMenu.show(button, 0, button.getHeight());
            } else {
                System.err.println("Button is not fully visible on screen.");
            }
        });
    }

    /**
     * Main method for testing and demonstrating the ProgressReportScreen class.
     * Creates a test frame to display the screen.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a JFrame to display the screen
            JFrame frame = new JFrame("Progress Report Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800); // Set the window size
            frame.setLocationRelativeTo(null); // Center the window on the screen

            // Create a dummy CardLayout and JPanel for testing
            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            // Create a dummy DreamChaserApp instance
            DreamChaserApp app = new DreamChaserApp();
            User user = new User("joy");
            app.setCurrentUser(user);

            // Create the ProgressReportScreen instance
            ProgressReportScreen progressReportScreen = new ProgressReportScreen(cardLayout, mainPanel, app);

            // Add the screen to the frame
            frame.add(progressReportScreen);

            // Make the frame visible
            frame.setVisible(true);
        });
    }

}
