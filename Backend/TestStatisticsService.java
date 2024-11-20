package Backend;

public class TestStatisticsService {
    public static void main(String[] args) {
        // Run all test functions
        testCalculateTaskCompletionPercentage();
        testCalculateTotalTimeToComplete();
        testCalculateDaysLeft();
        testCalculateWeeklyHourGoal();
    }

    public static void testCalculateTaskCompletionPercentage() {
        System.out.println("\n--- Test: Calculate Task Completion Percentage ---");
        StatisticsService service = new StatisticsService();

        // Test data
        String username = "joy";
        String goalName = "Learn Html";

        double completionPercentage = service.calculateTaskCompletionPercentage(username, goalName);

        System.out.printf("Completion Percentage for goal '%s': %.2f%%\n", goalName, completionPercentage);
    }

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

    public static void testCalculateDaysLeft() {
        System.out.println("\n--- Test: Calculate Days Left ---");
        StatisticsService service = new StatisticsService();

        // Test data
        String dueDateString = "2025-01-16";

        long daysLeft = service.calculateDaysLeft(dueDateString);
        System.out.printf("Days left until '%s': %d days\n", dueDateString, daysLeft);
    }

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
