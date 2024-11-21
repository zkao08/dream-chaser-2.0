package Backend;

/**
 * <h1>TestStatisticsService Class</h1>
 * The TestStatisticsService class is designed to test the functionality of
 * the StatisticsService class. It includes multiple test methods to verify
 * different calculations such as task completion percentage, total time to
 * complete goals, the number of days left until a due date, and weekly hour
 * goals to meet a specific deadline.
 *
 * Usage:
 * Each test method calls the respective method from the StatisticsService
 * class and prints the results to the console to verify correctness.
 *
 * @author Max Henson
 * @version 2.0
 * @since 11/20/2024
 * @package Backend
 */

public class TestStatisticsService {

    /**
     * Main method that runs all the test functions to verify the functionality
     * of the StatisticsService methods. Each method checks a different calculation
     * related to task completion, time management, or goal tracking.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Run all test functions
        testCalculateTaskCompletionPercentage();
        testCalculateTotalTimeToComplete();
        testCalculateDaysLeft();
        testCalculateWeeklyHourGoal();
    }

    /**
     * Tests the calculation of task completion percentage for a given goal.
     * It calls the calculateTaskCompletionPercentage method from the
     * StatisticsService and prints the result.
     */
    public static void testCalculateTaskCompletionPercentage() {
        System.out.println("\n--- Test: Calculate Task Completion Percentage ---");
        StatisticsService service = new StatisticsService();

        // Test data
        String username = "joy";
        String goalName = "Learn Html";

        double completionPercentage = service.calculateTaskCompletionPercentage(username, goalName);

        System.out.printf("Completion Percentage for goal '%s': %.2f%%\n", goalName, completionPercentage);
    }

    /**
     * Tests the calculation of the total time required to complete a goal.
     * It calls the calculateTotalTimeToComplete method from the StatisticsService
     * and prints the total hours and minutes required.
     */
    public static void testCalculateTotalTimeToComplete() {
        System.out.println("\n--- Test: Calculate Total Time to Complete ---");
        StatisticsService service = new StatisticsService();

        // Test data
        String username = "joy";
        String goalName = "Learn Html";

        int[] totalTime = service.calculateTotalTimeToComplete(username, goalName);
        System.out.printf("Total time to complete '%s': %d hours and %d minutes\n",
                goalName, totalTime[0], totalTime[1]);
    }

    /**
     * Tests the calculation of the number of days left until a specific due date.
     * It calls the calculateDaysLeft method from the StatisticsService and
     * prints the number of days remaining.
     */
    public static void testCalculateDaysLeft() {
        System.out.println("\n--- Test: Calculate Days Left ---");
        StatisticsService service = new StatisticsService();

        // Test data
        String dueDateString = "2025-01-16";

        long daysLeft = service.calculateDaysLeft(dueDateString);
        System.out.printf("Days left until '%s': %d days\n", dueDateString, daysLeft);
    }

    /**
     * Tests the calculation of the weekly hour goal required to meet a due date.
     * It calls the calculateWeeklyHourGoal method from the StatisticsService
     * and prints the weekly hours needed to meet the goal.
     */
    public static void testCalculateWeeklyHourGoal() {
        System.out.println("\n--- Test: Calculate Weekly Hour Goal ---");
        StatisticsService service = new StatisticsService();

        // Test data
        int totalHoursNeeded = 32; // Example total hours needed
        String dueDateString = "2025-01-16";

        double weeklyHours = service.calculateWeeklyHourGoal(totalHoursNeeded, dueDateString);
        System.out.printf("Weekly hours required to meet goal by '%s': %.2f hours/week\n",
                dueDateString, weeklyHours);
    }
}
