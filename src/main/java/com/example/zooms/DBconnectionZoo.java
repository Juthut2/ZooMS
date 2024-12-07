package com.example.zooms;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnectionZoo {
    private static Connection con = null; // Static connection for reuse

    public static Connection ConnectionDB() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the MySQL database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/altf4_db-1", "Admin", "Putbol");
            System.out.println("Connection Succeeded");
            return con; // Return the connection object
        } catch (Exception e) {
            // Show a GUI error message and print stack trace if connection fails
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        // Test the connection
        Connection con = DBconnectionZoo.ConnectionDB();
        if (con != null) {
            System.out.println("Database connection is ready to use.");
        } else {
            System.out.println("Failed to establish a database connection.");
        }
    }
}
