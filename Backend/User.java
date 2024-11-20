package Backend;

import java.util.ArrayList;
import java.util.List;
import Backend.CsvEditor;
import Backend.Goal;
import Backend.Task;

public class User {

    private String currentUser;
    private String password;
    private List<Goal> goals;




    public User(String currentUser) {
        this.currentUser = currentUser;
        this.goals =  new ArrayList<>();
        this.password = setPassword();
        setGoalsAndTasks();

    }

    private String setPassword(){
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

    public void setGoalsAndTasks(){
        // Fetch goals and their associated tasks for the current user
        System.out.println("Setting goals and tasks..." );
        List<String> goalNames = CsvEditor.readGoals(this.currentUser);
        System.out.println("CsvEditor.readGoals returned : " + goalNames);

        for (String goalName : goalNames) {
            Goal goal = new Goal(this.currentUser, goalName); // Create a goal object
            System.out.println("GOal in User: " + goal.getGoalName());
            this.goals.add(goal);
        }

    }


    // Getter for username
    public String getUsername() {
        return this.currentUser;
    }

    // Getter for password
    public String getPassword() {
        return this.password;
    }

    // Getter for goals
    public List<Goal> getGoals() {
        return this.goals;
    }





    // Convert user to a CSV row
    public String toCsvRow() {
        return this.currentUser + "," + this.password;
    }

    // Parse a User object from a CSV row
    public static User fromCsvRow(String[] columns) {
        if (columns.length < 2) {
            throw new IllegalArgumentException("Invalid CSV row for User: " + String.join(",", columns));
        }
        return new User(columns[0]);
    }

    // Override toString for better output
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
