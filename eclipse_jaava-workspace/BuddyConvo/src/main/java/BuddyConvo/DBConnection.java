package BuddyConvo;

import java.net.URI;
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
            DatabaseConfig config = resolveDatabaseConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    config.url, config.username, config.password);
            System.out.println("DATABASE CONNECTED SUCCESSFULLY");
            return connection;
        } catch (Exception e) {
            System.out.println("DATABASE CONNECTION ERROR:");
            e.printStackTrace();
            return null;
        }
    }

    private static DatabaseConfig resolveDatabaseConfig() {
        if (!isBlank(DB_URL)) {
            String url = toJdbcUrl(DB_URL.trim());
            if (!url.startsWith("jdbc:mysql://")) {
                throw new IllegalStateException(
                        "DB_URL must be a MySQL JDBC URL (jdbc:mysql://host:port/database).");
            }
            requireCredentials();
            return new DatabaseConfig(url, USER, PASSWORD);
        }

        if (isBlank(HOST) || isBlank(DATABASE) || isBlank(USER) || isBlank(PASSWORD)) {
            throw new IllegalStateException(
                    "Set DB_URL and DB_USERNAME/DB_PASSWORD, or set DB_HOST, DB_NAME, DB_USERNAME and DB_PASSWORD.");
        }

        String port = isBlank(PORT) ? "3306" : PORT.trim();
        String url = "jdbc:mysql://" + HOST.trim() + ":" + port + "/" + DATABASE.trim()
                + "?useSSL=true&requireSSL=true&serverTimezone=UTC";
        return new DatabaseConfig(url, USER, PASSWORD);
    }

    private static String toJdbcUrl(String configuredUrl) {
        if (!configuredUrl.startsWith("mysql://")) {
            return configuredUrl;
        }

        URI uri = URI.create(configuredUrl);
        String databasePath = uri.getRawPath();
        if (isBlank(uri.getHost()) || isBlank(databasePath) || "/".equals(databasePath)) {
            throw new IllegalStateException("DB_URL must include a MySQL host and database name.");
        }

        StringBuilder jdbcUrl = new StringBuilder("jdbc:mysql://").append(uri.getHost());
        if (uri.getPort() != -1) {
            jdbcUrl.append(":").append(uri.getPort());
        }
        jdbcUrl.append(databasePath);
        if (uri.getRawQuery() != null) {
            jdbcUrl.append('?').append(uri.getRawQuery());
        }
        return jdbcUrl.toString();
    }

    private static void requireCredentials() {
        if (isBlank(USER) || isBlank(PASSWORD)) {
            throw new IllegalStateException("Set DB_USERNAME and DB_PASSWORD for the configured database.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static final class DatabaseConfig {
        private final String url;
        private final String username;
        private final String password;

        private DatabaseConfig(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }
    }
}
