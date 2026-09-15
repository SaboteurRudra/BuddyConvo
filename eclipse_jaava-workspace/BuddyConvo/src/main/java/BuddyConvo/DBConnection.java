package BuddyConvo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String HOST = System.getenv("DB_HOST");
    private static final String PORT = System.getenv("DB_PORT");
    private static final String DATABASE = System.getenv("DB_NAME");
    private static final String USER = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {

        try {

            if (HOST == null || PORT == null || DATABASE == null
                    || USER == null || PASSWORD == null) {

                System.out.println("DATABASE CONFIGURATION ERROR");
                System.out.println("Check DB_HOST, DB_PORT, DB_NAME, DB_USERNAME and DB_PASSWORD in Render.");

                return null;
            }

            String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
                    + "?useSSL=true&requireSSL=true&serverTimezone=UTC";

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    url,
                    USER,
                    PASSWORD
            );

            System.out.println("DATABASE CONNECTED SUCCESSFULLY");

            return connection;

        } catch (Exception e) {

            System.out.println("DATABASE CONNECTION ERROR:");
            e.printStackTrace();

            return null;
        }
    }
}
