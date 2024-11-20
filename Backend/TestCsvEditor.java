
package Backend;

import java.util.List;

    public class TestCsvEditor {

        public static void main(String[] args) {
            // Ensure directories exist
            setupDirectories();

//            // Run all tests
//            testAddNewUser();
//            testAddGoalAndTasks();
//            testLogTimeToTask();
//            testVerifyGoalDueDate();
              testReadGoals("joy");
       }

        private static void setupDirectories() {
            System.out.println("\n--- Setting up directories ---");
            CsvEditor.ensureDirectoryExists("UserData/users.csv");
            CsvEditor.ensureDirectoryExists("UserData/goals.csv");
            CsvEditor.ensureDirectoryExists("UserData/tasks.csv");
            CsvEditor.ensureDirectoryExists("UserData/loggedTime.csv");
        }

        private static void testAddNewUser() {
            System.out.println("\n--- Test 1: Adding a new user ---");
            String testUsername = "john_doe";
            String testPassword = "securepassword123";

            // Add user
            CsvEditor.writeUser(testUsername, testPassword);

            // Verify user was added
            List<String> usernames = CsvEditor.readUsernames();
            System.out.println("Usernames in the file: " + usernames);
        }

        private static void testAddGoalAndTasks() {
            System.out.println("\n--- Test 2: Adding a goal and tasks ---");
            String testUsername = "john_doe";
            String goalName = "Learn Java";
            Goal testGoal = new Goal(testUsername, goalName);

            // Add tasks
            testGoal.addTask(new Task("Complete basics", 10, 0));
            testGoal.addTask(new Task("Understand OOP concepts", 5, 0));
            testGoal.addTask(new Task("Practice problem-solving", 8, 0));
            testGoal.addTask(new Task("Build a project", 15, 0));
            String dueDate = "2025-01-16";
            String startDate = "2024-11-19";

            // Write to CSV
            CsvEditor.writeGoalAndTasks(testUsername, testGoal, dueDate, startDate);

            // Verify goals
            List<String> goals = CsvEditor.readGoals(testUsername);
            System.out.println("Goals for user " + testUsername + ": " + goals);

            // Verify tasks
            List<Task> tasks = CsvEditor.readTasks(testUsername, goalName);
            System.out.println("Tasks for goal '" + goalName + "':");
            for (Task task : tasks) {
                System.out.println("- " + task);
            }
        }

        private static void testLogTimeToTask() {
            System.out.println("\n--- Test 3: Logging time to a task ---");
            String testUsername = "john_doe";
            String goalName = "Learn Java";
            String taskName = "Complete basics";

            // Log time
            CsvEditor.logTimeToTask(testUsername, goalName, taskName, 2, 30);

            // Verify logged time
            List<Task> tasks = CsvEditor.readTasks(testUsername, goalName);
            System.out.println("Updated tasks for goal '" + goalName + "':");
            for (Task task : tasks) {
                System.out.println("- " + task);
            }
        }

        private static void testReadGoals(String username) {
            List<String> goals = CsvEditor.readGoals(username);
            System.out.println("Goals listed:" + goals);

        }
    }




