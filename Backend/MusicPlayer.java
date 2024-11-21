package Backend;

/**
 * <h1>MusicPlayer Class</h1>
 * The MusicPlayer class provides functionality to play music tracks from a predefined library.
 * It supports starting and stopping music playback, and listing available songs.
 * The music is played in a separate thread to avoid blocking the main thread.
 *
 * @author Max Henson
 * @version 1.0
 * @since 11/20/2024
 * @package Backend
 *
 * <p>Usage:
 * This class allows users to select a song by name, play it, and stop playback when necessary.
 * It manages a library of songs and ensures smooth music playback in a background thread.</p>
 *
 */

import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class MusicPlayer {
    private Player player;
    private Thread musicThread;
    private final Map<String, String> songs;

    /**
     * Constructor for the MusicPlayer class. Initializes the song library with song names and file paths.
     */
    public MusicPlayer() {
        songs = new LinkedHashMap<>();
        // Add songs to the library (name, resource path)
        songs.put("Study Vibes", "/Music/747599__viramiller__gentle-tracks-for-relaxing-and-enjoying-natures-beauty.mp3");
        songs.put("Focus Beats", "/Music/763418__lolamoore__calm-piano-melodies-for-relaxing-times.mp3");
        songs.put("Calm Ambient", "/Music/767570__lolamoore__lo-fi-chill-for-reflective-moments.mp3");
        songs.put("Relaxing Piano", "/Music/768519__lolamoore__soothing-piano-moments.mp3");
    }

    /**
     * This method plays the selected music file by its name.
     * @param songName This is the name of the song to play.
     */
    public void playMusic(String songName) {
        if (!songs.containsKey(songName)) {
            System.out.println("Song not found in the library.");
            return;
        }

        String resourcePath = songs.get(songName); // Retrieve the resource path
        stopMusic(); // Stop any currently playing music

        musicThread = new Thread(() -> {
            try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Resource not found: " + resourcePath);
                }

                player = new Player(inputStream); // Pass InputStream to the Player
                player.play(); // Blocks until the song finishes
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        musicThread.start();
    }

    /**
     * This method stops the music if it's currently playing.
     */
    public void stopMusic() {
        if (player != null) {
            player.close();
            player = null;
        }
        if (musicThread != null && musicThread.isAlive()) {
            musicThread.interrupt();
            musicThread = null;
        }
    }

    /**
     * This method returns a map of all available songs in the library.
     * @return Map<String, String> This returns a map where the keys are song names
     *         and the values are file paths to the songs.
     */
    public Map<String, String> getSongs() {
        return songs;
    }
}
