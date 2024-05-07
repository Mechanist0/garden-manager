import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Plant {
    private int plantID;
    private String name;
    private String description;
    private Date lastWatered;
    private Date datePlanted;
    private int totalFruit;
    private int harvestedFruit;
    private int failedFruit;
    private Map<Date, Integer> heightTime = new HashMap<>();

    public Plant(int plantID, String name, String description, Date lastWatered, Date datePlanted, int totalFruit, int harvestedFruit, int failedFruit) {
        this.plantID = plantID;
        this.name = name;
        this.description = description;
        this.lastWatered = lastWatered;
        this.datePlanted = datePlanted;
        this.totalFruit = totalFruit;
        this.harvestedFruit = harvestedFruit;
        this.failedFruit = failedFruit;
    }

    private void heightInitializer() throws SQLException {
        heightTime = new HashMap<>();
        String query = "SELECT * FROM plantheight";
        Statement statement = DatabaseManager.getStatement();
        ResultSet set = statement.executeQuery(query);
        while(set.next()) {
            int plantID = set.getInt("plantid");
            int height = set.getInt("height");
            Date date = set.getDate("date");
            if(plantID == this.plantID) {
                heightTime.put(date, height);
            }
        }
        statement.close();
    }

    // Getters
    public int getPlantID() {
        return plantID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getLastWatered() {
        return lastWatered;
    }

    public Date getDatePlanted() {
        return datePlanted;
    }

    public int getTotalFruit() {
        return totalFruit;
    }

    public int getHarvestedFruit() {
        return harvestedFruit;
    }

    public int getFailedFruit() {
        return failedFruit;
    }

    // If empty, run a query that gets all the date that is connected to this plant id
    public Map<Date, Integer> getHeightTime() throws SQLException {
        this.heightInitializer();
        return heightTime;
    }

    // Setters
    public void setPlantID(int plantID) {
        this.plantID = plantID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastWatered(Date lastWatered) {
        this.lastWatered = lastWatered;
    }

    public void setDatePlanted(Date datePlanted) {
        this.datePlanted = datePlanted;
    }

    public void setTotalFruit(int totalFruit) {
        this.totalFruit = totalFruit;
    }

    public void setHarvestedFruit(int harvestedFruit) {
        this.harvestedFruit = harvestedFruit;
    }

    public void setFailedFruit(int failedFruit) {
        this.failedFruit = failedFruit;
    }

    public void setHeightTime(Map<Date, Integer> heightTime) {
        this.heightTime = heightTime;
    }

    // ToString

    @Override
    public String toString() {
        return "Plant: " + plantID + " " + name + " " + description;
    }
}
