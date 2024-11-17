package Backend;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Goal> goals;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.goals = new ArrayList<>();
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Getter for goals
    public List<Goal> getGoals() {
        return goals;
    }

    // Add a goal to the user
    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    // Convert user to a CSV row
    public String toCsvRow() {
        return username + "," + password;
    }

    // Parse a User object from a CSV row
    public static User fromCsvRow(String[] columns) {
        if (columns.length < 2) {
            throw new IllegalArgumentException("Invalid CSV row for User: " + String.join(",", columns));
        }
        return new User(columns[0], columns[1]);
    }

    // Override toString for better output
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(username).append("\nGoals:\n");
        for (Goal goal : goals) {
            sb.append(goal).append("\n");
        }
        return sb.toString();
    }
}
