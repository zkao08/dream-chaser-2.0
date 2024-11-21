package Frontend;

import Backend.CsvEditor;
import Backend.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import Backend.User;

/**
 * <h1>StudySessionScreen Class</h1>
 * The StudySessionScreen class is responsible for providing the user interface
 * for a study session, including a timer, music player, and task logging
 * functionalities. It interacts with the backend to log user activity
 * and manage music during the session.
 *
 * <p>This class includes features such as a start/stop timer, music selection,
 * and logging the time spent on tasks, updating goals and tasks, and navigating
 * between screens. The UI elements are dynamically updated based on user input.</p>
 *
 * @author Venus Ubani
 * @version 2.0
 * @since 2024-11-20
 * @package Frontend
 */

public class StudySessionScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel timerLabel;
    private JButton toggleButton;
    private Timer timer;
    private int timeInSeconds = 0;
    private boolean isRunning = false;
    private Image backgroundImage;
    private DreamChaserApp app;
    private JLabel goalLabel;
    private String username;
    private String goalName;
    private String taskName;

    private MusicPlayer musicPlayer;

    /**
     * <h1>StudySessionScreen Constructor</h1>
     * This constructor initializes the components for the study session screen,
     * including the timer, music player, and layout of the screen. It sets up
     * the goal label, timer label, toggle button, music controls, and the
     * "End Study Session" button.
     *
     * <p>Usage:</p>
     * The constructor sets up the layout, initializes the background image,
     * and configures all buttons and event listeners.
     *
     * @param cardLayout The layout manager for switching between panels.
     * @param mainPanel The main panel that holds the current screen.
     * @param app The instance of the main application.
     */
    public StudySessionScreen(CardLayout cardLayout, JPanel mainPanel, DreamChaserApp app) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.app = app;
        this.musicPlayer = new MusicPlayer();

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/Images/AllPageBackground.png")).getImage();

        setLayout(new BorderLayout());

        // Title Panel
        goalLabel = new JLabel("No goal selected", SwingConstants.CENTER);
        goalLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Extra large font
        goalLabel.setForeground(Color.decode("#021f37"));
        add(goalLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Timer Label
        timerLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 96)); // Extra large font for timer
        timerLabel.setForeground(Color.decode("#021f37"));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(timerLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Toggle Button
        toggleButton = createStyledButton("Start Timer");
        toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleButton.addActionListener(e -> {
            if (isRunning) {
                stopTimer();
            } else {
                startTimer();
            }
        });
        centerPanel.add(toggleButton);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        // Music Panel
        JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        musicPanel.setOpaque(false);
        musicPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#021f37"), 3, true),
                "Music Player",
                0,
                0,
                new Font("Arial", Font.BOLD, 24),
                Color.decode("#021f37")
        ));

        JLabel musicLabel = new JLabel("Choose Music to Play:");
        musicLabel.setFont(new Font("Arial", Font.BOLD, 24));
        musicLabel.setForeground(Color.decode("#021f37"));
        musicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicPanel.add(musicLabel);

        ButtonGroup musicGroup = new ButtonGroup();
        Map<String, String> songs = musicPlayer.getSongs();

        for (String songName : songs.keySet()) {
            JRadioButton songButton = new JRadioButton(songName);
            songButton.setFont(new Font("Arial", Font.PLAIN, 20));
            songButton.setForeground(Color.decode("#021f37"));
            songButton.setOpaque(false);
            songButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            musicGroup.add(songButton);
            musicPanel.add(songButton);

            songButton.addActionListener(e -> {
                musicPlayer.stopMusic();
                musicPlayer.playMusic(songName);
            });
        }

        JButton stopMusicButton = createStyledButton("Stop Music");
        stopMusicButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        stopMusicButton.addActionListener(e -> musicPlayer.stopMusic());
        musicPanel.add(Box.createVerticalStrut(10));
        musicPanel.add(stopMusicButton);

        add(musicPanel, BorderLayout.EAST);

        // End Study Session Button
        JButton endSessionButton = createStyledButton("End Study Session");
        endSessionButton.setFont(new Font("Arial", Font.BOLD, 32)); // Larger font
        endSessionButton.addActionListener(e -> endStudySession());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(endSessionButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize Timer
        timer = new Timer(1000, e -> {
            timeInSeconds++;
            updateTimerLabel();
        });
    }

    /**
     * <h1>paintComponent</h1>
     * This method is called to paint the component (study session screen),
     * including drawing the background image.
     *
     * @param g The graphics context used for drawing the background image.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * <h1>setGoalAndTask</h1>
     * This method sets the current goal and task name, updating the goal label.
     *
     * @param username The username of the user for the session.
     * @param goalName The name of the current goal.
     * @param taskName The name of the current task.
     */
    public void setGoalAndTask(String username, String goalName, String taskName) {
        this.username = username;
        this.goalName = goalName;
        this.taskName = taskName;
        goalLabel.setText("Current Goal: " + goalName + ", Task: " + taskName);
    }

    /**
     * <h1>createStyledButton</h1>
     * This method creates a styled JButton with custom appearance including
     * round edges, custom background color, and centered text.
     *
     * @param text The text to display on the button.
     * @return JButton The custom styled button.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Button Background
                g2.setColor(Color.decode("#021f37")); // Navy blue
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Button Text
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
        button.setPreferredSize(new Dimension(300, 60)); // Larger button size
        button.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font
        return button;
    }

    /**
     * <h1>startTimer</h1>
     * This method starts the timer and changes the button text to "Stop Timer".
     */
    private void startTimer() {
        toggleButton.setText("Stop Timer");
        isRunning = true;
        timer.start();
    }

    /**
     * <h1>stopTimer</h1>
     * This method stops the timer and changes the button text to "Start Timer".
     * It also logs the total time that was recorded during the session.
     */
    private void stopTimer() {
        toggleButton.setText("Start Timer");
        isRunning = false;
        timer.stop();
        System.out.println("Total Time Recorded: " + timeInSeconds + " seconds");
    }

    /**
     * <h1>endStudySession</h1>
     * This method ends the study session, logs the time to the task in a CSV file,
     * stops the music, and navigates to the "ProgressReport" screen.
     */
    private void endStudySession() {
        if (timeInSeconds > 0) {
            int hours = timeInSeconds / 3600;
            int minutes = (timeInSeconds % 3600) / 60;
            CsvEditor.logTimeToTask(username, goalName, taskName, hours, minutes);

            User user = app.getCurrentUser();
            user.setGoalsAndTasks();
            System.out.printf("Logged %d hours and %d minutes to task '%s' under goal '%s'.\n", hours, minutes, taskName, goalName);
        } else {
            System.out.println("No time logged during this session.");
        }

        timeInSeconds = 0;
        updateTimerLabel();

        //Stop music
        musicPlayer.stopMusic();

        // Is this the correct navigate, or is the one below correct
        // app.navigateToScreen("ProgressReport");

        // Switch to the Progress Report Screen
        cardLayout.show(mainPanel, "ProgressReport");
    }

    /**
     * <h1>updateTimerLabel</h1>
     * This method updates the timer label to display the current time.
     */
    private void updateTimerLabel() {
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
