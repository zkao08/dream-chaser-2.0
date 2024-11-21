package Backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import Backend.CsvEditor;


/**
 * StatisticsService.java
 * Created By: Max Henson
 * Date Created: 10/03/2024
 * Version: 1.0
 *
 * Description:
 * Provides functionality to calculate user statistics for goals and tasks.
 *
 * Change Log:
 * Version 1.0 (10/30/2024):
 * - Updated placeholder functions/code, added a method to increment number of tasks a user has started
 */
public class StatisticsService {
    private static final String LOGGED_TIME_FILE = "UserData/loggedTime.csv";


    /**
     * Calculates the task completion percentage for a user.
     *
     * @param username The username of the user.
     * @return Completion percentage as a double.
     */
    public double calculateTaskCompletionPercentage(String username, String goalName) {
        CsvEditor.ensureDirectoryExists("UserData/tasks.csv");

        int totalTasks = 0;
        int completedTasks = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("UserData/tasks.csv"))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 8 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    totalTasks++; // Count every task
                    if (Boolean.parseBoolean(columns[7].trim())) { // Check if task is complete
                        completedTasks++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate the completion percentage (ensure it's valid based on tasks)
        double completionPercentage;
        if (totalTasks > 0) {
            completionPercentage = ((double) completedTasks / totalTasks) * 100;
        } else {
            completionPercentage = 0.0;
        }

        System.out.printf("Completion percentage for goal '%s' by user '%s': %.2f%%\n", goalName, username, completionPercentage);
        return completionPercentage;
    }


    public int[] calculateTotalTimeToComplete(String username, String goalName) {
        // Ensure the TASKS_FILE exists
        CsvEditor.ensureDirectoryExists("UserData/tasks.csv");

        int totalHours = 0;
        int totalMinutes = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("UserData/tasks.csv"))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 8 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    // Parse timeToCompleteHours and timeToCompleteMinutes
                    int timeToCompleteHours = Integer.parseInt(columns[3].trim());
                    int timeToCompleteMinutes = Integer.parseInt(columns[4].trim());

                    // Accumulate time
                    totalHours += timeToCompleteHours;
                    totalMinutes += timeToCompleteMinutes;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert minutes to hours if totalMinutes >= 60
        totalHours += totalMinutes / 60;
        totalMinutes = totalMinutes % 60;

        // Return total hours and minutes as an array
        return new int[]{totalHours, totalMinutes};
    }

    public List<Task> getIncompleteTasks(String username, String goalName) {
        CsvEditor.ensureDirectoryExists("UserData/tasks.csv");

        List<Task> incompleteTasks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("UserData/tasks.csv"))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 8 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    if (!Boolean.parseBoolean(columns[7].trim())) { // If task is not complete
                        incompleteTasks.add(Task.fromCsvRow(columns));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return incompleteTasks;
    }


    public Map<Long, Integer> groupLoggedTimeByWeek(String goalName, String username) {
        Map<Long, Integer> weeklyLoggedTime = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("UserData/loggedTime.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns[0].equals(username) && columns[1].equals(goalName)) {
                    String logDate = columns[5];
                    int hours = Integer.parseInt(columns[3]);
                    int minutes = Integer.parseInt(columns[4]);

                    LocalDate logDateParsed = LocalDate.parse(logDate);
                    LocalDate startDateParsed = LocalDate.parse(Objects.requireNonNull(CsvEditor.readGoalStartDate(username, goalName)));

                    long weekNumber = ChronoUnit.WEEKS.between(startDateParsed, logDateParsed);
                    weeklyLoggedTime.put(weekNumber, weeklyLoggedTime.getOrDefault(weekNumber, 0) + hours + (minutes / 60));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklyLoggedTime;
    }

    public double calculateAccuracy(Map<Long, Integer> weeklyLoggedTime, double weeklyGoal) {
        int weeksMetGoal = 0;
        for (long week : weeklyLoggedTime.keySet()) {
            if (weeklyLoggedTime.get(week) >= weeklyGoal) {
                weeksMetGoal++;
            }
        }
        return (weeklyLoggedTime.size() > 0) ? (double) weeksMetGoal / weeklyLoggedTime.size() * 100 : 0;
    }

    /**
     * Calculates the number of days left until the goal's due date.
     *
     * @param dueDateString The due date in the format "yyyy-MM-dd".
     * @return Number of days left, or -1 if the due date has passed.
     */
    public long calculateDaysLeft(String dueDateString) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = LocalDate.parse(dueDateString);

        long daysLeft = ChronoUnit.DAYS.between(today, dueDate);

        if (daysLeft < 0) {
            System.out.println("The due date has already passed.");
            return -1; // Indicates the due date has passed
        }

        System.out.printf("Days left until '%s': %d days\n", dueDateString, daysLeft);
        return daysLeft;
    }

