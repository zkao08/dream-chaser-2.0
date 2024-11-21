package Backend;

/**
 * <h1>CsvEditor Class</h1>
 * The CsvEditor class provides methods for managing user data, goals, tasks,
 * and logged time through CSV files. It ensures the directory structure is
 * maintained and offers utility methods for reading, writing, and modifying
 * these files.
 *
 * <p>Usage:
 * Methods include functionality to log time to tasks, write user data,
 * and retrieve usernames, among other features, while handling potential
 * file I/O issues.</p>
 *
 * @author Max Henson
 * @version 2.0
 * @since 11/20/2024
 * @package Backend
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class CsvEditor {

    // Constants representing the file paths for storing user, goal, task, and time data
    private static final String USERS_FILE = "UserData/users.csv";
    private static final String GOALS_FILE = "UserData/goals.csv";
    private static final String TASKS_FILE = "UserData/tasks.csv";
    private static final String TIME_FILE = "UserData/loggedTime.csv";

    /**
     * Ensures that the directory for a given file path exists.
     * Creates the directory if it does not exist.
     *
     * @param filePath the path of the file whose directory needs to be ensured
     */
    public static void ensureDirectoryExists(String filePath) {
        // Create a File object for the given path
        File file = new File(filePath);

        // Get the parent directory of the file
        File parentDir = file.getParentFile();

        // Check if the parent directory exists; if not, create it
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * Logs time for a specific task by updating the task's time in the tasks CSV file
     * and appending a record of the time log in the time CSV file.
     *
     * @param username the username associated with the task
     * @param goalName the name of the goal containing the task
     * @param taskName the name of the task for which time is being logged
     * @param hours    the number of hours to log
     * @param minutes  the number of minutes to log
     */
    public static void logTimeToTask(String username, String goalName, String taskName, int hours, int minutes) {
        // List to store updated lines for rewriting the tasks CSV file
        List<String> updatedLines = new ArrayList<>();
        boolean taskFound = false;

        // Ensure the tasks file directory exists
        ensureDirectoryExists(TASKS_FILE);

        // Read the tasks file and update the time for the specified task
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            // Read and store the header line
            String header = reader.readLine();
            updatedLines.add(header);

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into columns
                String[] columns = line.split(",");
                if (columns[0].equals(username) && columns[1].equals(goalName) && columns[2].equals(taskName)) {
                    // Parse the task, update its logged time, and add the updated row
                    Task task = Task.fromCsvRow(columns);
                    task.logTime(hours, minutes);
                    updatedLines.add(task.toCsvRow(username, goalName));
                    taskFound = true;
                } else {
                    // Add the unmodified line to the updated lines
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            // Handle any file I/O errors
            e.printStackTrace();
        }

        // If the task was not found, print a message and return
        if (!taskFound) {
            System.out.printf("Task '%s' under goal '%s' for user '%s' not found.\n", taskName, goalName, username);
            return;
        }

        // Write the updated lines back to the tasks file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Log the time entry in the time CSV file
        try (BufferedWriter timeWriter = new BufferedWriter(new FileWriter(TIME_FILE, true))) {
            // Get the current date in the format "yyyy-MM-dd"
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Create a log entry combining all required fields
            String logEntry = String.join(",",
                    username,
                    goalName,
                    taskName,
                    String.valueOf(hours),
                    String.valueOf(minutes),
                    currentDate);

            // Write the log entry to the file
            timeWriter.write(logEntry);
            timeWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print a success message
        System.out.printf("Time logged to task '%s' under goal '%s' for user '%s'.\n", taskName, goalName, username);
    }

    /**
     * Writes a new user entry to the users CSV file.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     */
    public static void writeUser(String username, String password) {
        // Ensure the users file directory exists
        ensureDirectoryExists(USERS_FILE);

        // Append the user data to the users file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all tasks of all users to a CSV file.
     *
     * @param users List of User objects whose tasks will be written to the file.
     */
    public static void writeTasks(List<User> users) {
        ensureDirectoryExists(TASKS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            // Write header line
            writer.write("username,goalName,taskName,timeToCompleteHours,timeToCompleteMinutes,loggedTimeHours,loggedTimeMinutes,isComplete\n");
            // Iterate through users and write their tasks
            for (User user : users) {
                for (Goal goal : user.getGoals()) {
                    for (Task task : goal.getTasks()) {
                        writer.write(task.toCsvRow(user.getUsername(), goal.getGoalName()));
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new goal along with its tasks to their respective CSV files.
     *
     * @param username  The username of the user adding the goal.
     * @param goal      The Goal object containing tasks to be added.
     * @param dueDate   The due date of the goal.
     * @param startDate The start date of the goal.
     */
    public static void writeGoalAndTasks(String username, Goal goal, String dueDate, String startDate) {
        ensureDirectoryExists(GOALS_FILE);
        ensureDirectoryExists(TASKS_FILE);
        try (BufferedWriter goalWriter = new BufferedWriter(new FileWriter(GOALS_FILE, true));
             BufferedWriter taskWriter = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {

            // Write the goal to goals.csv
            goalWriter.write(username + "," + goal.getGoalName() + "," + dueDate + "," + startDate + "\n");

            // Write tasks to tasks.csv
            for (Task task : goal.getTasks()) {
                taskWriter.write(task.toCsvRow(username, goal.getGoalName()));
                taskWriter.newLine();
            }

            System.out.printf("Goal '%s' with due date '%s', start date '%s', and its tasks have been added for user '%s'.\n",
                    goal.getGoalName(), dueDate, startDate, username);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all usernames from the users CSV file.
     *
     * @return A list of usernames.
     */
    public static List<String> readUsernames() {
        ensureDirectoryExists(USERS_FILE);
        List<String> usernames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 2) {
                    usernames.add(columns[0]); // Add username to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernames;
    }

    /**
     * Reads all passwords from the users CSV file.
     *
     * @return A list of passwords.
     */
    public static List<String> readPasswords() {
        ensureDirectoryExists(USERS_FILE);
        List<String> passwords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 2) {
                    passwords.add(columns[1]); // Add password to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passwords;
    }

    /**
     * Reads all goals for a specific user from the goals CSV file.
     *
     * @param username The username of the user.
     * @return A list of goal names for the specified user.
     */
    public static List<String> readGoals(String username) {
        ensureDirectoryExists(GOALS_FILE);
        List<String> goals = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 4 && columns[0].equals(username)) {
                    goals.add(columns[1]); // Add goal name to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return goals;
    }

    /**
     * Reads the due date of a specific goal for a user.
     *
     * @param username The username of the user.
     * @param goalName The name of the goal.
     * @return The due date as a string, or null if not found.
     */
    public static String readGoalDueDate(String username, String goalName) {
        ensureDirectoryExists(GOALS_FILE);
        System.out.println("Reading Due Date...");
        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 4 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    return columns[2].trim(); // Return due date
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Reads the start date of a specific goal for a user from the goals file.
     *
     * @param username The username of the user.
     * @param goalName The name of the goal.
     * @return The start date of the specified goal as a String, or null if the goal or start date is not found.
     */
    public static String readGoalStartDate(String username, String goalName) {
        ensureDirectoryExists(GOALS_FILE);
        System.out.println("Reading Start Date...");
        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 4 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    return columns[3].trim(); // Return the start date
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Return null if no start date is found
    }

    /**
     * Reads all tasks for a specific goal and user from the tasks CSV file.
     *
     * @param username The username of the user.
     * @param goalName The name of the goal.
     * @return A list of Task objects.
     */
    public static List<Task> readTasks(String username, String goalName) {
        ensureDirectoryExists(TASKS_FILE);
        List<Task> tasks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 8 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    // Create a Task object from the CSV row
                    Task task = Task.fromCsvRow(columns);
                    // Add the task to the list
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}