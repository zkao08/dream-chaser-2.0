package Backend;

public class Session {
    private static Session instance;
    private String currentUser;

    private Session() {
        // Private constructor to prevent instantiation
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCurrentUser(String user) {
        this.currentUser = user;
    }

    public String getCurrentUser() {
        return this.currentUser; // Return String, not User
    }

    public void clearSession() {
        this.currentUser = null;
    }
}
