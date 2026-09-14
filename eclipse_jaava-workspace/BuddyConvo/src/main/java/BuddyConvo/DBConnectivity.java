package BuddyConvo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnectivity {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "220105";

        try {

            Connection con = DriverManager.getConnection(url, user, password);

            Statement st = con.createStatement();

            st.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS BuddyConvo"
            );

            st.executeUpdate(
                "USE BuddyConvo"
            );

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(100) NOT NULL" +
                ")"
            );

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS messages (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "sender VARCHAR(50) NOT NULL," +
                "receiver VARCHAR(50) NOT NULL," +
                "message TEXT NOT NULL," +
                "sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            System.out.println(
                "Database and tables created successfully!"
            );

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}