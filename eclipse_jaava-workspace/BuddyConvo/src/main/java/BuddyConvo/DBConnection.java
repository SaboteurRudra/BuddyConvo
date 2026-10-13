package BuddyConvo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String DB_URL = System.getenv("DB_URL");
    private static final String HOST = System.getenv("DB_HOST");
    private static final String PORT = System.getenv("DB_PORT");
    private static final String DATABASE = System.getenv("DB_NAME");
    private static final String USER = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        try {
            String url = resolveJdbcUrl();
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, USER, PASSWORD);
            System.out.println("DATABASE CONNECTED SUCCESSFULLY");
            return connection;
        } catch (Exception e) {
            System.out.println("DATABASE CONNECTION ERROR:");
            e.printStackTrace();
            return null;
        }
    }

    private static String resolveJdbcUrl() {
        if (DB_URL != null && !DB_URL.isBlank()) {
            String url = DB_URL.trim();
            if (url.startsWith("mysql://")) {
                url = "jdbc:" + url;
            }
            if (!url.startsWith("jdbc:mysql://")) {
                throw new IllegalStateException(
                        "DB_URL must be a MySQL JDBC URL (jdbc:mysql://host:port/database).");
            }
            requireCredentials();
            return url;
        }

        if (isBlank(HOST) || isBlank(DATABASE) || isBlank(USER) || isBlank(PASSWORD)) {
            throw new IllegalStateException(
                    "Set DB_URL and DB_USERNAME/DB_PASSWORD, or set DB_HOST, DB_NAME, DB_USERNAME and DB_PASSWORD.");
        }

        String port = isBlank(PORT) ? "3306" : PORT.trim();
        return "jdbc:mysql://" + HOST.trim() + ":" + port + "/" + DATABASE.trim()
                + "?useSSL=true&requireSSL=true&serverTimezone=UTC";
    }

    private static void requireCredentials() {
        if (isBlank(USER) || isBlank(PASSWORD)) {
            throw new IllegalStateException("Set DB_USERNAME and DB_PASSWORD for the configured database.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
