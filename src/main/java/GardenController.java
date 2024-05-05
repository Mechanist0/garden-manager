import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class GardenController {
    private static HashMap<Integer, Garden> gardens = new HashMap<>();
    private static HashMap<Integer, Bucket> buckets = new HashMap<>();
    private static HashMap<Integer, Plant> plants = new HashMap<>();
    public static void gardenInit() throws SQLException {
        String query = "SELECT * FROM garden";
        Statement statement = DatabaseManager.getStatement();
        ResultSet set = statement.executeQuery(query);
        while(set.next()) {
            int id = set.getInt("gardenID");
            String description = set.getString("description");

            Garden garden = new Garden(id, description);
            gardens.put(garden.getGardenID(), garden);
        }
        statement.close();
    }

    public static void bucketInit() throws SQLException {
        String query = "SELECT * FROM bucket";
        Statement statement = DatabaseManager.getStatement();
        ResultSet set = statement.executeQuery(query);
        while(set.next()) {
            int id = set.getInt("bucketID");
            String description = set.getString("description");

            Bucket bucket = new Bucket(id, description);
            buckets.put(bucket.getBucketID(), bucket);

        }
        statement.close();
    }

    public static void plantInit() throws SQLException {
        String query = "SELECT * FROM plant";
        Statement statement = DatabaseManager.getStatement();
        ResultSet set = statement.executeQuery(query);
        while(set.next()) {
            int id = set.getInt("plantID");
            String name = set.getString("name");
            String description = set.getString("description");
            Date lastWatered = set.getDate("lastwatered");
            Date datePlanted = set.getDate("dateplanted");
            int totalFruit = set.getInt("totalfruit");
            int harvestedFruit = set.getInt("harvestedfruit");
            int failedFruit = set.getInt("failedfruit");

            Plant plant = new Plant(id, name, description, lastWatered, datePlanted, totalFruit, harvestedFruit, failedFruit);
            plants.put(plant.getPlantID(), plant);
        }
        statement.close();
    }


    // Getters
    public static HashMap<Integer, Garden> getGardens() {
        return gardens;
    }

    public static HashMap<Integer, Bucket> getBuckets() {
        return buckets;
    }

    public static HashMap<Integer, Plant> getPlants() {
        return plants;
    }

    public static Garden getGardenByID(int id) {
        return gardens.get(id);
    }

    public static Bucket getBucketByID(int id) {
        return buckets.get(id);
    }

    public static Plant getPlantByID(int id) {
        return plants.get(id);
    }

    // Setters
    public static void setGardens(HashMap<Integer, Garden> gardens) {
        GardenController.gardens = gardens;
    }

    public static void setBuckets(HashMap<Integer, Bucket> buckets) {
        GardenController.buckets = buckets;
    }

    public static void setPlants(HashMap<Integer, Plant> plants) {
        GardenController.plants = plants;
    }
}
