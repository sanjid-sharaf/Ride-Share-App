package ui;

import controllers.MainController;

import javax.swing.*;
import java.awt.*;

public class LoginGUI {

    private MainController mainController;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea outputArea;

    public LoginGUI(MainController mainController) {
        this.mainController = mainController;
    }

    public void showFrame() {
        frame = new JFrame("Login");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 80, 25);
        frame.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 25);
        frame.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 80, 25);
        frame.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 25);
        frame.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 150, 100, 30);
        loginBtn.addActionListener(e -> attemptLogin());
        frame.add(loginBtn);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBounds(50, 200, 300, 50);
        frame.add(scroll);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        mainController.loginController.verifyLogin(username, password);
    }

    public void displayText(String msg) {
        if (outputArea != null) outputArea.append(msg + "\n");
    }

    public void close() {
        if (frame != null) frame.dispose();
    }
}
