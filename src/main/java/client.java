import java.sql.*;
public class client {
    public static void main(String[] args) throws SQLException {
        String query = "SELECT * FROM plant";
        ResultSet set = DatabaseManager.executeQuery(query);
        while(set.next()) {

        }


    }
    // Population order:
    // Garden - id, desc
    // Buckets - check and place into garden
    // Plants - check and place into bucket
    public static void populateLocalDatabaseInformation() {

    }

    public static void populateGarden() {

    }

    public static void populateBucket() {

    }

    public static void populatePlant() {

    }
}
