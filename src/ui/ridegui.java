package ui;

import javax.swing.*;
import controllers.*;
import models.*;

public class RideGUI {

    private JFrame frame;
    private MainController mainController;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RideGUI().init());
    }

    public void init() {
        mainController = new MainController();
        createLoginFrame();
    }

    private void createLoginFrame() {
        frame = new JFrame("TMU Ride-Share App - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);

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

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 180, 100, 30);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(200, 180, 100, 30);

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(typeLabel);
        frame.add(typeBox);
        frame.add(loginButton);
        frame.add(registerButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // --- Login Action ---
        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String type = (String) typeBox.getSelectedItem();

            User user = mainController.loginController.verifyLoginByName(name, password, type);
            if (user != null) {
                frame.dispose(); // close login
                if (user instanceof Rider rider) {
                    new RiderGUI(rider, mainController).showFrame();
                } else if (user instanceof Driver driver) {
                    new DriverGUI(driver, mainController).showFrame();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed!");
            }
        });

        // --- Registration Action ---
        registerButton.addActionListener(e -> {
            new RegistrationGUI(mainController).showFrame();
            frame.dispose();
        });
    }
}
