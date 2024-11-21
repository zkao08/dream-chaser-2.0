package Backend;

/**
 * <h1>Task Class</h1>
 * The Task class represents a specific task with a name, estimated completion time,
 * logged time, and completion status. It provides methods to set and get task details,
 * log time, and calculate remaining time. The class also supports conversion to and
 * from CSV rows for file operations.
 *
 * <p>Usage:
 * Create instances of Task to manage individual task details. Methods are provided
 * to log time, check remaining time, and mark tasks as complete.</p>
 *
 * @author Luke Barnett
 * @version 2.0
 * @since 11/20/2024
 * @package Backend
 */
public class Task {
    private String taskName;
    private int timeToCompleteHours;
    private int timeToCompleteMinutes;
    private int loggedTimeHours;
    private int loggedTimeMinutes;
    private boolean isComplete;

    /**
     * Constructor to initialize a Task object with name and estimated time.
     *
     * @param taskName             The name of the task (non-empty and within 100 characters).
     * @param timeToCompleteHours  The estimated hours to complete the task.
     * @param timeToCompleteMinutes The estimated minutes to complete the task (less than 60).
     */
    public Task(String taskName, int timeToCompleteHours, int timeToCompleteMinutes) {
        setTaskName(taskName);
        setTimeToComplete(timeToCompleteHours, timeToCompleteMinutes);
        this.loggedTimeHours = 0;
        this.loggedTimeMinutes = 0;
        this.isComplete = false;
    }

    /**
     * Sets the name of the task after validating the input.
     *
     * @param taskName The name of the task to be set (non-empty and within 100 characters).
     * @throws IllegalArgumentException if the task name is invalid.
     */
    public void setTaskName(String taskName) {
        if (taskName != null && !taskName.trim().isEmpty() && taskName.length() <= 100) {
            this.taskName = taskName;
        } else {
            throw new IllegalArgumentException("Task name should be a non-empty string within 100 characters.");
        }
    }

    /**
     * Sets the estimated time to complete the task.
     *
     * @param hours  The hours to complete the task (non-negative).
     * @param minutes The minutes to complete the task (less than 60 and non-negative).
     * @throws IllegalArgumentException if the time is invalid.
     */
    public void setTimeToComplete(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes >= 60) {
            throw new IllegalArgumentException("Time to complete must be non-negative and minutes should be less than 60.");
        }
        this.timeToCompleteHours = hours;
        this.timeToCompleteMinutes = minutes;
    }

    /**
     * Retrieves the task name.
     *
     * @return String The name of the task.
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Retrieves the estimated hours to complete the task.
     *
     * @return int The estimated hours to complete.
     */
    public int getTimeToCompleteHours() {
        return timeToCompleteHours;
    }

    /**
     * Retrieves the estimated minutes to complete the task.
     *
     * @return int The estimated minutes to complete.
     */
    public int getTimeToCompleteMinutes() {
        return timeToCompleteMinutes;
    }

    /**
     * Retrieves the logged time in hours.
     *
     * @return int The logged time in hours.
     */
    public int getLoggedTimeHours() {
        return loggedTimeHours;
    }

    /**
     * Retrieves the logged time in minutes.
     *
     * @return int The logged time in minutes.
     */
    public int getLoggedTimeMinutes() {
        return loggedTimeMinutes;
    }

    /**
     * Checks whether the task is complete.
     *
     * @return boolean True if the task is complete, otherwise false.
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Calculates and retrieves the remaining time to complete the task.
     *
     * @return String The formatted remaining time or a completion message.
     */
    public String getRemainingTime() {
        if (isComplete) {
            return "Task is already complete.";
        }

        int totalTimeMinutes = (timeToCompleteHours * 60) + timeToCompleteMinutes;
        int loggedTimeMinutes = (this.loggedTimeHours * 60) + this.loggedTimeMinutes;

        int remainingMinutes = totalTimeMinutes - loggedTimeMinutes;

        if (remainingMinutes <= 0) {
            return "Remaining Time: 0 hours, 0 minutes";
        }

        int remainingHours = remainingMinutes / 60;
        remainingMinutes %= 60;

        return String.format("Remaining Time: %d hours, %d minutes", remainingHours, remainingMinutes);
    }

    /**
     * Logs time towards task completion and checks if the task is complete.
     *
     * @param hours  The hours to log.
     * @param minutes The minutes to log.
     * @throws IllegalArgumentException if the logged time is invalid.
     */
    public void logTime(int hours, int minutes) {
        if (hours < 0 || minutes < 0) {
            throw new IllegalArgumentException("Logged time cannot be negative.");
        }
        if (isComplete) {
            System.out.println("Task is already complete. No additional time can be logged.");
            return;
        }

        // Add the logged time
        this.loggedTimeHours += hours;
        this.loggedTimeMinutes += minutes;

        // Roll over excess minutes to hours
        if (this.loggedTimeMinutes >= 60) {
            this.loggedTimeHours += this.loggedTimeMinutes / 60;
            this.loggedTimeMinutes %= 60;
        }

        // Calculate total logged and total required time in minutes
        int totalTimeMinutes = (timeToCompleteHours * 60) + timeToCompleteMinutes;
        int totalLoggedMinutes = (this.loggedTimeHours * 60) + this.loggedTimeMinutes;

        // Mark as complete if total logged time meets or exceeds total time
        if (totalLoggedMinutes >= totalTimeMinutes) {
            this.isComplete = true;
            System.out.println("Task '" + this.taskName + "' is now complete.");
        }
    }

    /**
     * Converts the Task object to a CSV row for storage.
     *
     * @param username The username associated with the task.
     * @param goalName The goal name associated with the task.
     * @return String The formatted CSV row.
     */
    public String toCsvRow(String username, String goalName) {
        return String.format("%s,%s,%s,%d,%d,%d,%d,%b",
                username, goalName, taskName,
                timeToCompleteHours, timeToCompleteMinutes,
                loggedTimeHours, loggedTimeMinutes, isComplete);
    }

    /**
     * Creates a Task object from a CSV row.
     *
     * @param columns The CSV row data split into an array of strings.
     * @return Task The created Task object.
     * @throws IllegalArgumentException if the CSV row is invalid.
     */
    public static Task fromCsvRow(String[] columns) {
        // Ensure proper length to avoid index out of bounds
        if (columns.length < 8) {
            throw new IllegalArgumentException("Invalid CSV row for Task");
        }

        // Parsing Csv information
        String taskName = columns[2];
        int timeToCompleteHours = Integer.parseInt(columns[3]);
        int timeToCompleteMinutes = Integer.parseInt(columns[4]);
        int loggedTimeHours = Integer.parseInt(columns[5]);
        int loggedTimeMinutes = Integer.parseInt(columns[6]);
        boolean isComplete = Boolean.parseBoolean(columns[7]);

        Task task = new Task(taskName, timeToCompleteHours, timeToCompleteMinutes);
        task.loggedTimeHours = loggedTimeHours;
        task.loggedTimeMinutes = loggedTimeMinutes;
        task.isComplete = isComplete;
        return task;
    }

    /**
     * Overrides the toString method to provide a string representation of the Task object.
     *
     * @return String The formatted task details.
     */
    @Override
    public String toString() {
        return String.format("Task: %s (%d hours, %d minutes, Status: %s)",
                taskName, timeToCompleteHours, timeToCompleteMinutes, isComplete ? "Complete" : "Incomplete");
    }
}
