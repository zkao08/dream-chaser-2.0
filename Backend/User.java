package Backend;

import java.util.ArrayList;
import java.util.List;
import Backend.CsvEditor;
import Backend.Goal;
import Backend.Task;

/**
 * <h1>User Class</h1>
 * The User class represents a user in the system with associated login credentials,
 * goals, and tasks. It provides functionality to manage and retrieve user-related data
 * such as goals and passwords from CSV files, and ensures proper initialization of
 * user-specific data.
 *
 * <p>Usage:
 * The class is used to manage user credentials, goals, and tasks associated with
 * the user. It handles the initialization of goals, tasks, and password retrieval
 * from CSV files. It also allows conversion of the user object to a CSV row format
 * and parsing from CSV rows.</p>
 *
 * @author Max Henson
 * @version 2.0
 * @since 2024-11-20
 * @package Backend
 */
public class User {

    private String currentUser;  // Username of the user
    private String password;  // Password for the user
    private List<Goal> goals;  // List of goals associated with the user

    /**
     * Constructor for the User class that initializes a user with their username,
     * password, and associated goals and tasks.
     *
     * @param currentUser The username for the user.
     */
    public User(String currentUser) {
        this.currentUser = currentUser;
        this.goals = new ArrayList<>();
        this.password = setPassword();  // Initialize the password
        setGoalsAndTasks();  // Initialize goals and associated tasks
    }

    /**
     * Sets the password for the current user by retrieving it from the CSV files.
     * The password is matched with the username from the user data stored in CSV.
     *
     * @return The password for the user, or null if no matching user is found.
     */
    private String setPassword() {
        // Find the password for the current user using CsvEditor
        System.out.println("Setting Password.... ");
        List<String> usernames = CsvEditor.readUsernames();
        List<String> passwords = CsvEditor.readPasswords();
        System.out.println("CsvEditor.readUsernames returned : " + usernames);
        System.out.println("CsvEditor.readPasswords returned : " + passwords);

        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(this.currentUser)) {
                return passwords.get(i); // Return the corresponding password
            }
        }
        return null; // Return null if no matching user is found
    }

    /**
     * Reads the goals and tasks for the current user from the CSV file and initializes
     * the user's goals list. If a goal already exists, it updates the associated tasks.
     */
    public void setGoalsAndTasks() {
        // Fetch goals and their associated tasks for the current user
        System.out.println("Setting goals and tasks...");
        List<String> goalNames = CsvEditor.readGoals(this.currentUser);
        System.out.println("CsvEditor.readGoals returned : " + goalNames);

        for (String goalName : goalNames) {
            // Check for duplicate goal
            boolean goalExists = false;
            for (Goal goal : this.goals) {
                if (goal.getGoalName().equals(goalName)) {
                    goalExists = true;
                    // If goal already exists, update it
                    goal.updateGoalTasks();
                    break;
                }
            }

            // If no duplicate goal, create a new one and add it
            if (!goalExists) {
                Goal goal = new Goal(this.currentUser, goalName); // Create a goal object
                System.out.println("Goal in User: " + goal.getGoalName());
                this.goals.add(goal);
            }
        }
    }

    /**
     * Getter for the user's username.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return this.currentUser;
    }

    /**
     * Getter for the user's password.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Getter for the list of goals associated with the user.
     *
     * @return A list of the user's goals.
     */
    public List<Goal> getGoals() {
        return this.goals;
    }

    /**
     * Converts the User object to a CSV-compatible row format containing the username
     * and password.
     *
     * @return A CSV row string representation of the user.
     */
    public String toCsvRow() {
        return this.currentUser + "," + this.password;
    }

    /**
     * Parses a User object from a CSV row containing the username and password.
     *
     * @param columns The CSV row split into columns.
     * @return A User object created from the CSV data.
     * @throws IllegalArgumentException If the CSV row is invalid.
     */
    public static User fromCsvRow(String[] columns) {
        if (columns.length < 2) {
            throw new IllegalArgumentException("Invalid CSV row for User: " + String.join(",", columns));
        }
        return new User(columns[0]);
    }

    /**
     * Provides a string representation of the User object, including the username
     * and the associated goals and tasks.
     *
     * @return A string representation of the user and their goals.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(this.currentUser).append("\nGoals:\n");
        for (Goal goal : goals) {
            sb.append(goal).append("\n");
        }
        return sb.toString();
    }
}