    /**
     * Calculates the weekly hours required to meet a goal within the due date.
     *
     * @param totalHoursNeeded Total hours needed to complete the goal.
     * @param dueDateString    The due date in the format "yyyy-MM-dd".
     * @return Weekly hours required to meet the goal, or -1 if the due date has passed.
     */
    public double calculateWeeklyHourGoal(int totalHoursNeeded, String dueDateString) {
        long daysLeft = calculateDaysLeft(dueDateString);

        if (daysLeft <= 0) {
            System.out.println("Cannot calculate weekly hours. The due date has already passed.");
            return -1;
        }

        // Calculate weeks remaining
        double weeksRemaining = daysLeft / 7.0;

        // Calculate weekly hours required
        double weeklyHours = totalHoursNeeded / weeksRemaining;

        System.out.printf("Weekly hours required to complete the goal by '%s': %.2f hours/week\n", dueDateString, weeklyHours);
        return weeklyHours;
    }

    /**
     * Calculates the total number of weeks between two dates.
     *
     * @param dueDate   The due date in the format "yyyy-MM-dd".
     * @param startDate The start date in the format "yyyy-MM-dd".
     * @return The total number of weeks between the two dates, or -1 if there's an error in date parsing.
     */
    public static long calculateTotalWeeksBetweenDates(String dueDate, String startDate) {
        try {
            // Parse the input dates
            LocalDate dueDateParsed = LocalDate.parse(dueDate);
            LocalDate startDateParsed = LocalDate.parse(startDate);

            // Calculate the total number of days between the two dates
            long totalDays = ChronoUnit.DAYS.between(startDateParsed, dueDateParsed);

            // Convert days to weeks
            long totalWeeks = totalDays / 7;

            return totalWeeks;
        } catch (Exception e) {
            System.out.println("Error parsing dates: " + e.getMessage());
            return -1; // Return -1 in case of an error
        }
    }

    /**
     * Calculates the total logged time for a specific user and goal, aggregating the hours and minutes.
     *
     * @param username The username of the user.
     * @param goalName The name of the goal.
     * @return An array with total hours [0] and total minutes [1] spent by the user on the specified goal.
     */
    public int[] calculateLoggedTime(String username, String goalName) {
        CsvEditor.ensureDirectoryExists(LOGGED_TIME_FILE);

        int totalHours = 0;
        int totalMinutes = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(LOGGED_TIME_FILE))) {
            String line = reader.readLine(); // Skip header (if there is one)
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 5 && columns[0].equals(username) && columns[1].equals(goalName)) {
                    // Parse logged time hours and minutes
                    int loggedHours = Integer.parseInt(columns[3].trim());
                    int loggedMinutes = Integer.parseInt(columns[4].trim());

                    // Accumulate time
                    totalHours += loggedHours;
                    totalMinutes += loggedMinutes;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert minutes to hours if totalMinutes >= 60
        totalHours += totalMinutes / 60;
        totalMinutes = totalMinutes % 60;

        // Return total hours and minutes as an array
        return new int[]{totalHours, totalMinutes};
    }
}