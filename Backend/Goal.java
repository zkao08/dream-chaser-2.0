package Backend;

import java.util.ArrayList;
import java.util.List;
import Backend.CsvEditor;

/**
 * <h1>Goal Class</h1>
 * The Goal class represents a specific user's goal, along with its associated tasks
 * and due date. It allows for managing and updating tasks, fetching goal-related
 * information, and converting goal data into CSV-compatible rows.
 *
 * <p>Usage:
 * The class is used to manage tasks associated with a particular goal and retrieve
 * goal details such as the due date. It also provides functionality to convert the goal
 * into CSV format and manage the list of tasks.</p>
 *
 * @author Max Henson
 * @version 2.0
 * @since 2024-11-20
 * @package Backend
 */
public class Goal {
    private String goalName;  // Name of the goal
    private String currentUser;  // Username of the user
    private List<Task> tasks;  // List of tasks related to the goal
    private String dueDate;  // Due date of the goal

    /**
     * Constructor for the Goal class that initializes a goal with a user and goal name.
     * It also sets up the associated tasks and due date for the goal.
     *
     * @param currentUser The username of the user associated with the goal.
     * @param goalName The name of the goal.
     */
    public Goal(String currentUser, String goalName) {
        this.currentUser = currentUser;
        this.goalName = goalName;
        this.tasks = setTasks();  // Initialize the list of tasks
        this.dueDate = setDueDate();  // Initialize the due date
    }

    /**
     * Getter for the goal's name.
     *
     * @return The name of the goal.
     */
    public String getGoalName() {
        return this.goalName;
    }

    /**
     * Adds a new task to the list of tasks for the goal.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Sets the due date for the goal by fetching it from the goals file.
     *
     * @return The due date for the goal.
     */
    private String setDueDate() {
        System.out.println("Setting due date...");
        // Read from the goals file and retrieve the due date for the specified goal and user
        String currentDueDate = CsvEditor.readGoalDueDate(currentUser, goalName);
        return currentDueDate;
    }

    /**
     * Getter for the goal's due date.
     *
     * @return The due date of the goal.
     */
    public String getDueDate() {
        return this.dueDate;
    }

    /**
     * Sets the list of tasks for the goal by fetching them from the tasks file.
     *
     * @return A list of tasks associated with the goal.
     */
    private List<Task> setTasks() {
        // Fetch tasks related to this goal from the TASKS_FILE
        System.out.println("Setting Tasks...");
        List<Task> goalTasks = new ArrayList<>();
        List<Task> allTasks = CsvEditor.readTasks(currentUser, this.goalName); // Fetch tasks based on username

        for (Task task : allTasks) {
            System.out.println("Task in goalName: " + task);
            goalTasks.add(task);
        }
        return goalTasks;
    }

    /**
     * Updates the list of tasks associated with the goal.
     * This re-fetches the tasks from the tasks file.
     */
    public void updateGoalTasks() {
        this.tasks = setTasks();  // Re-fetch tasks for the goal
    }

    /**
     * Getter for the list of tasks associated with the goal.
     *
     * @return A list of tasks for the goal.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Converts the Goal object into a CSV-compatible row.
     * This only includes the goal's user and name, while tasks are serialized separately.
     *
     * @param username The username associated with the goal.
     * @return A CSV row string representation of the goal.
     */
    public String toCsvRow(String username) {
        return String.format("%s,%s", username, goalName);
    }

    /**
     * Provides a string representation of the goal and its associated tasks.
     * The goal name is followed by each task listed under it.
     *
     * @return A string representation of the goal with its tasks.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Goal: ").append(goalName).append("\nTasks:\n");
        for (Task task : tasks) {
            sb.append("  ").append(task).append("\n");  // Add each task to the string representation
        }
        return sb.toString();
    }
}
