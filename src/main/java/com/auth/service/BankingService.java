package com.auth.service;

import com.auth.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BankingService {

    public double getBalance(String username) {

        String query = "SELECT balance FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean deposit(String username, double amount) {

        if (amount <= 0) return false;

        String query = "UPDATE users SET balance = balance + ? WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, username);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean withdraw(String username, double amount) {

        if (amount <= 0) return false;

        String check = "SELECT balance FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(check)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");

                if (balance < amount) return false;
            }

            String update = "UPDATE users SET balance = balance - ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(update);

            updateStmt.setDouble(1, amount);
            updateStmt.setString(2, username);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean transfer(String fromUser, String toUser, double amount) {

        //  validation
        if (fromUser == null || toUser == null || fromUser.equals(toUser) || amount <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);


            // Check sender exists + get balance

            PreparedStatement senderStmt = conn.prepareStatement(
                    "SELECT balance FROM users WHERE username = ?"
            );
            senderStmt.setString(1, fromUser);
            ResultSet senderRs = senderStmt.executeQuery();

            if (!senderRs.next()) {
                conn.rollback();
                return false;
            }

            double senderBalance = senderRs.getDouble("balance");

            if (senderBalance < amount) {
                conn.rollback();
                return false;
            }


            //  Check receiver exists

            PreparedStatement receiverStmt = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?"
            );
            receiverStmt.setString(1, toUser);
            ResultSet receiverRs = receiverStmt.executeQuery();

            if (!receiverRs.next()) {
                conn.rollback();
                return false;
            }


            //  Perform transfer

            PreparedStatement deduct = conn.prepareStatement(
                    "UPDATE users SET balance = balance - ? WHERE username = ?"
            );
            deduct.setDouble(1, amount);
            deduct.setString(2, fromUser);
            deduct.executeUpdate();

            PreparedStatement add = conn.prepareStatement(
                    "UPDATE users SET balance = balance + ? WHERE username = ?"
            );
            add.setDouble(1, amount);
            add.setString(2, toUser);
            add.executeUpdate();


            //  Log transactions

            PreparedStatement logOut = conn.prepareStatement(
                    "INSERT INTO transactions (username, target_user, type, amount) VALUES (?, ?, 'TRANSFER_OUT', ?)"
            );

            logOut.setString(1, fromUser);
            logOut.setString(2, toUser);
            logOut.setDouble(3, amount);
            logOut.executeUpdate();

            PreparedStatement logIn = conn.prepareStatement(
                    "INSERT INTO transactions (username, target_user, type, amount) VALUES (?, ?, 'TRANSFER_IN', ?)"
            );

            logIn.setString(1, toUser);
            logIn.setString(2, fromUser);
            logIn.setDouble(3, amount);
            logIn.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }
    public boolean userExists(String username) {

        String query = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // true if user exists

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public String viewAllTransactions() {

        StringBuilder history = new StringBuilder();

        String query =
                "SELECT username, target_user, type, amount, created_at " +
                        "FROM transactions " +
                        "ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                history.append("User: ")
                        .append(rs.getString("username"));

                String target = rs.getString("target_user");

                if (target != null) {
                    history.append(" -> ").append(target);
                }

                history.append(" | ")
                        .append(rs.getString("type"))
                        .append(" | ₱")
                        .append(rs.getDouble("amount"))
                        .append(" | ")
                        .append(rs.getTimestamp("created_at"))
                        .append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return history.toString();
    }
    public String viewUserTransactions(String username) {

        StringBuilder history = new StringBuilder();

        String query =
                "SELECT username, target_user, type, amount, created_at " +
                        "FROM transactions " +
                        "WHERE username = ? " +
                        "ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                history.append("Type: ")
                        .append(rs.getString("type"));

                String target = rs.getString("target_user");

                if (target != null) {
                    history.append(" | User: ")
                            .append(target);
                }

                history.append(" | ₱")
                        .append(rs.getDouble("amount"))
                        .append(" | ")
                        .append(rs.getTimestamp("created_at"))
                        .append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return history.toString();
    }
}