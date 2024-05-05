import java.sql.*;
public class client {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseManager.getConnection();

        String query = "SELECT * FROM garden";
        try {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                ResultSet set = statement.executeQuery(query);

                while(set.next()) {
                    int gardenID = set.getInt("gardenid");
                    String description = set.getString("description");

                    System.out.println(gardenID + " " + description);
                }
            }
        } catch (NullPointerException np) {
            System.out.println("null");
        }

        DatabaseManager.closeConnection();
    }
}
