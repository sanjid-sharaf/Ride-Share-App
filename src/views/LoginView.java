package views;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

    public JTextField emailField;
    public JPasswordField passwordField;
    public JButton loginButton;

    public LoginView() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("RideShare Login");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(18);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        add(loginButton, gbc);
    }

    // Optional: GUI version of "login result"
    public void displayLoginResult(boolean success) {
        JOptionPane.showMessageDialog(
            this,
            success ? "Login successful!" : "Login failed. Try again.",
            "Login",
            success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
    }

    // Optional: GUI version of "show user details"
    public void printUserDetails(models.User user) {
        JOptionPane.showMessageDialog(
            this,
            "User Profile:\n" +
            "ID: " + user.getId() + "\n" +
            "Name: " + user.getName() + "\n" +
            "Email: " + user.getEmail() + "\n" +
            "Phone: " + user.getPhoneNumber() + "\n" +
            "Address: " + user.getAddress(),
            "User Info",
            JOptionPane.PLAIN_MESSAGE
        );
    }
}
