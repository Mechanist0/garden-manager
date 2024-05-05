import java.sql.Date;
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
    private Map<Date, Integer> heightTime;
}
