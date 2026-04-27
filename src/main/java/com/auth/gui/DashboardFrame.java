package com.auth.gui;

import com.auth.service.BankingService;
import com.auth.session.SessionManager;

import javax.swing.*;

public class DashboardFrame extends JFrame {

    private BankingService bankingService = new BankingService();
    private JLabel balanceLabel;

    public DashboardFrame() {

        setTitle("Dashboard");
        setSize(300, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String user = SessionManager.getCurrentUser();

        JLabel welcome = new JLabel("Welcome, " + user);
        welcome.setBounds(20, 20, 200, 25);
        add(welcome);

        balanceLabel = new JLabel();
        balanceLabel.setBounds(20, 60, 200, 25);
        add(balanceLabel);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(20, 100, 120, 30);
        add(depositBtn);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(160, 100, 120, 30);
        add(withdrawBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(90, 180, 120, 30);
        add(logoutBtn);

        updateBalance();

        depositBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter amount:");
            double amount = Double.parseDouble(input);

            if (bankingService.deposit(user, amount)) {
                updateBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed");
            }
        });

        withdrawBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter amount:");
            double amount = Double.parseDouble(input);

            if (bankingService.withdraw(user, amount)) {
                updateBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds");
            }
        });

        logoutBtn.addActionListener(e -> {
            SessionManager.logout();
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    private void updateBalance() {
        String user = SessionManager.getCurrentUser();
        double balance = bankingService.getBalance(user);
        balanceLabel.setText("Balance: " + balance);
    }
}