package Frontend;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import Backend.*;

public class StatisticsScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DreamChaserApp app; // Reference to the app
    private String currentGoal;
    private StatisticsService statsService;
    private Image backgroundImage; // To hold the background image

    public StatisticsScreen(CardLayout cardLayout, JPanel mainPanel, DreamChaserApp app) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.app = app;
        this.statsService = new StatisticsService();
        this.backgroundImage = new ImageIcon(getClass().getResource("/resources/Images/AllPageBackground.png")).getImage();

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Statistics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.decode("#021f37"));
        add(title, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void setGoal(String goalName) {
        this.currentGoal = goalName;
        refreshContent();
    }

    private void refreshContent() {
        removeAll();
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Statistics for: " + currentGoal, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Increased font size
        titleLabel.setForeground(Color.decode("#021f37"));
        add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false); // Transparent for background visibility
        statsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Larger spacing between elements

        // Fetch data for charts and table
        double completionPercentage = statsService.calculateTaskCompletionPercentage(
                app.getCurrentUser().getUsername(), currentGoal
        );
        int[] totalTimeToComplete = statsService.calculateTotalTimeToComplete(
                app.getCurrentUser().getUsername(), currentGoal
        );
        double weeklyGoal = statsService.calculateWeeklyHourGoal(
                totalTimeToComplete[0], CsvEditor.readGoalDueDate(app.getCurrentUser().getUsername(), currentGoal)
        );
        Map<Long, Integer> weeklyLoggedTime = statsService.groupLoggedTimeByWeek(
                currentGoal, app.getCurrentUser().getUsername()
        );

        // Progress Bar for Completed Tasks
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both graph and table
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) completionPercentage);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.decode("#021f37")); // Navy blue for progress
        progressBar.setBackground(Color.white); // Light gray for background
        progressBar.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font for progress bar
        progressBar.setPreferredSize(new Dimension(800, 40)); // Larger width and height
        statsPanel.add(progressBar, gbc);

        // Weekly Progress Line Chart
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH; // Expand to fill space
        statsPanel.add(createWeeklyProgressChart(weeklyGoal, weeklyLoggedTime), gbc);

        // Table of Incomplete Tasks
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH; // Expand to fill space
        statsPanel.add(createIncompleteTasksTablePanel(), gbc);

        add(statsPanel, BorderLayout.CENTER);

        // Back Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Transparent for background visibility

        // Create Back Button with Rounded Edges and Custom Styles
        JButton backButton = new JButton("Back to Progress Report") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded rectangle background
                g2.setColor(Color.decode("#021f37")); // Navy blue background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 20px corner radius

                // Draw button text
                g2.setColor(Color.WHITE); // White text color
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

        // Set Button Preferences
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(300, 60)); // Larger button size
        backButton.setFont(new Font("Arial", Font.BOLD, 20)); // Larger font for button
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ProgressReport")); // Navigate to Progress Report

        buttonPanel.add(backButton);

        // Add Button Panel to South Region
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }


    private JPanel createWeeklyProgressChart(double weeklyGoal, Map<Long, Integer> weeklyLoggedTime) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Long, Integer> entry : weeklyLoggedTime.entrySet()) {
            dataset.addValue(entry.getValue(), "Logged Time", "Week " + entry.getKey());
            dataset.addValue(weeklyGoal, "Weekly Goal", "Week " + entry.getKey());
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Weekly Goal vs Logged Time",
                "Weeks",
                "Hours",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        lineChart.getTitle().setFont(new Font("Arial", Font.BOLD, 24)); // Larger chart title
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Increase font size for axis labels and tick marks
        plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 18)); // X-axis label
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 16)); // X-axis ticks
        plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 18)); // Y-axis label
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 16)); // Y-axis ticks

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(700, 400)); // Larger chart dimensions
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createIncompleteTasksTablePanel() {
        JTable table = createIncompleteTasksTable();
        table.setFont(new Font("Arial", Font.PLAIN, 18)); // Larger font for table cells
        table.setForeground(Color.decode("#021f37")); // Navy blue font color for table cells
        table.setRowHeight(30); // Larger row height for better readability

        // Set table background and grid color
        table.setBackground(Color.WHITE); // White background for the table
        table.setGridColor(Color.decode("#021f37")); // Navy blue grid lines
        table.setOpaque(true);

        // Customize table header
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20)); // Larger font for table headers
        table.getTableHeader().setForeground(Color.decode("#021f37")); // Navy blue font color for table headers
        table.getTableHeader().setBackground(Color.WHITE); // White background for the header

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400)); // Larger table dimensions
        scrollPane.getViewport().setBackground(Color.WHITE); // White background for the viewport
        scrollPane.setOpaque(false); // Allow transparency for surrounding areas

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#021f37"), 2), // Navy blue border
                "Tasks Left",
                0,
                0,
                new Font("Arial", Font.BOLD, 20) // Larger title font
        ));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JTable createIncompleteTasksTable() {
        String[] columnNames = {"Task Name", "Time to Complete (Hours)", "Time to Complete (Minutes)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        List<Task> tasks = statsService.getIncompleteTasks(app.getCurrentUser().getUsername(), currentGoal);
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{task.getTaskName(), task.getTimeToCompleteHours(), task.getTimeToCompleteMinutes()});
        }

        JTable table = new JTable(tableModel);
        table.setOpaque(false);
        table.setBackground(new Color(245, 245, 245));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        return table;
    }
}
