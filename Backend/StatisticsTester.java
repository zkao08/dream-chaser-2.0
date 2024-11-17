//package Backend;
//
//import com.fasterxml.jackson.databind.JsonNode;
//
///**
// * StatisticsTester.java
// * Created By: Max Henson
// * Date Created: 10/30/2024
// * Version: 1.0
// *
// * Description: This class is responsible for testing the functionality of the
// * StatisticsFileManager and StatisticsService classes. It includes tests for
// * creating, updating, retrieving, and saving user statistics.
// *
// * Usage:
// * - Run the main method to execute the tests on the user statistics functionality.
// *
// * Change Log:
// * Version 1.0 (10/30/2024): Initial version created for testing user statistics.
// * Version 2.0 (11/16/2024): Updated code to test all important functions, added commenting & header.
// */
//public class StatisticsTester {
//
//    /**
//     * Main.java method to execute all the tests.
//     * It runs individual test methods for creating, updating, retrieving,
//     * and saving user statistics.
//     */
//    public static void main(String[] args) {
//        System.out.println("Starting tests...\n");
//
//        // Run all the test methods
//        testCreateNewUser();
//        testUpdateUserStatistics();
//        testRetrieveUserStatistics();
//        testSaveUserStatistics();
//
//        System.out.println("\nAll tests completed.");
//    }
//
//    /**
//     * Test the creation of a new user and the generation of default statistics.
//     * It checks if the default statistics are created correctly when a new user is added.
//     */
//    public static void testCreateNewUser() {
//        System.out.println("Running: testCreateNewUser");
//        String username = "testUser1";
//
//        // Try to retrieve statistics for the new user
//        JsonNode userStats = StatisticsFileManager.getUserStatistics(username);
//
//        // Check if the statistics for the user exist
//        if (userStats != null) {
//            System.out.println("testCreateNewUser: SUCCESS - Default statistics created for user " + username);
//        } else {
//            System.out.println("testCreateNewUser: FAILURE - Could not create default statistics for user " + username);
//        }
//    }
//
//    /**
//     * Test the updating of user statistics.
//     * It verifies that the user statistics are correctly updated when new data is provided.
//     */
//    public static void testUpdateUserStatistics() {
//        System.out.println("Running: testUpdateUserStatistics");
//        String username = "testUser1";
//        int totalTimeSpent = 5;
//        int totalTasks = 10;
//        int completedTasks = 7;
//
//        // Update the statistics for the user
//        StatisticsService.saveStatistics(username, totalTimeSpent, totalTasks, completedTasks);
//
//        // Retrieve the updated statistics
//        JsonNode userStats = StatisticsFileManager.getUserStatistics(username);
//
//        // Check if the statistics match the expected values
//        if (userStats != null &&
//                userStats.get("totalTimeSpent").asInt() == totalTimeSpent &&
//                userStats.get("totalTasks").asInt() == totalTasks &&
//                userStats.get("completedTasks").asInt() == completedTasks) {
//            System.out.println("testUpdateUserStatistics: SUCCESS - Statistics updated for user " + username);
//        } else {
//            System.out.println("testUpdateUserStatistics: FAILURE - Statistics not updated correctly for user " + username);
//        }
//    }
//
//    /**
//     * Test the retrieval of user statistics.
//     * It verifies that user statistics can be successfully retrieved.
//     */
//    public static void testRetrieveUserStatistics() {
//        System.out.println("Running: testRetrieveUserStatistics");
//        String username = "testUser1";
//
//        // Retrieve the statistics for the user
//        JsonNode userStats = StatisticsFileManager.getUserStatistics(username);
//
//        // Check if the statistics were found
//        if (userStats != null) {
//            System.out.println("testRetrieveUserStatistics: SUCCESS - Retrieved statistics for user " + username);
//        } else {
//            System.out.println("testRetrieveUserStatistics: FAILURE - No statistics found for user " + username);
//        }
//    }
//
//    /**
//     * Test saving user statistics.
//     * It checks if user statistics can be saved and written to the data store correctly.
//     */
//    public static void testSaveUserStatistics() {
//        System.out.println("Running: testSaveUserStatistics");
//        String username = "testUser1";
//        int totalTimeSpent = 10;
//        int totalTasks = 20;
//        int completedTasks = 15;
//        double completionPercentage = completedTasks / (double) totalTasks;
//
//        try {
//            // Save the statistics for the user
//            StatisticsFileManager.saveUserStatistics(username, totalTimeSpent, totalTasks, completedTasks, completionPercentage);
//            System.out.println("testSaveUserStatistics: SUCCESS - User statistics saved for user " + username);
//        } catch (Exception e) {
//            // Handle failure to save statistics
//            System.out.println("testSaveUserStatistics: FAILURE - Could not save statistics for user " + username);
//            e.printStackTrace(); // Optionally, print the exception for debugging purposes
//        }
//    }
//}   // End of StatisticsTester class