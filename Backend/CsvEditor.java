package Backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvEditor {

    private static final String USERS_FILE = "UserData/users.csv";
    private static final String GOALS_FILE = "UserData/goals.csv";
    private static final String TASKS_FILE = "UserData/tasks.csv";

    // Ensure directory exists
    private static void ensureDirectoryExists(String filePath)
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

        System.out.printf("Time logged to task '%s' under goal '%s' for user '%s'.\n", taskName, goalName, username);
    }

    // Write users to CSV
    public static void writeUsers(List<User> users) {
        ensureDirectoryExists(USERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            writer.write("username,password\n");
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Write goals to CSV
    public static void writeGoals(List<User> users) {
        ensureDirectoryExists(GOALS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GOALS_FILE))) {
            writer.write("username,goalName\n");
            for (User user : users) {
                for (Goal goal : user.getGoals()) {
                    writer.write(user.getUsername() + "," + goal.getGoalName() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public static void writeGoalAndTasks(String username, Goal goal) {
        ensureDirectoryExists(GOALS_FILE);
        ensureDirectoryExists(TASKS_FILE);
        try (BufferedWriter goalWriter = new BufferedWriter(new FileWriter(GOALS_FILE, true));
             BufferedWriter taskWriter = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {

            // Add goal to goals.csv
            goalWriter.write(username + "," + goal.getGoalName() + "\n");

            // Add tasks to tasks.csv
            for (Task task : goal.getTasks()) {
                taskWriter.write(task.toCsvRow(username, goal.getGoalName()));
                taskWriter.newLine();
            }

            System.out.printf("Goal '%s' and its tasks have been added for user '%s'.\n", goal.getGoalName(), username);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read users from CSV
    public static List<User> readUsers() {
        ensureDirectoryExists(USERS_FILE);
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 2) {
                    users.add(new User(columns[0], columns[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Read goals from CSV
    public static void readGoals(List<User> users) {
        ensureDirectoryExists(GOALS_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 2) {
                    for (User user : users) {
                        if (user.getUsername().equals(columns[0])) {
                            user.addGoal(new Goal(columns[1]));
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read tasks from CSV
    public static void readTasks(List<User> users) {
        ensureDirectoryExists(TASKS_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 8) {
                    for (User user : users) {
                        if (user.getUsername().equals(columns[0])) {
                            for (Goal goal : user.getGoals()) {
                                if (goal.getGoalName().equals(columns[1])) {
                                    Task task = Task.fromCsvRow(columns);
                                    goal.addTask(task);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
