package Backend;

import java.util.ArrayList;
import java.util.List;
import Backend.CsvEditor;

public class Goal {
    private String goalName;
    private String currentUser;
    private List<Task> tasks;
    private String dueDate;

    public Goal(String currentUser, String goalName) {
        this.currentUser = currentUser;
        this.goalName = goalName;
        this.tasks = setTasks();
        this.dueDate = setDueDate();
    }

    // Getter for goal name
    public String getGoalName() {
        return this.goalName;
    }

    public void  addTask(Task task){
        this.tasks.add(task);
    }

    private String setDueDate(){

        System.out.println("Setting due date...");
        //Read from goals file and get the due date for that goal name and username
        String currentDueDate = CsvEditor.readGoalDueDate(currentUser,goalName);
        return currentDueDate;
    }

    public String getDueDate(){
        return this.dueDate;
    }

    private List<Task> setTasks(){
        // Fetch tasks related to this goal from the TASKS_FILE
        System.out.println("Setting Tasks...");
        List<Task> goalTasks = new ArrayList<>();
        List<Task> allTasks = CsvEditor.readTasks(currentUser,this.goalName); // Fetch tasks based on username

        for (Task task : allTasks) {
            System.out.println("Task in goalName: " + task);
            goalTasks.add(task);
        }
        return goalTasks;
    }

    public void updateGoalTasks()
    {
        this.tasks = setTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    // Converts Goal to CSV rows (tasks are serialized separately)
    public String toCsvRow(String username) {
        return String.format("%s,%s", username, goalName);
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
