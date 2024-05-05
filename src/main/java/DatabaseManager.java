import java.sql.*;

public class DatabaseManager {
    private static Connection databaseConnection;

    // Get the connection singleton if it exists, otherwise get a new connection
    public static Connection getConnection() {
        if(databaseConnection != null) return databaseConnection;

        String jdbcURL = "jdbc:postgresql://localhost:5432/Garden";
        String username = "postgres";
        String password = "password";

        try {
            Class.forName("org.postgresql.Driver");
            databaseConnection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("| =) | Connection Opened");
            return databaseConnection;
        } catch (ClassNotFoundException ce) {
            System.out.println("Trolled");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Run queries from here so that the database connection never has to leave this class
    public static ResultSet executeQuery(String query) {
        try {
            try (Statement statement = getConnection().createStatement()) {
                return statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (NullPointerException np) {
            System.out.println("null");
        }
        return null;
    }

    // Close connection
    public static void closeConnection() {
        if (databaseConnection != null) {
            try {
                databaseConnection.close();
                System.out.println("| =) | Connection closed.");
            } catch (SQLException e) {
                System.err.println("| D= | Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

