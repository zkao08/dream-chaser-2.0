package Backend;

import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class MusicPlayer {
    private Player player;
    private Thread musicThread;
    private final Map<String, String> songs;

    public MusicPlayer() {
        songs = new LinkedHashMap<>();
        // Add songs to the library (name, file path)
        songs.put("Study Vibes", "resources/Music/747599__viramiller__gentle-tracks-for-relaxing-and-enjoying-natures-beauty.mp3");
        songs.put("Focus Beats", "resources/Music/763418__lolamoore__calm-piano-melodies-for-relaxing-times.mp3");
        songs.put("Calm Ambient", "resources/Music/767570__lolamoore__lo-fi-chill-for-reflective-moments.mp3");
        songs.put("Relaxing Piano", "resources/Music/768519__lolamoore__soothing-piano-moments.mp3");
    }

    // Play the selected music file by its name
    public void playMusic(String songName) {
        if (!songs.containsKey(songName)) {
            System.out.println("Song not found in the library.");
            return;
        }

        String filePath = songs.get(songName);
        stopMusic(); // Stop any currently playing music

        musicThread = new Thread(() -> {
            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                player = new Player(fileInputStream);
                player.play(); // Blocks until the song finishes
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        musicThread.start();
    }

    // Stop the music
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

    // List all available songs in the library
    public Map<String, String> getSongs() {
        return songs;
    }
}
