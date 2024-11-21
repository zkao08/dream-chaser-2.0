package Frontend;

import Backend.CsvEditor;
import Backend.User;



import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;





public class SignInScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private boolean passwordChecker;
    private DreamChaserApp app; // Reference to the app
    private static final String USERS_FILE = "UserData/users.csv";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private Image backgroundImage; // Background image

    public SignInScreen(CardLayout cardLayout, JPanel mainPanel, DreamChaserApp app) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.app = app;

        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/Images/AllPageBackground.png")).getImage();

        setLayout(new GridBagLayout());
        setOpaque(false); // Make panel transparent to show background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Larger spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 55)); // Larger title font
        titleLabel.setForeground(Color.decode("#021f37")); // Navy blue font color
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font for label
        usernameLabel.setForeground(Color.decode("#021f37")); // Navy blue font color
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(usernameLabel, gbc);

        // Username Input
        usernameField = createStyledTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font for label
        passwordLabel.setForeground(Color.decode("#021f37")); // Navy blue font color
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        // Password Input
        passwordField = createStyledPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        // Show Password Checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 20)); // Larger font for checkbox
        showPasswordCheckBox.setForeground(Color.decode("#021f37")); // Navy blue font color
        showPasswordCheckBox.setOpaque(false); // Transparent checkbox
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        add(showPasswordCheckBox, gbc);

        // Sign In Button
        JButton signInButton = createStyledButton("Sign In");
        signInButton.addActionListener(e -> handleSignIn());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(signInButton, gbc);

        // Sign Up Button
        JButton signUpButton = createStyledButton("Don't have an account? Sign Up");
        signUpButton.addActionListener(e -> cardLayout.show(mainPanel, "SignUp"));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(signUpButton, gbc);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 24)); // Larger font for input
        textField.setForeground(Color.decode("#021f37")); // Navy blue font color
        textField.setOpaque(false); // Transparent background
        textField.setBorder(BorderFactory.createLineBorder(Color.decode("#021f37"), 3, true)); // Navy blue rounded border
        textField.setPreferredSize(new Dimension(400, 50)); // Larger input size
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 24)); // Larger font for input
        passwordField.setForeground(Color.decode("#021f37")); // Navy blue font color
        passwordField.setOpaque(false); // Transparent background
        passwordField.setBorder(BorderFactory.createLineBorder(Color.decode("#021f37"), 3, true)); // Navy blue rounded border
        passwordField.setPreferredSize(new Dimension(400, 50)); // Larger input size
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw button background
                g2.setColor(Color.decode("#021f37")); // Navy blue background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded edges

                // Draw button text
                g2.setColor(Color.WHITE); // White font color
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(400, 60)); // Larger button size
        button.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font
        return button;
    }


    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
        }
    }
    public static boolean matchUsernameAndPassword(String inputUsername, String inputPassword) {
        CsvEditor.ensureDirectoryExists(USERS_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 2) {
                    String username = columns[0].trim();
                    String password = columns[1].trim();

                    // Check if the input username and password match
                    if (username.equals(inputUsername) && password.equals(inputPassword)) {
                        return true; // Match found
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // No match found
    }

    private void handleSignIn() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Check if either field is empty
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate username and password
        passwordChecker = matchUsernameAndPassword(username, password);

        if (passwordChecker) {
            // Successful login
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
//            TrackCurrentUser.setCurrentUser(username); // Set the current user
            app.setCurrentUser(new User(username)); // Set the current user in the app
            app.initializeScreens(); // Initialize other screens dynamically
            handleScreenTransition(); // Transition to the next screen
        } else {
            // Incorrect username or password
            JOptionPane.showMessageDialog(this, "Incorrect username or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleScreenTransition() {
        // Show the LoadingScreen immediately
        cardLayout.show(mainPanel, "Loading");

        // Use SwingWorker to perform initialization in the background
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                // Simulate a loading process (e.g., initialize resources)
                Thread.sleep(3000); // Show LoadingScreen for 3 seconds
                return null;
            }

            @Override
            protected void done() {
                // After the loading is complete, show the ProgressReport screen
                app.navigateToScreen("ProgressReport");
            }
        };

        worker.execute(); // Start the background task
    }

}
