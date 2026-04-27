package com.auth.service;

import com.auth.db.DBConnection;
import com.auth.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService {


    // PIN VALIDATION

    private boolean isValidPin(String pin) {
        return pin != null && pin.matches("\\d{4}");
    }


    // REGISTER USER

    public boolean register(String username, String email, String password, String pin) {

        if (!isValidPin(pin)) {
            System.out.println("PIN must be exactly 4 digits.");
            return false;
        }

        String checkQuery = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setString(1, username);
            checkStmt.setString(2, email);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("User already exists.");
                return false;
            }

            String hashedPassword = PasswordUtil.hash(password);
            String hashedPin = PasswordUtil.hash(pin);

            String insertQuery =
                    "INSERT INTO users (username, email, password_hash, pin_hash) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, hashedPin);

            stmt.executeUpdate();

            System.out.println("User registered successfully.");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // LOGIN USER

    public boolean login(String username, String password, String pin) {

        String query =
                "SELECT password_hash, pin_hash FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String storedPassword = rs.getString("password_hash");
                String storedPin = rs.getString("pin_hash");

                boolean passwordMatch = PasswordUtil.verify(password, storedPassword);
                boolean pinMatch = PasswordUtil.verify(pin, storedPin);

                return passwordMatch && pinMatch;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}