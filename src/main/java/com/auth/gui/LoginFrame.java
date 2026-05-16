package com.auth.gui;
import java.awt.*;
import com.auth.service.AuthService;
import javax.swing.*;
import com.auth.session.SessionManager;

public class LoginFrame extends JFrame {

    private AuthService authService = new AuthService();

    public LoginFrame() {

        setTitle("BangKo Login");
        setSize(300, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon originalIcon = new ImageIcon(
                getClass().getResource("/logo.png")
        );

        Image scaledImage = originalIcon.getImage()
                .getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        ImageIcon logoIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(logoIcon);
        int frameWidth = 300;
        int logoWidth = 100;

        int x = (frameWidth - logoWidth) / 2;

        logoLabel.setBounds(x, 10, 100, 100);
        add(logoLabel);

        JTextField username = new JTextField();
        username.setBounds(120, 140, 140, 25);

        JPasswordField password = new JPasswordField();
        password.setBounds(120, 180, 140, 25);

        JPasswordField pin = new JPasswordField();
        pin.setBounds(120, 220, 140, 25);


        add(new JLabel("Username")).setBounds(20, 140, 80, 25);
        add(username);

        add(new JLabel("Password")).setBounds(20, 180, 80, 25);
        add(password);

        add(new JLabel("PIN")).setBounds(20, 220, 80, 25);
        add(pin);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(40, 280, 100, 35);
        add(loginBtn);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(150, 280, 100, 35);
        add(registerBtn);

        JButton forgotBtn = new JButton("Forgot Password");
        forgotBtn.setBounds(70, 330, 160, 30);

        add(forgotBtn);

        forgotBtn.addActionListener(e -> {
            new ForgotPasswordFrame();
        });

        JButton forgotPinBtn = new JButton("Forgot PIN");
        forgotPinBtn.setBounds(70, 370, 160, 30);
        add(forgotPinBtn);

        forgotPinBtn.addActionListener(e -> {
            new ForgotPinFrame();
        });

        loginBtn.addActionListener(e -> {

            boolean success = authService.login(
                    username.getText(),
                    new String(password.getPassword()),
                    pin.getText()
            );

            if (success) {
                SessionManager.login(username.getText());
                dispose();
                new DashboardFrame();
            }

            JOptionPane.showMessageDialog(this,
                    success ? "Login successful!" : "Invalid credentials");
        });

        registerBtn.addActionListener(e -> {
            new RegisterFrame();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}