package com.auth.gui;

import com.auth.service.AuthService;

import javax.swing.*;

public class LoginFrame extends JFrame {

    private AuthService authService = new AuthService();

    public LoginFrame() {

        setTitle("Login");
        setSize(300, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField username = new JTextField();
        username.setBounds(120, 20, 140, 25);

        JPasswordField password = new JPasswordField();
        password.setBounds(120, 60, 140, 25);

        JTextField pin = new JTextField();
        pin.setBounds(120, 100, 140, 25);

        add(new JLabel("Username")).setBounds(20, 20, 80, 25);
        add(username);

        add(new JLabel("Password")).setBounds(20, 60, 80, 25);
        add(password);

        add(new JLabel("PIN")).setBounds(20, 100, 80, 25);
        add(pin);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(40, 150, 100, 30);
        add(loginBtn);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(150, 150, 100, 30);
        add(registerBtn);

        loginBtn.addActionListener(e -> {

            boolean success = authService.login(
                    username.getText(),
                    new String(password.getPassword()),
                    pin.getText()
            );

            JOptionPane.showMessageDialog(this,
                    success ? "Login successful!" : "Invalid credentials");
        });

        registerBtn.addActionListener(e -> {
            new RegisterFrame();
        });

        setVisible(true);
    }
}