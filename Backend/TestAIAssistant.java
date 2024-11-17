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

    public static void main(String[] args) {
        // Load environment variables from .env file
        String envFilePath = "./.env"; // Adjust path as needed
        AIAssistant.loadEnvFile(envFilePath);

        // Use AIAssistant to get tasks for a goal
        System.out.println("Using AI to get tasks for a goal...");
        LocalDate dueDate = LocalDate.of(2024, 12, 31);
        System.out.println("Due Date:" + dueDate);

        try {
            ArrayList<Task> taskList = AIAssistant.getTasksAI("Learn to code in Python", dueDate);

            // Check and display the generated tasks
            if (taskList == null || taskList.isEmpty()) {
                System.out.println("No tasks generated or an error occurred.");
            } else {
                System.out.println("Generated Tasks:");
                for (Task task : taskList) {
                    System.out.println(task);
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }
}