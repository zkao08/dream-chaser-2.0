package Backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class CsvEditor {

    private static final String USERS_FILE = "UserData/users.csv";
    private static final String GOALS_FILE = "UserData/goals.csv";
    private static final String TASKS_FILE = "UserData/tasks.csv";
    private static final String TIME_FILE = "UserData/loggedTime.csv";


    // Ensure directory exists
    public static void ensureDirectoryExists(String filePath)
    {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists())
        {
            parentDir.mkdirs();
        }
    }

    // Method to log time to a task and update tasks.csv
    public static void logTimeToTask(String username, String goalName, String taskName, int hours, int minutes) {
        List<String> updatedLines = new ArrayList<>();
        boolean taskFound = false;

        ensureDirectoryExists(TASKS_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            String header = reader.readLine(); // Read header
            updatedLines.add(header); // Retain header

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns[0].equals(username) && columns[1].equals(goalName) && columns[2].equals(taskName)) {
                    Task task = Task.fromCsvRow(columns);
                    task.logTime(hours, minutes);
                    updatedLines.add(task.toCsvRow(username, goalName));
                    taskFound = true;
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!taskFound) {
            System.out.printf("Task '%s' under goal '%s' for user '%s' not found.\n", taskName, goalName, username);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Log the time and date to TIME_FILE
        try (BufferedWriter timeWriter = new BufferedWriter(new FileWriter(TIME_FILE, true))) {
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String logEntry = String.join(",",
                    username,
                    goalName,
                    taskName,
                    String.valueOf(hours),
                    String.valueOf(minutes),
                    currentDate);

            timeWriter.write(logEntry);
            timeWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("Time logged to task '%s' under goal '%s' for user '%s'.\n", taskName, goalName, username);
    }

    // Write users to CSV
    public static void writeUser(String username, String password) {
        ensureDirectoryExists(USERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            // Append the new username and password as a new line
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    // Write a goal to CSV
//    public static void writeGoal(List<User> user) {
//        ensureDirectoryExists(GOALS_FILE);
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GOALS_FILE))) {
//            writer.write("username,goalName\n");
//            for (User user : users) {
//                for (Goal goal : user.getGoals()) {
//                    writer.write(user.getUsername() + "," + goal.getGoalName() + "\n");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // Write tasks to CSV
    public static void writeTasks(List<User> users) {
        ensureDirectoryExists(TASKS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            writer.write("username,goalName,taskName,timeToCompleteHours,timeToCompleteMinutes,loggedTimeHours,loggedTimeMinutes,isComplete\n");
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

    // Add a new goal with tasks to CSV
        public static void writeGoalAndTasks(String username, Goal goal, String dueDate, String startDate) {
            ensureDirectoryExists(GOALS_FILE);
            ensureDirectoryExists(TASKS_FILE);
            try (BufferedWriter goalWriter = new BufferedWriter(new FileWriter(GOALS_FILE, true));
                 BufferedWriter taskWriter = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {

                // Add goal to goals.csv with due date
                goalWriter.write(username + "," + goal.getGoalName() + "," + dueDate + "," + startDate +"\n");

                // Add tasks to tasks.csv
                for (Task task : goal.getTasks()) {
                    taskWriter.write(task.toCsvRow(username, goal.getGoalName()));
                    taskWriter.newLine();
                }

                System.out.printf("Goal '%s' with due date: '%s', start date: '%s' and its tasks have been added for user '%s'.\n",
                        goal.getGoalName(),dueDate, startDate, username);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    // Read users from CSV
    public static List<String> readUsernames() {
        ensureDirectoryExists(USERS_FILE);
        List<String> usernames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 2) {
                    usernames.add(columns[0]); // Add the username (first column) to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernames;
    }

    public static List<String> readPasswords() {
        ensureDirectoryExists(USERS_FILE);
        List<String> passwords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 2) {
                    passwords.add(columns[1]); // Add the password (second column) to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passwords;

    }


    // Read goals from CSV
    public static List<String> readGoals(String username) {
        ensureDirectoryExists(GOALS_FILE);
        List<String> goals = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 4 && columns[0].equals(username)) {
                    goals.add(columns[1]); // Add only the goal name to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return goals;
    }

    public static String readGoalDueDate(String username, String goalName) {
        ensureDirectoryExists(GOALS_FILE);
        System.out.println("Reading Due Date...");
        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 4 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    return columns[2].trim(); // Return the due date
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Return null if no due date is found
    }
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



    // Read tasks from CSV
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
                    tasks.add(task); // Add the task to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
