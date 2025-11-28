package ui;

import javax.swing.*;
import controllers.*;
import models.*;

public class RegistrationGUI {

    private JFrame frame;
    private MainController mainController;

    public RegistrationGUI(MainController controller) {
        this.mainController = controller;
    }

    public void showFrame() {
        frame = new JFrame("TMU Ride-Share App - Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);

        // --- Labels and Fields ---
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

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(50, 180, 100, 30);
        JButton backButton = new JButton("Back");
        backButton.setBounds(200, 180, 100, 30);

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(typeLabel);
        frame.add(typeBox);
        frame.add(registerButton);
        frame.add(backButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // --- Register Action ---
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String type = (String) typeBox.getSelectedItem();

            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields!");
                return;
            }

            User newUser;
            if (type.equals("Rider")) {
                newUser = new Rider();
                ((Rider)newUser).setWallet(new Wallet());
            } else {
                newUser = new Driver();
            }

            newUser.setName(name);
            newUser.setPassword(password);

            // Add user to database
            Database.users.put(newUser.getId(), newUser);

            JOptionPane.showMessageDialog(frame, "Registration successful!");
            frame.dispose();

            // Open login GUI again
            new RideGUI().init();
        });

        // --- Back Button ---
        backButton.addActionListener(e -> {
            frame.dispose();
            new RideGUI().init();
        });
    }
}

