package com.auth.gui;

import com.auth.service.BankingService;
import com.auth.session.SessionManager;
import com.auth.service.AccountService;
import javax.swing.*;

public class DashboardFrame extends JFrame {

    private BankingService bankingService = new BankingService();
    private JLabel balanceLabel;
    private AccountService accountService = new AccountService();

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

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBounds(20, 140, 120, 30);
        add(transferBtn);

        //Deposit
        depositBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter amount:");
            double amount = Double.parseDouble(input);

            if (bankingService.deposit(user, amount)) {
                updateBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed");
            }
        });
        //Withdraw
        withdrawBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter amount:");
            double amount = Double.parseDouble(input);

            if (bankingService.withdraw(user, amount)) {
                updateBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds");
            }
        });
        //Logout
        logoutBtn.addActionListener(e -> {
            SessionManager.logout();
            dispose();
            new LoginFrame();
        });

        setVisible(true);


        // Transfer Button
        transferBtn.addActionListener(e -> {

            String toUser = JOptionPane.showInputDialog("Enter recipient username:");

            if (toUser == null || toUser.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Recipient is required");
                return;
            }

            if (!bankingService.userExists(toUser)) {
                JOptionPane.showMessageDialog(this, "User does not exist");
                return;
            }

            String amountStr = JOptionPane.showInputDialog("Enter amount:");

            if (amountStr == null || amountStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Amount is required");
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                //  PIN CONFIRMATION
                JPasswordField pinField = new JPasswordField();

                int option = JOptionPane.showConfirmDialog(
                        this,
                        pinField,
                        "Enter PIN to confirm",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (option != JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(this, "Transfer cancelled");
                    return;
                }

                String pin = new String(pinField.getPassword());

                if (pin == null || pin.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "PIN is required");
                    return;
                }

                boolean pinValid = accountService.verifyPin(
                        SessionManager.getCurrentUser(),
                        pin
                );

                if (!pinValid) {
                    JOptionPane.showMessageDialog(this, "Invalid PIN");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Send " + amount + " to " + toUser + "?",
                        "Confirm Transfer",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Transfer cancelled");
                    return;
                }
                // Pin Validation
                boolean success = bankingService.transfer(
                        SessionManager.getCurrentUser(),
                        toUser,
                        amount
                );

                JOptionPane.showMessageDialog(this,
                        success ? "Transfer successful"
                                : "Transfer failed (insufficient balance)");

                updateBalance();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount input");
            }
        });
    }

    private void updateBalance() {
        String user = SessionManager.getCurrentUser();
        double balance = bankingService.getBalance(user);
        balanceLabel.setText("Balance: " + balance);
    }
}