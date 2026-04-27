package com.auth.gui;

import com.auth.service.AccountService;

import javax.swing.*;

public class ForgotPinFrame extends JFrame {

    private AccountService accountService = new AccountService();

    public ForgotPinFrame() {

        setTitle("Forgot PIN");
        setSize(300, 250);
        setLayout(null);

        JTextField username = new JTextField();
        username.setBounds(120, 20, 140, 25);

        JPasswordField password = new JPasswordField();
        password.setBounds(120, 60, 140, 25);

        JPasswordField newPin = new JPasswordField();
        newPin.setBounds(120, 100, 140, 25);

        add(new JLabel("Username")).setBounds(20, 20, 80, 25);
        add(username);

        add(new JLabel("Password")).setBounds(20, 60, 80, 25);
        add(password);

        add(new JLabel("New PIN")).setBounds(20, 100, 80, 25);
        add(newPin);

        JButton resetBtn = new JButton("Reset PIN");
        resetBtn.setBounds(90, 150, 120, 30);
        add(resetBtn);

        resetBtn.addActionListener(e -> {

            boolean success = accountService.resetPin(
                    username.getText(),
                    new String(password.getPassword()),
                    newPin.getText()
            );

            JOptionPane.showMessageDialog(this,
                    success ? "PIN reset successful!" : "Invalid username or password");
        });

        setVisible(true);
    }
}