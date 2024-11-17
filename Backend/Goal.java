package Backend;

import java.util.ArrayList;
import java.util.List;

public class Goal {
    private String goalName;
    private List<Task> tasks;

    public Goal(String goalName) {
        this.goalName = goalName;
        this.tasks = new ArrayList<>();
    }

    // Getter for goal name
    public String getGoalName() {
        return goalName;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void logTimeToTask(String taskName, int hours, int minutes) {
        for (Task task : tasks) {
            if (task.getTaskName().equals(taskName)) {
                task.logTime(hours, minutes);
                return;
            }
        }
        System.out.println("Task '" + taskName + "' not found.");
    }

    public Task findTask(String taskName) {
        for (Task task : tasks) {
            if (task.getTaskName().equals(taskName)) {
                return task;
            }
        }
        return null;
    }

    // Converts Goal to CSV rows (tasks are serialized separately)
    public String toCsvRow(String username) {
        return String.format("%s,%s", username, goalName);
    }

    // Creates Goal from a CSV row
    public static Goal fromCsvRow(String[] columns) {
        return new Goal(columns[1]); // columns[0] is username, columns[1] is goalName
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Goal: ").append(goalName).append("\nTasks:\n");
        for (Task task : tasks) {
            sb.append("  ").append(task).append("\n");
        }
        return sb.toString();
    }
}
