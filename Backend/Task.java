package Backend;

public class Task {
    private String taskName;
    private int timeToCompleteHours;

    private int timeToCompleteMinutes;
    private int loggedTimeHours;
    private int loggedTimeMinutes;
    private boolean isComplete;

    public Task( String taskName, int timeToCompleteHours, int timeToCompleteMinutes) {
        setTaskName(taskName);
        setTimeToComplete(timeToCompleteHours, timeToCompleteMinutes);
        this.loggedTimeHours = 0;
        this.loggedTimeMinutes = 0;
        this.isComplete = false;
    }

    public void setTaskName(String taskName) {
        if (taskName != null && !taskName.trim().isEmpty() && taskName.length() <= 100) {
            this.taskName = taskName;
        } else {
            throw new IllegalArgumentException("Task name should be a non-empty string within 100 characters.");
        }
    }

    public void setTimeToComplete(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes >= 60) {
            throw new IllegalArgumentException("Time to complete must be non-negative and minutes should be less than 60.");
        }
        this.timeToCompleteHours = hours;
        this.timeToCompleteMinutes = minutes;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getTimeToCompleteHours() {
        return timeToCompleteHours;
    }

    public int getTimeToCompleteMinutes() {
        return timeToCompleteMinutes;
    }

    // Getter for logged time hours
    public int getLoggedTimeHours() {
        return loggedTimeHours;
    }

    // Getter for logged time minutes
    public int getLoggedTimeMinutes() {
        return loggedTimeMinutes;
    }


    public boolean isComplete() {
        return isComplete;
    }

    public String getRemainingTime() {
        if (isComplete) {
            return "Task is already complete.";
        }

        int totalTimeMinutes = (timeToCompleteHours * 60) + timeToCompleteMinutes;
        int loggedTimeMinutes = (this.loggedTimeHours * 60) + this.loggedTimeMinutes;

        int remainingMinutes = totalTimeMinutes - loggedTimeMinutes;

        // If logged time exceeds or equals total time, return zero remaining time.
        if (remainingMinutes <= 0) {
            return "Remaining Time: 0 hours, 0 minutes";
        }

        int remainingHours = remainingMinutes / 60;
        remainingMinutes %= 60;

        return String.format("Remaining Time: %d hours, %d minutes", remainingHours, remainingMinutes);
    }

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

    // Converts Task to a CSV row
    public String toCsvRow(String username, String goalName) {
        return String.format("%s,%s,%s,%d,%d,%d,%d,%b",
                username, goalName, taskName,
                timeToCompleteHours, timeToCompleteMinutes,
                loggedTimeHours, loggedTimeMinutes, isComplete);
    }

    // Creates Task from a CSV row
    public static Task fromCsvRow(String[] columns) {
        // Ensure proper length to avoid index out of bounds
        if (columns.length < 8) {
            throw new IllegalArgumentException("Invalid CSV row for Task");
        }

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

    @Override
    public String toString() {
        return String.format("Task: %s (%d hours, %d minutes, Status: %s)",
                taskName, timeToCompleteHours, timeToCompleteMinutes, isComplete ? "Complete" : "Incomplete");
    }
}
