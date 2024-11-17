package Frontend;

import Backend.CsvEditor;
import Backend.Task;
import Backend.Goal;
import Backend.AIAssistant;
import Backend.Session;
import Backend.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GoalCreationScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentUser;

    private JTextField goalNameField;
    private JTextField dueDateField;
    private JTable tasksTable;
    private DefaultTableModel tableModel;

    public GoalCreationScreen(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.currentUser = Session.getInstance().getCurrentUser();

        setLayout(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        titlePanel.setBorder(BorderFactory.createTitledBorder("Goal Details"));

        titlePanel.add(new JLabel("Goal Name:"));
        goalNameField = new JTextField();
        titlePanel.add(goalNameField);

        titlePanel.add(new JLabel("Due Date (yyyy-MM-dd):"));
        dueDateField = new JTextField();
        titlePanel.add(dueDateField);

        add(titlePanel, BorderLayout.NORTH);

        // Tasks table
        String[] columnNames = {"Task Name", "Time to Complete (Hours)", "Time to Complete (Minutes)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tasksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tasksTable);

        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));

        JButton backButton = new JButton("Back to Progress Report");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ProgressReport"));
        buttonPanel.add(backButton);

        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> addTask());
        buttonPanel.add(addTaskButton);

        JButton generateTasksButton = new JButton("Generate Tasks Using AI");
        generateTasksButton.addActionListener(e -> generateTasks());
        buttonPanel.add(generateTasksButton);

        JButton completeButton = new JButton("Complete Goal");
        completeButton.addActionListener(e -> completeGoal());
        buttonPanel.add(completeButton);

        add(buttonPanel, BorderLayout.SOUTH);
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

            String envFilePath = "./.env";
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
            Goal goal = new Goal(goalName);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String taskName = (String) tableModel.getValueAt(i, 0);
                int timeToCompleteHours = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                int timeToCompleteMinutes = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                goal.addTask(new Task(taskName, timeToCompleteHours, timeToCompleteMinutes));
            }

            CsvEditor.writeGoalAndTasks(this.currentUser, goal);

            JOptionPane.showMessageDialog(this, "Goal completed and saved successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "ProgressReport");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Completion Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
