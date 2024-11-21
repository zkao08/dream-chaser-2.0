package Backend;

import java.util.List;

/**
 * <h1>TestUser Class</h1>
 * This class is used to test the functionality of the User class and its associated methods.
 * It demonstrates how to create a user, retrieve their goals, and print their goal details along
 * with associated tasks, including completion information.
 *
 * <p>Usage:
 * The TestUser class simulates the process of fetching user data (username, password, and goals),
 * printing out the information, and displaying details for each goal and its tasks. It is a test
 * class designed to ensure the functionality of the User and Goal classes.</p>
 *
 * @author Luke Barnett
 * @version 1.0
 * @since 2024-11-20
 * @package Backend
 */
public class TestUser {
    public static void main(String[] args) {
        // Step 3: Retrieve the user and their goals
        // Create a User object by passing the username, which will also load the user's goals and tasks
        User user = new User("joy"); // Replace "joy" with the desired username to fetch specific user
        List<Goal> userGoals = user.getGoals(); // Fetch the list of goals associated with the user


        // Step 4: Print the user's information and tasks for each goal
        // Display the username of the current user
        System.out.println("User: " + user.getUsername()); // Print the username of the current user
        // Display the password of the current user
        System.out.println("Password: " + user.getPassword()); // Print the user's password
        System.out.println("Goals and Tasks:"); // Header for goal and task details

        // Loop through each goal associated with the user
        for (Goal goal : userGoals) {
            System.out.println("- Goal: " + goal.getGoalName()); // Print the goal's name
            List<Task> tasks = goal.getTasks(); // Fetch the list of tasks associated with this goal
            // Loop through each task within the current goal
            for (Task task : tasks) {
                // Display task details including the task name, time to complete, logged time, and completion status
                System.out.println("  - Task: " + task.getTaskName()); // Print the task's name
                // Print the time required to complete the task
                System.out.println("    Time to Complete: " + task.getTimeToCompleteHours() + "h " + task.getTimeToCompleteMinutes() + "m");
                // Print the time already logged for completing the task
                System.out.println("    Logged Time: " + task.getLoggedTimeHours() + "h " + task.getLoggedTimeMinutes() + "m");
                // Print whether the task is complete or not
                System.out.println("    Completed: " + task.isComplete()); // Print the task's completion status
            }
        }
    }
}
