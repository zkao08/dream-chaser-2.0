package Frontend;

import java.awt.*;
import javax.swing.*;

public class StatisticsScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public StatisticsScreen(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Statistics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        // Example statistics content
        JLabel completionLabel = new JLabel("Goal Completion: 75%");
        completionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        statsPanel.add(completionLabel);

        JLabel timeSpentLabel = new JLabel("Total Time Spent: 120 Hours");
        timeSpentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        statsPanel.add(timeSpentLabel);

        add(statsPanel, BorderLayout.CENTER);

        // Add "Back to Progress Report" button
        JButton backButton = new JButton("Back to Progress Report");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ProgressReport"));
        add(backButton, BorderLayout.SOUTH);
    }
}