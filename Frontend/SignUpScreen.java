package Frontend;

import Backend.CsvEditor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SignUpScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckBox;
    private Image backgroundImage; // Background image

    public SignUpScreen(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/Images/AllPageBackground.png")).getImage();

        setLayout(new GridBagLayout());
        setOpaque(false); // Make panel transparent to show background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Larger spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Larger title font
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

        // Confirm Password Label
        JLabel confirmPasswordLabel = new JLabel("Re-enter Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font for label
        confirmPasswordLabel.setForeground(Color.decode("#021f37")); // Navy blue font color
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(confirmPasswordLabel, gbc);

        // Confirm Password Input
        confirmPasswordField = createStyledPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(confirmPasswordField, gbc);

        // Show Password Checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 20)); // Larger font for checkbox
        showPasswordCheckBox.setForeground(Color.decode("#021f37")); // Navy blue font color
        showPasswordCheckBox.setOpaque(false); // Transparent checkbox
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        add(showPasswordCheckBox, gbc);

        // Sign Up Button
        JButton signUpButton = createStyledButton("Sign Up");
        signUpButton.addActionListener(e -> handleSignUp());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(signUpButton, gbc);

        // Back Button
        JButton backButton = createStyledButton("Back to Sign In");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "SignIn"));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(backButton, gbc);
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
            confirmPasswordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
            confirmPasswordField.setEchoChar('*');
        }
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.length() < 3 || username.length() > 20) {
            JOptionPane.showMessageDialog(this, "Username must be between 3 and 20 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Password must be 8–20 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> usernames = CsvEditor.readUsernames(); // Fetch all existing usernames

        if (usernames.contains(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CsvEditor.writeUser(username, password);

        JOptionPane.showMessageDialog(this, "Sign Up Successful! Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(mainPanel, "SignIn");
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }
        boolean hasUppercase = false, hasLowercase = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }
}
