import java.util.ArrayList;

public class Bucket {
    private int bucketID;
    private String description;
    private ArrayList<Plant> plantList;

    public Bucket(int bucketID, String description) {
        this.bucketID = bucketID;
        this.description = description;
    }

    // Getters
    public int getBucketID() {
        return bucketID;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Plant> getPlantList() {
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
}
