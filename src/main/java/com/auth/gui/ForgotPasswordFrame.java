package com.auth.gui;

import com.auth.service.AccountService;

import javax.swing.*;

public class ForgotPasswordFrame extends JFrame {

    private AccountService accountService = new AccountService();

    public ForgotPasswordFrame() {

        setTitle("Forgot Password");
        setSize(300, 250);
        setLayout(null);

        JTextField username = new JTextField();
        username.setBounds(120, 20, 140, 25);

        JPasswordField pin = new JPasswordField();
        pin.setBounds(120, 60, 140, 25);

        JPasswordField newPassword = new JPasswordField();
        newPassword.setBounds(120, 100, 140, 25);

        add(new JLabel("Username")).setBounds(20, 20, 80, 25);
        add(username);

        add(new JLabel("PIN")).setBounds(20, 60, 80, 25);
        add(pin);

        add(new JLabel("New Password")).setBounds(20, 100, 100, 25);
        add(newPassword);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setBounds(90, 150, 120, 30);
        add(resetBtn);

        resetBtn.addActionListener(e -> {

            boolean success = accountService.resetPassword(
                    username.getText(),
                    pin.getText(),
                    new String(newPassword.getPassword())
            );

            JOptionPane.showMessageDialog(this,
                    success ? "Password reset successful!" : "Invalid username or PIN");
        });

        setVisible(true);
    }
}