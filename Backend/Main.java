package Backend;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create tasks
        Task task1 = new Task("Read Chapter 1", 2, 30);
        Task task2 = new Task("Write a program", 1, 45);

        // Create a goal and add tasks
        Goal goal = new Goal("Learn Java");
        goal.addTask(task1);
        goal.addTask(task2);

        // Create a user and assign the goal
        User user = new User("john_doe", "securepassword123");
        user.addGoal(goal);

        // Save users to CSVs
        List<User> users = new ArrayList<>();
        users.add(user);

        System.out.println("Writing data to CSVs...");
        CsvEditor.writeUsers(users);
        CsvEditor.writeGoals(users);
        CsvEditor.writeTasks(users);

        // Log time to a task
        System.out.println("\nLogging time to task 'Write a program'...");
        CsvEditor.logTimeToTask("john_doe", "Learn Java", "Write a program", 1, 30);
        CsvEditor.logTimeToTask("john_doe", "Learn Java", "Read Chapter 1", 2, 35);

        // Read updated tasks
        System.out.println("\nReading data from CSVs...");
        List<User> readUsers = CsvEditor.readUsers();
        CsvEditor.readGoals(readUsers);
        CsvEditor.readTasks(readUsers);

        for (User readUser : readUsers) {
            System.out.println(readUser);
        }
    }
}
