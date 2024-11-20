package Backend;

import java.util.List;

public class TestUser {
    public static void main(String[] args) {
        // Step 3: Retrieve the user and their goals
        User user = new User("joy"); // Replace "joy" with the desired username
        List<Goal> userGoals = user.getGoals();


        // Step 4: Print the user's information and tasks for each goal
        System.out.println("User: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Goals and Tasks:");
        for (Goal goal : userGoals) {
            System.out.println("- Goal: " + goal.getGoalName());
            List<Task> tasks = goal.getTasks(); // Fetch tasks for this goal
            for (Task task : tasks) {
                System.out.println("  - Task: " + task.getTaskName());
                System.out.println("    Time to Complete: " + task.getTimeToCompleteHours() + "h " + task.getTimeToCompleteMinutes() + "m");
                System.out.println("    Logged Time: " + task.getLoggedTimeHours() + "h " + task.getLoggedTimeMinutes() + "m");
                System.out.println("    Completed: " + task.isComplete());
            }
        }
    }
}

