package com.auth.gui;

import com.auth.service.AuthService;

import javax.swing.*;

public class RegisterFrame extends JFrame {

    private AuthService authService = new AuthService();

    public RegisterFrame() {

        setTitle("Register");
        setSize(300, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField username = new JTextField();
        username.setBounds(120, 20, 140, 25);

        JTextField email = new JTextField();
        email.setBounds(120, 60, 140, 25);

        JPasswordField password = new JPasswordField();
        password.setBounds(120, 100, 140, 25);

        JTextField pin = new JTextField();
        pin.setBounds(120, 140, 140, 25);

        add(new JLabel("Username")).setBounds(20, 20, 80, 25);
        add(username);

        add(new JLabel("Email")).setBounds(20, 60, 80, 25);
        add(email);

        add(new JLabel("Password")).setBounds(20, 100, 80, 25);
        add(password);

        add(new JLabel("PIN")).setBounds(20, 140, 80, 25);
        add(pin);

        JButton submit = new JButton("Register");
        submit.setBounds(90, 190, 120, 30);
        add(submit);

        submit.addActionListener(e -> {

            boolean success = authService.register(
                    username.getText(),
                    email.getText(),
                    new String(password.getPassword()),
                    pin.getText()
            );

            JOptionPane.showMessageDialog(this,
                    success ? "Registered successfully!" : "Registration failed.");
        });

        setVisible(true);
    }
}