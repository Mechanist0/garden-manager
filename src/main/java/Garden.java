import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Garden {
    private int gardenID;
    private String description;
    private ArrayList<Bucket> bucketList = new ArrayList<>();

    public Garden(int gardenID, String description) throws SQLException {
        this.gardenID = gardenID;
        this.description = description;
    }

    private void bucketInitializer() throws SQLException {
        bucketList = new ArrayList<>();
        String query = "SELECT * FROM gardenbucket";
        Statement statement = DatabaseManager.getStatement();
        ResultSet set = statement.executeQuery(query);
        while(set.next()) {
            int gardenID = set.getInt("gardenid");
            int bucketID = set.getInt("bucketid");
            Bucket b = GardenController.getBucketByID(bucketID);
            if(gardenID == this.gardenID) {
                bucketList.add(b);
            }
        }
        statement.close();
    }

    // Getters
    public int getGardenID() {
        return gardenID;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Bucket> getBucketList() throws SQLException {
        this.bucketInitializer();
        return bucketList;
    }

    // Setters
    public void setGardenID(int id) {
        this.gardenID = id;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setBucketList(ArrayList<Bucket> buckets) {
        this.bucketList = buckets;
    }

    // ToString
    @Override
    public String toString() {
        return "Garden: " + gardenID + " " + description;
    }
}
