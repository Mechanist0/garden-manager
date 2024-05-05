import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Bucket {
    private int bucketID;
    private String description;
    private ArrayList<Plant> plantList = new ArrayList<>();

    public Bucket(int bucketID, String description) {
        this.bucketID = bucketID;
        this.description = description;
    }

    private void plantInitializer() throws SQLException {
        String query = "SELECT * FROM bucketplant";
        Statement statement = DatabaseManager.getStatement();
        ResultSet set = statement.executeQuery(query);
        while(set.next()) {
            int bucketID = set.getInt("bucketid");
            int plantID = set.getInt("plantid");
            Plant p = GardenController.getPlantByID(plantID);
            if(bucketID == this.bucketID) {
                plantList.add(p);
            }
        }
        statement.close();
    }

    // Getters
    public int getBucketID() {
        return bucketID;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Plant> getPlantList() throws SQLException {
        this.plantInitializer();
        return plantList;
    }

    // Setters
    public void setBucketID(int bucketID) {
        this.bucketID = bucketID;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setPlantList(ArrayList<Plant> plantList) {
        this.plantList = plantList;
    }

    // ToString

    @Override
    public String toString() {
        return "Bucket: " + bucketID + " " + description;
    }
}
