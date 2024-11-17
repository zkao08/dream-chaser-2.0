//package Backend;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.logging.Logger;
//
///**
// * StatisticsFileManager.java Created By: Max Henson Date Created: 10/03/2024
// * Version: 1.0
// *
// * Description: The StatisticsFileManager class handles the reading and writing
// * of user statistics to and from JSON files..
// *
// * Usage: 1. Create an instance of StatisticsFileManager. 2. Use the
// * writeStatisticsToJson(String userID, Statistics statistics) method to save a
// * Statistics object to a JSON file. 3. Use the readStatisticsFromJson(String
// * userID) method to retrieve a Statistics object from a JSON file.
// *
// * Change Log: Version 1.0 (10/03/2024): - Initial implementation with methods
// * to write and read statistics from JSON files. - Placeholder methods for JSON
// * conversion and file I/O operations. Version 1.1 (10/30/2024): - Overhaul of
// * code - Placeholders replaced with methods - Updated implementation of reading
// * and writing methods
// */
///**
// * Manages the reading and writing of user statistics to and from the JSON file.
// * This class interacts with jsonUtils for JSON operations and handles input
// * validation and logging.
// */
//public class StatisticsFileManager {
//
//    private static final Logger logger = Logger.getLogger(StatisticsFileManager.class.getName());  // Logger for error and info handling
//
//    /**
//     * Creates default statistics for a new user in the system.
//     *
//     * @param username The username for which to create the default statistics.
//     * @return ObjectNode containing default user statistics.
//     */
//    private static ObjectNode createDefaultStatistics(String username) {
//        // Create a new ObjectNode for storing default statistics
//        ObjectNode defaultStats = JsonUtils.objectMapper.createObjectNode();
//        defaultStats.put("totalTimeSpent", 0);
//        defaultStats.put("totalTasks", 0);
//        defaultStats.put("completedTasks", 0);
//        defaultStats.put("completionPercentage", 0.0);
//        defaultStats.put("lastUpdated", java.time.LocalDateTime.now().toString());
//
//        logger.info("Created default statistics for new user: " + username);
//        return defaultStats;
//    }
//
//    /**
//     * Saves a user's statistics data to the JSON file. If the user already
//     * exists, their data is updated. Otherwise, a new entry is created. This
//     * method includes input validation to ensure data integrity.
//     *
//     * @param username The username of the user.
//     * @param totalTimeSpent The total time spent by the user.
//     * @param totalTasks The total number of tasks the user has.
//     * @param completedTasks The number of tasks completed by the user.
//     * @param completionPercentage The percentage of tasks completed.
//     */
//    public static void saveUserStatistics(String username, int totalTimeSpent, int totalTasks, int completedTasks, double completionPercentage) {
//        try {
//            // Input validation to ensure valid statistics data
//            if (totalTimeSpent < 0 || totalTasks < 0 || completedTasks < 0 || completedTasks > totalTasks || completionPercentage < 0 || completionPercentage > 100) {
//                throw new IllegalArgumentException("Invalid statistics data provided for user: " + username);
//            }
//
//            // Create a new ObjectNode to store the user's statistics
//            ObjectNode userStats = JsonUtils.objectMapper.createObjectNode();
//            userStats.put("totalTimeSpent", totalTimeSpent);
//            userStats.put("totalTasks", totalTasks);
//            userStats.put("completedTasks", completedTasks);
//            userStats.put("completionPercentage", completionPercentage);
//
//            // Add the last updated timestamp
//            userStats.put("lastUpdated", java.time.LocalDateTime.now().toString());
//
//            // Update the JSON file with the user's statistics
//            JsonUtils.updateUserData(username, userStats);
//
//            logger.info("User statistics saved successfully for user: " + username);
//
//        } catch (IllegalArgumentException e) {
//            logger.severe("Error saving user statistics: " + e.getMessage());
//        } catch (IOException e) {
//            logger.severe("I/O error saving user statistics: " + e.getMessage());
//        } catch (Exception e) {
//            logger.severe("Unexpected error: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Retrieves a user's statistics from the JSON file. If the user does not
//     * exist, a new entry is created with default values.
//     *
//     * @param username The username of the user.
//     * @return A JsonNode containing the user's statistics, or null if an error
//     * occurs.
//     */
//    public static JsonNode getUserStatistics(String username) {
//        try {
//            // Retrieve the user's statistics from the JSON file
//            JsonNode userStats = JsonUtils.getUserData(username);
//
//            // If the user doesn't exist, automatically create a new entry with default statistics
//            if (userStats == null) {
//                logger.info("User not found, creating a new entry for user: " + username);
//                ObjectNode defaultStats = createDefaultStatistics(username);
//                JsonUtils.updateUserData(username, defaultStats);
//                return defaultStats;  // Return the newly created default stats
//            }
//
//            return userStats;
//
//        } catch (IOException e) {
//            logger.severe("Error retrieving user statistics: " + e.getMessage());
//            return null;
//        }
//    }
//}	// End of StatisticsFileManager class