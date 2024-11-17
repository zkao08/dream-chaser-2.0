package Frontend;
/**
 * The DreamTrackerclass implements a goal tracking system that allows users
 * to create goals, manage tasks, and track their progress.
 *
 * Author: Anointiyae Beasley
 * Version: 1.1
 * Since: 10/04/2024
 *
 * Usage:
 * This class provides functionality for users to define goals, add tasks to
 * those goals, mark tasks as completed, and check goal progress.
 *
 * Change Log:
 * Version 1.0 (10/04/2024):
 * - Initial creation of the DreamTracker class, along with the Goal and Task classes.
 * Version 1.1 (10/30/2024):
 * - Creating the logic for the page. Ensuring that the user can start a timer,
 * stop the timer, and then that time is added to the users information.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StudySessionScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel timerLabel;
    private JButton toggleButton;
    private Timer timer;
    private int timeInSeconds = 0;
    private boolean isRunning = false;
    private Image backgroundImage;
    private Image logoImage;
    private JLabel goalLabel;

    public StudySessionScreen(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/Images/bg.jpg")).getImage();

        // Load the logo image
        logoImage = new ImageIcon(getClass().getResource("/Images/Logo.png")).getImage();

        setLayout(new BorderLayout());
        goalLabel = new JLabel("No goal selected", SwingConstants.CENTER);
        goalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(goalLabel, BorderLayout.CENTER);

        // Center Panel for Timer and Toggle Button
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false); // Transparent panel to show background

        // Timer label
        timerLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(timerLabel);
        centerPanel.add(Box.createVerticalStrut(20)); // Spacing

        // Toggle button with custom styling
        toggleButton = new JButton("Start Timer");
        toggleButton.setFont(new Font("Arial", Font.BOLD, 24));
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setBackground(new Color(0, 51, 102, 200)); // Dark blue with transparency
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });
        centerPanel.add(toggleButton);

        add(centerPanel, BorderLayout.CENTER);

        // End Study Session button in the lower-left corner
        JButton endSessionButton = new JButton("End Study Session");
        endSessionButton.setFont(new Font("Arial", Font.BOLD, 18));
        endSessionButton.setForeground(Color.WHITE);
        endSessionButton.setBackground(Color.BLUE); // Dark blue with transparency
        endSessionButton.setFocusPainted(false);
        endSessionButton.setBorderPainted(false);
        endSessionButton.setOpaque(false);
        endSessionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        endSessionButton.addActionListener(e -> cardLayout.show(mainPanel, "ProgressReport"));
        add(endSessionButton, BorderLayout.SOUTH);

        // Create a transparent panel for placing the logo in the top-right corner
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (logoImage != null) {
                    g.drawImage(logoImage, getWidth() - logoImage.getWidth(null) - 20, 10, null);
                }
            }
        };
        logoPanel.setOpaque(false); // Transparent to show the background
        add(logoPanel, BorderLayout.NORTH);

        // Spotify-like controls panel on the right side
        JPanel spotifyPanel = createSpotifyPanel();
        add(spotifyPanel, BorderLayout.EAST);

        // Initialize Timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeInSeconds++;
                updateTimerLabel();
            }
        });


    }

    // Method to set the selected goal
    public void setGoal(String goal) {
        goalLabel.setText("Current Goal: " + goal);
    }

    private JPanel createSpotifyPanel() {
        JPanel spotifyPanel = new JPanel();
        spotifyPanel.setLayout(new BoxLayout(spotifyPanel, BoxLayout.Y_AXIS));
        spotifyPanel.setOpaque(false); // Make it transparent

        JLabel spotifyLabel = new JLabel("Spotify");
        spotifyLabel.setFont(new Font("Arial", Font.BOLD, 20));
        spotifyLabel.setForeground(Color.WHITE);
        spotifyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        spotifyPanel.add(spotifyLabel);

        spotifyPanel.add(Box.createVerticalStrut(10)); // Spacing

        // Play/pause button
        JButton playPauseButton = new JButton("▶");
        playPauseButton.setFont(new Font("Arial", Font.BOLD, 18));
        playPauseButton.setForeground(Color.WHITE);
        playPauseButton.setBackground(new Color(0, 51, 102, 200));
        playPauseButton.setFocusPainted(false);
        playPauseButton.setBorderPainted(false);
        playPauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playPauseButton.addActionListener(e -> {
            // Toggle play/pause button text
            playPauseButton.setText(playPauseButton.getText().equals("▶") ? "⏸" : "▶");
        });
        spotifyPanel.add(playPauseButton);

        spotifyPanel.add(Box.createVerticalStrut(10)); // Spacing

        // Song progress slider
        JSlider progressSlider = new JSlider(0, 100, 0);
        progressSlider.setOpaque(false); // Transparent
        spotifyPanel.add(progressSlider);

        spotifyPanel.add(Box.createVerticalStrut(10)); // Spacing

        // Volume slider
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setOpaque(false); // Transparent
        spotifyPanel.add(volumeSlider);

        // Add spacing at the bottom
        spotifyPanel.add(Box.createVerticalGlue());

        return spotifyPanel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image to cover the entire panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void startTimer() {
        toggleButton.setText("Stop Timer");
        isRunning = true;
        timer.start();
    }

    private void stopTimer() {
        toggleButton.setText("Start Timer");
        isRunning = false;
        timer.stop();
        System.out.println("Total Time Recorded: " + timeInSeconds + " seconds");
    }

    private void updateTimerLabel() {
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}