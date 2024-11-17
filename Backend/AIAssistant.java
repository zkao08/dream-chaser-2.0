package Backend;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.time.LocalDate;

/**
 * The AIAssistant class integrates with a generative AI API to provide users
 * with assistance in creating and managing goals.
 *
 * @Author: Zachary Kao
 * @Version: 2.0
 * @Since: 11/8/2024
 *
 * Usage:
 * This class allows users to leverage AI capabilities
 * for estimating task durations and generating task lists from goals.
 *
 * Change Log:
 * Version 1.0 (10/04/2024):
 * - Initial creation of the AIAssistant class.
 * Version 1.1 (10/20/2024):
 * - Import ChatGPT integration test code
 * Version 1.2 (10/30/2024):
 * - Format ChatGPT response as CSV
 * Version 1.3 (11/8/2024):
 * - Update to not use deprecated methods
 * - Create methods to prompt ChatGPT and parse the output to return tasks
 * Version 2.0 (11/17/2024):
 * - Update to accept different parameters for goals
 * - Returns a list of tasks instead of modifying a goal
 */
public class AIAssistant
{

    /**
     * Load ENV variables into system properties
     * Used to set the ChatGPT API key
     *
     * @param resourcePath : the filepath of the ..env file
     */
    public static void loadEnvFile(String resourcePath)
    {
        Properties properties = new Properties();
        try {
            // Load the .env file from resources
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);

            if (inputStream != null)
            {
                properties.load(inputStream);

                // Set each property as a system environment variable
                for (String key : properties.stringPropertyNames())
                {
                    String value = properties.getProperty(key);
                    System.setProperty(key, value);
                }

                inputStream.close();
            }
            else
            {
                System.out.println(".env file not found at " + resourcePath);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Send a prompt to ChatGPT and return a response
     *
     * @param prompt : the prompt to send
     * @return the response
     */
    public static String chatGPT(String prompt)
    {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = System.getProperty("OPENAI_API_KEY");
        String model = "gpt-3.5-turbo";

        try
        {
            //wrap prompt in request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";

            //create HttpClient to handle requests and responses
            HttpClient client = HttpClient.newHttpClient();

            //create HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            //send request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //extract and return message from JSON response
            return extractMessageFromJSONResponse(response.body());

        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extract the response as a string from the JSON returned by ChatGPT
     *
     * @param response the resposne from ChatGPT as a JSON string
     * @return the response as text string
     */
    public static String extractMessageFromJSONResponse(String response)
    {
        //find the start of the content in JSON and skip the header
        int start = response.indexOf("content")+ 11;

        //find the end of the response
        int end = response.indexOf("\"", start);

        //extract the message
        String extractedMessage = response.substring(start, end);

        //replace newlines for formatting and return
        return extractedMessage.replace("\\n", "\n");

    }

    /**
     * Helper function to parse the CSV returned by a ChatGPT response
     * and turn it into a list of tasks
     *
     * @param csv : the CSV output from ChatGPT
     * @return a List of tasks
     */
    public static ArrayList<Task> parseCSV(String csv)
    {
        System.out.println("Parsing CSV: " + csv); // Debugging line

        //create a list to store the output
        ArrayList<Task> tasks = new ArrayList<Task>();

        //split the CSV by lines
        String[] lines = csv.split("\n");

        //start at 1 to skip the header
        for(int i = 1; i < lines.length; i++)
        {
            //get the line and trim any potential unnecessary whitespace
            String line = lines[i].trim();

            //skip line if empty
            if(line.isEmpty())
                continue;

            //split the line by commas
            String[] fields = line.split(",");
            if (fields.length != 3) {
                System.err.println("Malformed line: " + line); // Debugging line
                continue;
            }

            try {
                String taskName = fields[0].trim();
                int hours = Integer.parseInt(fields[1].trim());
                int minutes = Integer.parseInt(fields[2].trim());
                tasks.add(new Task(taskName, hours, minutes));
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format in line: " + line); // Debugging line
            }
        }

        return tasks;
    }

    //Static methods for getting AI assistance

    /**
     * A method to prompt the AI to break down a goal into a list of tasks
     * Directly modifies the goal object
     *
     * @param goal : the goal to be edited by the AI
     * @return List<Task> : a list of tasks
     */
    public static ArrayList<Task> getTasksAI(String goal, LocalDate dueDate)
    {
        System.out.println("Due Date:" + dueDate);
        System.out.println("Goal Name:" + goal);
        //do not process request if goal has no name or due date
        if(goal == "" || dueDate == null)
        {
            return null;
        }

        //send a prompt to get tasks for the goal name to ChatGPT
        String response = chatGPT("Give me a list of tasks to break down the goal '" + goal + "' "
                + "starting " + LocalDate.now() + " and ending " + dueDate + " into smaller achievable tasks. "
                + "Format the output in a csv as follows, which each task on a new line: 'task name, hours to complete task, minutes to complete task' "
                + "The hours and minutes should add up to the total time estimated to complete the task. "
                + "The required time to complete all tasks should not exceed the time until the due date. "
                + "The tasks should not be numbered and the first line should be the format specification. "
                + "The tasks should be simple, achievable through self-study, and should not require professional instruction, public performance, or additional resources. "
                + "Ensure task names are short, simple, and do not contain commas or special punctuation.");
        System.out.println("Response:" + response);
        //parse the response into a list of tasks
        ArrayList<Task> tasks = new ArrayList<Task>();
        tasks = parseCSV(response);


        return tasks;
    }
}