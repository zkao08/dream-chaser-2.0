package Backend;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import Backend.AIAssistant;
import Backend.Task;

/**
 * AIAssistant class tester
 *
 * @Author: Zachary Kao
 * @Version: 2.0
 * @Since: 11/8/2024
 *
 * Usage:
 * Verify that the AIAssistant class is working as intended
 *
 * Change Log:
 * Version 1.0 (11/8/2024):
 * - Created tester for the AIAssistant class
 * Version 2.0 (11/16/2024):
 * - Update to match new version of AIAssistant
 */

public class TestAIAssistant {

    /**
     * The main method that drives the test of the AIAssistant functionality.
     * It demonstrates loading environment variables, setting up a goal,
     * and using the AIAssistant to generate tasks for the goal.
     *
     * @param args Command-line arguments (not used in this test).
     */
    public static void main(String[] args) {
        // Load environment variables from the .env file to configure API keys and other settings
        String envFilePath = ".env"; // The path to the .env file (adjust as needed)
        AIAssistant.loadEnvFile(envFilePath); // Loading the environment file

        // Set a goal and display the corresponding due date
        System.out.println("Using AI to get tasks for a goal...");
        LocalDate dueDate = LocalDate.of(2024, 12, 31); // Set the due date for the goal
        System.out.println("Due Date: " + dueDate); // Display the due date

        try {
            // Generate the list of tasks using the AIAssistant for the goal "Learn to code in Python"
            ArrayList<Task> taskList = AIAssistant.getTasksAI("Learn to code in Python", dueDate);

            // Check if the task list is null or empty, indicating a problem in generating tasks
            if (taskList == null || taskList.isEmpty()) {
                System.out.println("No tasks generated or an error occurred.");
            } else {
                // If tasks are generated, print each task
                System.out.println("Generated Tasks:");
                for (Task task : taskList) {
                    System.out.println(task); // Print each task's details
                }
            }
        } catch (Exception e) {
            // If an error occurs while generating tasks, catch and display the exception details
            System.err.println("An error occurred while generating tasks: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
}