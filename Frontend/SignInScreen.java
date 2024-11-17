package Frontend;

import Backend.CsvEditor;
import Backend.User;
import Backend.Session;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SignInScreen extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    public SignInScreen(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(usernameLabel, gbc);

        usernameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        add(showPasswordCheckBox, gbc);

        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.BOLD, 14));
        signInButton.setBackground(new Color(0, 123, 255));
        signInButton.setForeground(Color.WHITE);
        signInButton.setFocusPainted(false);
        signInButton.addActionListener(e -> handleSignIn());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(signInButton, gbc);

        JButton signUpButton = new JButton("Don't have an account? Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setBackground(new Color(220, 53, 69));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.addActionListener(e -> cardLayout.show(mainPanel, "SignUp"));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(signUpButton, gbc);
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
        }
    }

    private void handleSignIn() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<User> users = CsvEditor.readUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    Session.getInstance().setCurrentUser(username);
                    JOptionPane.showMessageDialog(this, "Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    handleScreenTransition();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Username not found. Please try again or sign up.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void handleScreenTransition() {
        cardLayout.show(mainPanel, "Loading");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                Thread.sleep(2000);
                return null;
            }

            @Override
            protected void done() {
                cardLayout.show(mainPanel, "ProgressReport");
            }
        };

        worker.execute();
    }
}
