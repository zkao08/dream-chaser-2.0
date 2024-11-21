package Backend;

/**
 * <h1>TestCsvEditor Class</h1>
 * The TestCsvEditor class is a utility class used to test the functionality
 * of the CsvEditor class, ensuring operations like adding users, managing
 * goals, and logging time work correctly.
 *
 * @author Max Henson
 * @version 1.0
 * @since 11/20/2024
 *
 * Usage:
 * This class is used for testing CsvEditor operations such as user management
 * and task logging.
 */

import java.util.List;

public class TestCsvEditor {

    /**
     * This is the main method which calls various test methods to verify
     * the functionality of the CsvEditor class. Uncomment test cases as needed.
     *
     * @param args Unused.
     * @return Nothing.
     */
    public static void main(String[] args) {
        // Set up directories required for storing CSV files
        setupDirectories();

        // Uncomment the following lines to run the respective test cases
        // testAddNewUser();
        // testAddGoalAndTasks();
        // testLogTimeToTask();
        // testVerifyGoalDueDate();
        testReadGoals("joy"); // Example test case for reading goals of a user
    }

    /**
     * Ensures that the required directories and files exist for data storage.
     * This method is executed first to set up the necessary file structure.
     *
     * @return Nothing.
     */
    private static void setupDirectories() {
        System.out.println("\n--- Setting up directories ---");

        // Ensure directory structure for storing user data
        CsvEditor.ensureDirectoryExists("UserData/users.csv");
        CsvEditor.ensureDirectoryExists("UserData/goals.csv");
        CsvEditor.ensureDirectoryExists("UserData/tasks.csv");
        CsvEditor.ensureDirectoryExists("UserData/loggedTime.csv");
    }

    /**
     * Adds a new user to the users CSV file and verifies the addition.
     *
     * @return Nothing.
     */
    private static void testAddNewUser() {
        System.out.println("\n--- Test 1: Adding a new user ---");

        // Test data for a new user
        String testUsername = "john_doe";
        String testPassword = "securepassword123";

        // Add the test user to the users CSV file
        CsvEditor.writeUser(testUsername, testPassword);

        // Verify the addition by reading usernames from the file
        List<String> usernames = CsvEditor.readUsernames();
        System.out.println("Usernames in the file: " + usernames);
    }

    /**
     * Adds a goal and associated tasks for a user, writes them to
     * the CSV file, and verifies the additions by reading back the data.
     *
     * @return Nothing.
     */
    private static void testAddGoalAndTasks() {
        System.out.println("\n--- Test 2: Adding a goal and tasks ---");

        // Test data
        String testUsername = "john_doe";
        String goalName = "Learn Java";
        Goal testGoal = new Goal(testUsername, goalName);

        // Add tasks to the goal
        testGoal.addTask(new Task("Complete basics", 10, 0));
        testGoal.addTask(new Task("Understand OOP concepts", 5, 0));
        testGoal.addTask(new Task("Practice problem-solving", 8, 0));
        testGoal.addTask(new Task("Build a project", 15, 0));

        // Define dates for the goal
        String dueDate = "2025-01-16";
        String startDate = "2024-11-19";

        // Write the goal and tasks to the CSV file
        CsvEditor.writeGoalAndTasks(testUsername, testGoal, dueDate, startDate);

        // Verify the goals for the user
        List<String> goals = CsvEditor.readGoals(testUsername);
        System.out.println("Goals for user " + testUsername + ": " + goals);

        // Verify the tasks for the goal
        List<Task> tasks = CsvEditor.readTasks(testUsername, goalName);
        System.out.println("Tasks for goal '" + goalName + "':");
        for (Task task : tasks) {
            System.out.println("- " + task);
        }
    }

    /**
     * Logs time for a specific task, updates the tasks CSV file,
     * and verifies the update by reading the task data.
     *
     * @return Nothing.
     */
    private static void testLogTimeToTask() {
        System.out.println("\n--- Test 3: Logging time to a task ---");

        // Test data
        String testUsername = "john_doe";
        String goalName = "Learn Java";
        String taskName = "Complete basics";

        // Log 2 hours and 30 minutes to the task
        CsvEditor.logTimeToTask(testUsername, goalName, taskName, 2, 30);

        // Verify the logged time by reading tasks for the goal
        List<Task> tasks = CsvEditor.readTasks(testUsername, goalName);
        System.out.println("Updated tasks for goal '" + goalName + "':");
        for (Task task : tasks) {
            System.out.println("- " + task);
        }
    }

    /**
     * Reads all goals associated with a specific user and displays them.
     *
     * @param username The username for whom goals need to be retrieved.
     * @return Nothing.
     */
    private static void testReadGoals(String username) {
        System.out.println("\n--- Test 4: Reading goals for user ---");

        // Retrieve the list of goals for the user
        List<String> goals = CsvEditor.readGoals(username);

        // Print the retrieved goals
        System.out.println("Goals listed: " + goals);
    }
}
