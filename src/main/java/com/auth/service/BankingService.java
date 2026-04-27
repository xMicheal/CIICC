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

            return stmt.executeUpdate() > 0;

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

            return updateStmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}