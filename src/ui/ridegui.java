package ui;

import javax.swing.*;
import controllers.*;
import models.*;
import java.awt.*;

public class RideGUI {

    private JFrame frame;
    private MainController mainController;
    private JTextArea outputArea; // for displaying messages

    public void init() {
        this.mainController = new MainController();
        createLoginFrame();
    }

    private void createLoginFrame() {
        frame = new JFrame("TMU Ride-Share App - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);

        // ---------------- Labels and Inputs ----------------
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 80, 25);
        JTextField nameField = new JTextField();
        nameField.setBounds(150, 50, 150, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 90, 80, 25);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 150, 25);

        JLabel typeLabel = new JLabel("User Type:");
        typeLabel.setBounds(50, 130, 80, 25);
        String[] types = {"Rider", "Driver"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setBounds(150, 130, 150, 25);

        // ---------------- Buttons ----------------
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 180, 100, 30);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(200, 180, 100, 30);

        // ---------------- Output Area ----------------
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(50, 220, 300, 120);

        // ---------------- Add components ----------------
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(typeLabel);
        frame.add(typeBox);
        frame.add(loginButton);
        frame.add(registerButton);
        frame.add(scrollPane);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // --- Login Action ---
        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String type = (String) typeBox.getSelectedItem();

            User user = mainController.loginController.verifyLoginByName(name, password, type);
            if (user != null) {
                displayText("Login successful: " + user.getName());
                frame.dispose(); // close login
                if (user instanceof Rider rider) {
                    new RiderGUI(rider, mainController).showFrame();
                } else if (user instanceof Driver driver) {
                    new DriverGUI(driver, mainController).showFrame();
                }
            } else {
                displayText("Login failed for user: " + name);
                JOptionPane.showMessageDialog(frame, "Login failed!");
            }
        });

        // --- Registration Action ---
        registerButton.addActionListener(e -> {
            displayText("Opening Registration...");
            new RegistrationGUI(mainController).showFrame();
            frame.dispose();
        });
    }

    // ---------------- Display methods for other views ----------------
    public void displayText(String msg) {
        if (outputArea != null) {
            outputArea.append(msg + "\n");
        }
    }

    public void displayText(String msg, boolean clearFirst) {
        if (outputArea != null) {
            if (clearFirst) outputArea.setText("");
            outputArea.append(msg + "\n");
        }
    }

    public void clearDisplay() {
        if (outputArea != null) outputArea.setText("");
    }
}
