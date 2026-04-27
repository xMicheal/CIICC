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

            int rows = stmt.executeUpdate(); // 🔥 THIS is the key line

            if (rows > 0) {

                // add logging.
                PreparedStatement logStmt = conn.prepareStatement(
                        "INSERT INTO transactions (username, type, amount) VALUES (?, 'DEPOSIT', ?)"
                );
                logStmt.setString(1, username);
                logStmt.setDouble(2, amount);
                logStmt.executeUpdate();

                return true;
            }

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

            int rows = updateStmt.executeUpdate(); // 🔥 success check

            if (rows > 0) {

                // ✅ ADD LOGGING HERE
                PreparedStatement logStmt = conn.prepareStatement(
                        "INSERT INTO transactions (username, type, amount) VALUES (?, 'WITHDRAW', ?)"
                );
                logStmt.setString(1, username);
                logStmt.setDouble(2, amount);
                logStmt.executeUpdate();

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean transfer(String fromUser, String toUser, double amount) {


        if (fromUser == null || toUser == null || fromUser.equals(toUser) || amount <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);


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


            PreparedStatement receiverStmt = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?"
            );
            receiverStmt.setString(1, toUser);
            ResultSet receiverRs = receiverStmt.executeQuery();

            if (!receiverRs.next()) {
                conn.rollback();
                return false;
            }


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


            PreparedStatement logOut = conn.prepareStatement(
                    "INSERT INTO transactions (username, type, amount) VALUES (?, 'TRANSFER_OUT', ?)"
            );
            logOut.setString(1, fromUser);
            logOut.setDouble(2, amount);
            logOut.executeUpdate();

            PreparedStatement logIn = conn.prepareStatement(
                    "INSERT INTO transactions (username, type, amount) VALUES (?, 'TRANSFER_IN', ?)"
            );
            logIn.setString(1, toUser);
            logIn.setDouble(2, amount);
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
}