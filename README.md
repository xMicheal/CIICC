BangKo is as digital wallet application with the main functions of gcash, Log in, Withdraw, Deposit and transfer.
Developed as a requirement for our Java Tesda scholarship graduation.
To run this application you must install xampp and run the sql script for your DB.



-- CREATE DATABASE
=========================
CREATE DATABASE IF NOT EXISTS auth_system;
USE auth_system;


-- USERS TABLE
========================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    pin_hash VARCHAR(255) NOT NULL,
    balance DOUBLE DEFAULT 0
);


-- TRANSACTIONS TABLE
=========================
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    type VARCHAR(30) NOT NULL,
    amount DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    target_user VARCHAR(50) DEFAULT NULL
);
