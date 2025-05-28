package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import gui.DBConnection;
import gui.MainFrame;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckBox;
    private JButton loginButton, forgotPasswordButton, registerButton;

    public LoginFrame() {
        setTitle("Login - SimpleNote");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 
        		40, 20, 40));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        rememberMeCheckBox = new JCheckBox("Remember me");

        loginButton = new JButton("Login");
        forgotPasswordButton = new JButton("Forgot password?");
        registerButton = new JButton("Register");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(rememberMeCheckBox);
        panel.add(loginButton);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(forgotPasswordButton);
        bottomPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadRememberedCredentials();

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter email and password!");
                return;
            }

            if (rememberMeCheckBox.isSelected()) {
                saveCredentials(email, password);
            } else {
                new File("remember.txt").delete();
            }

            int userId = loginFromDatabase(email, password);
            if (userId != -1) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose(); // Close LoginFrame
                new MainFrame(userId).setVisible(true); // Open MainFrame
            } else {
                JOptionPane.showMessageDialog(this, "Wrong email or password!");
            }
        });

        forgotPasswordButton.addActionListener(e -> {
            String email = JOptionPane.showInputDialog(this, "Enter your email:");
            if (email != null && !email.trim().isEmpty()) {
                String foundPassword = findPasswordByEmail(email.trim());
                if (foundPassword != null) {
                    JOptionPane.showMessageDialog(this, "Your password is: " + foundPassword);
                } else {
                    JOptionPane.showMessageDialog(this, "Email not found!");
                }
            }
        });

        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }

    private void saveCredentials(String email, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("remember.txt"))) {
            writer.write(email + "\n" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRememberedCredentials() {
        File file = new File("remember.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String email = reader.readLine();
                String password = reader.readLine();
                emailField.setText(email);
                passwordField.setText(password);
                rememberMeCheckBox.setSelected(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int loginFromDatabase(String email, String password) {
        String sql = "SELECT id FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error!");
        }
        return -1;
    }

    private String findPasswordByEmail(String email) {
        String sql = "SELECT password FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error!");
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
