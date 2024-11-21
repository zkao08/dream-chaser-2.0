package Frontend;

import Backend.CsvEditor;
import Backend.Task;
import Backend.Goal;
import Backend.AIAssistant;
import Backend.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GoalCreationScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DreamChaserApp app; // Reference to the app
    private JTextField goalNameField;
    private JTextField dueDateField;
    private JTable tasksTable;
    private User user;
    private DefaultTableModel tableModel;
    private Image backgroundImage; // To hold the background image


    public GoalCreationScreen(CardLayout cardLayout, JPanel mainPanel, DreamChaserApp app) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.app = app;
        this.user = app.getCurrentUser();
        this.setOpaque(true);

        backgroundImage = new ImageIcon(getClass().getResource("/Images/AllPageBackground.png")).getImage();
        setLayout(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        titlePanel.setOpaque(false); // Transparent
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Optional padding

        JLabel goalNameLabel = createTransparentLabel("Goal Name:");
        goalNameField = createTextField();
        titlePanel.add(goalNameLabel);
        titlePanel.add(goalNameField);

        JLabel dueDateLabel = createTransparentLabel("Due Date (yyyy-MM-dd):");
        dueDateField = createTextField();
        titlePanel.add(dueDateLabel);
        titlePanel.add(dueDateField);

        add(titlePanel, BorderLayout.NORTH);

        // Tasks table
        String[] columnNames = {"Task Name", "Time to Complete (Hours)", "Time to Complete (Minutes)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tasksTable = new JTable(tableModel) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Ensure grid lines are drawn
                setShowGrid(true);
                setGridColor(Color.decode("#021f37")); // Set grid color to navy blue
            }
        };

// Set table properties
        tasksTable.setOpaque(true);
        tasksTable.setBackground(new Color(245, 245, 245)); // Light gray background
        tasksTable.setFont(new Font("Arial", Font.PLAIN, 20)); // Cell font
        tasksTable.setRowHeight(30); // Adjust row height for better visibility
        tasksTable.setShowGrid(true);
        tasksTable.setGridColor(Color.decode("#021f37")); // Navy blue grid lines

// Remove table and intercell spacing borders
        tasksTable.setBorder(BorderFactory.createEmptyBorder()); // No border for the table
        tasksTable.setIntercellSpacing(new Dimension(0, 0)); // Remove space between cells

// Custom header renderer for column names
        JTableHeader tableHeader = tasksTable.getTableHeader();
        tableHeader.setOpaque(true);
        tableHeader.setBackground(Color.decode("#021f37")); // Dark blue background
        tableHeader.setForeground(Color.WHITE); // White font color
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16)); // Bold Arial font
        ((DefaultTableCellRenderer) tableHeader.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

// Transparent scroll pane with no borders
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // No border for the scroll pane

// Add the scroll pane to the layout
        add(scrollPane, BorderLayout.CENTER);


        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setOpaque(false); // Transparent

        JButton backButton = createTransparentButton("Back to Progress Report");
        backButton.addActionListener(e -> {
            //----Update content and go to progress report page
            app.navigateToScreen("ProgressReport");
        });
        buttonPanel.add(backButton);

        JButton addTaskButton = createTransparentButton("Add Task");
        addTaskButton.addActionListener(e -> addTask());
        buttonPanel.add(addTaskButton);

        JButton generateTasksButton = createTransparentButton("Generate Tasks Using AI");
        generateTasksButton.addActionListener(e -> generateTasks());
        buttonPanel.add(generateTasksButton);

        JButton completeButton = createTransparentButton("Complete Goal");
        completeButton.addActionListener(e -> completeGoal());
        buttonPanel.add(completeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JLabel createTransparentLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.decode("#021f37")); // Text color
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setOpaque(true); // Make it non-transparent
        textField.setBackground(new Color(245, 245, 245)); // Off-white background
        textField.setForeground(Color.decode("#021f37")); // Navy blue text color
        textField.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font style
        textField.setBorder(BorderFactory.createLineBorder(Color.decode("#021f37"))); // Navy blue border
        return textField;
    }

    private JButton createTransparentButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background (semi-transparent navy blue)
                g2.setColor(Color.decode("#021f37"));// Adjust alpha for transparency
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Text
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
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private void addTask() {
        String taskName = JOptionPane.showInputDialog(this, "Enter Task Name:");
        if (taskName == null || taskName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int hours = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Time to Complete (Hours):"));
            int minutes = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Time to Complete (Minutes):"));

            if (hours < 0 || minutes < 0 || minutes >= 60) {
                JOptionPane.showMessageDialog(this, "Invalid time entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableModel.addRow(new Object[]{taskName, hours, minutes});
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for hours and minutes.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateTasks() {
        String goalName = goalNameField.getText().trim();
        String dueDateString = dueDateField.getText().trim();

        if (goalName.isEmpty() || dueDateString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both goal name and due date before generating tasks.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate dueDate = LocalDate.parse(dueDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String envFilePath = ".env";
            AIAssistant.loadEnvFile(envFilePath);
            ArrayList<Task> tasks = AIAssistant.getTasksAI(goalName, dueDate);

            tableModel.setRowCount(0); // Clear existing rows
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
                    tableModel.addRow(new Object[]{task.getTaskName(), task.getTimeToCompleteHours(), task.getTimeToCompleteMinutes()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "AI Assistant did not generate any tasks.",
                        "AI Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Task Generation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completeGoal() {
        String goalName = goalNameField.getText().trim();
        String dueDateString = dueDateField.getText().trim();
        LocalDate currentDate = LocalDate.now();

        // Format the date as yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        if (goalName.isEmpty() || dueDateString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Goal name and due date are required.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "At least one task is required to complete the goal.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Goal goal = new Goal(user.getUsername(), goalName);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String taskName = (String) tableModel.getValueAt(i, 0);
                int timeToCompleteHours = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                int timeToCompleteMinutes = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                goal.addTask(new Task(taskName, timeToCompleteHours, timeToCompleteMinutes));
            }

            CsvEditor.writeGoalAndTasks(user.getUsername(), goal, dueDateString, formattedDate);

            // Update the user's goals and tasks
            user.setGoalsAndTasks();

            JOptionPane.showMessageDialog(this, "Goal completed and saved successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);


            app.navigateToScreen("ProgressReport");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Completion Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
