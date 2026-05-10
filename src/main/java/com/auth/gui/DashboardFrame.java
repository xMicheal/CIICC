package com.auth.gui;

import com.auth.service.BankingService;
import com.auth.session.SessionManager;
import javax.swing.*;
import com.auth.service.AccountService;
public class DashboardFrame extends JFrame {

    private BankingService bankingService = new BankingService();
    private JLabel balanceLabel;
    private AccountService accountService = new AccountService();
    public DashboardFrame() {

        setTitle("Dashboard");
        setSize(400, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String user = SessionManager.getCurrentUser();

// Welcome label
        JLabel welcome = new JLabel("Welcome, " + user);
        welcome.setBounds(20, 20, 250, 25);
        add(welcome);

// Balance label
        balanceLabel = new JLabel();
        balanceLabel.setBounds(20, 55, 250, 25);
        add(balanceLabel);

// Deposit button
        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(20, 100, 150, 35);
        add(depositBtn);

// Withdraw button
        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(200, 100, 150, 35);
        add(withdrawBtn);

// Transfer button
        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBounds(20, 150, 150, 35);
        add(transferBtn);

// My Transactions button
        JButton myTransactionsBtn = new JButton("My Transactions");
        myTransactionsBtn.setBounds(20, 200, 150, 35);
        add(myTransactionsBtn);

// All Transactions button
        JButton allTransactionsBtn = new JButton("All Transactions");
        allTransactionsBtn.setBounds(200, 200, 150, 35);
        add(allTransactionsBtn);

// Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(110, 280, 150, 35);
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

        myTransactionsBtn.addActionListener(e -> {

            String history = bankingService.viewUserTransactions(
                    SessionManager.getCurrentUser()
            );

            JTextArea area = new JTextArea(history);
            area.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(area);

            scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "My Transactions",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        allTransactionsBtn.addActionListener(e -> {

            String history = bankingService.viewAllTransactions();

            JTextArea area = new JTextArea(history);
            area.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(area);

            scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "All Transactions",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

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

                //  PIN INPUT (hidden)
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

                boolean pinValid = accountService.verifyPin(
                        SessionManager.getCurrentUser(),
                        pin
                );

                if (!pinValid) {
                    JOptionPane.showMessageDialog(this, "Invalid PIN");
                    return;
                }

                // FINAL CONFIRMATION
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

                // EXECUTE TRANSFER
                boolean success = bankingService.transfer(
                        SessionManager.getCurrentUser(),
                        toUser,
                        amount
                );

                JOptionPane.showMessageDialog(this,
                        success ? "Transfer successful"
                                : "Transfer failed (insufficient balance)");

                updateBalance();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount input");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateBalance() {
        String user = SessionManager.getCurrentUser();
        double balance = bankingService.getBalance(user);
        balanceLabel.setText("Balance: " + balance);
    }
}