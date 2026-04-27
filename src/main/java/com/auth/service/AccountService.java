package com.auth.service;

import com.auth.db.DBConnection;
import com.auth.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountService {


    // FORGOT PASSWORD

    public boolean resetPassword(String username, String pin, String newPassword) {

        String query = "SELECT pin_hash FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String storedPin = rs.getString("pin_hash");


                if (!PasswordUtil.verify(pin, storedPin)) {
                    return false;
                }


                String update = "UPDATE users SET password_hash = ? WHERE username = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);

                updateStmt.setString(1, PasswordUtil.hash(newPassword));
                updateStmt.setString(2, username);

                return updateStmt.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // CHANGE PIN
    public boolean resetPin(String username, String password, String newPin) {

        if (!newPin.matches("\\d{4}")) {
            System.out.println("PIN must be 4 digits");
            return false;
        }

        String query = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String storedPassword = rs.getString("password_hash");

                // Verify password
                if (!PasswordUtil.verify(password, storedPassword)) {
                    return false;
                }

                // Update PIN
                String update = "UPDATE users SET pin_hash = ? WHERE username = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);

                updateStmt.setString(1, PasswordUtil.hash(newPin));
                updateStmt.setString(2, username);

                return updateStmt.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}