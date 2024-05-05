import java.util.ArrayList;

public class Garden {
    private int gardenID;
    private String description;
    private ArrayList<Bucket> bucketList;

    public Garden(int gardenID, String description) {
        this.gardenID = gardenID;
        this.description = description;
    }

    // Getters
    public int getGardenID() {
        return gardenID;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Bucket> getBucketList() {
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
}
