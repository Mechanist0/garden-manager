import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class client {

    public static void main(String[] args)  {
        try {
            GardenController.gardenInit();
            GardenController.bucketInit();
            GardenController.plantInit();
            startUI();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        DatabaseManager.closeConnection();
    }

    // Population order:
    // Garden - id, desc
    // Buckets - check and place into garden
    // Plants - check and place into bucket
    public static void startUI() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to the Garden");
        boolean running = true;
        while (running) {
            // List of available operation
            System.out.print("""
                    Choose an operation:
                    1: Get all gardens
                    2: Get all buckets
                    3: Get all plants
                    4: Get all buckets in a specified garden
                    5: Get all plants in a specified bucket
                    6: Get all heights in a specified plant
                    7: Get tallest plants
                    8: Add new Garden
                    9: Add new Bucket
                    10: Add new Plant
                    11: Add height to plant
                    12: Add plant to bucket
                    13: Add bucket to garden
                    14: Remove plant from bucket
                    15: Remove bucket from garden
                    """);
            int inputInt = Integer.parseInt(reader.readLine());

            switch (inputInt) {
                case 0:
                    running = false;
                    break;

                // Get all gardens
                case 1:
                    GardenController.getGardens().forEach((integer, garden) -> System.out.println(garden));
                    break;

                // Get all buckets
                case 2:
                    GardenController.getBuckets().forEach((integer, bucket) -> System.out.println(bucket));
                    break;

                // Get all plants
                case 3:
                    GardenController.getPlants().forEach((integer, plant) -> System.out.println(plant));
                    break;

                // Get all buckets in a specified garden
                case 4:
                    System.out.println("Provide garden id:");
                    inputInt = Integer.parseInt(reader.readLine());
                    GardenController.getGardenByID(inputInt).getBucketList().forEach(System.out::println);
                    break;

                // Get all plants in a specified bucket
                case 5:
                    System.out.println("Provide bucket id:");
                    inputInt = Integer.parseInt(reader.readLine());
                    GardenController.getBucketByID(inputInt).getPlantList().forEach(System.out::println);
                    break;

                // Get all heights in a specified plant
                case 6:
                    System.out.println("Provide plant id:");
                    inputInt = Integer.parseInt(reader.readLine());
                    GardenController.getPlantByID(inputInt).getHeightTime().forEach(((date, integer) -> System.out.println(date + " " + integer + "cm")));
                    break;

                // Get tallest plants
                case 7:
                    String query = """
                            SELECT BP.*, CurHeight.height AS CurrentHeight
                            FROM Bucket_Plants_View AS BP
                            LEFT JOIN CURRENT_HEIGHT CurHeight\s
                              ON CurHeight.plantid = BP.plantid
                            WHERE CurHeight.height = (
                              SELECT MAX(innerCurHeight.height)  -- Find the maximum height
                              FROM CURRENT_HEIGHT AS innerCurHeight
                            )
                            """;
                    Statement statement = DatabaseManager.getStatement();
                    ResultSet set = statement.executeQuery(query);
                    Plant p;
                    while(set.next()) {
                        int id = set.getInt("plantid");
                        int height = set.getInt("currentheight");
                        p = GardenController.getPlantByID(id);
                        System.out.println("Current tallest plant is: (" + p + ") at " + height + "cm");
                    }
                    statement.close();
                    break;

                // Add new Garden
                case 8:
                    break;

                // Add new Bucket
                case 9:
                    break;

                // Add new Plant
                case 10:
                    break;

                // Add height to plant
                case 11:
                    break;

                // Add plant to bucket
                case 12:
                    break;

                // Add bucket to garden
                case 13:
                    break;

                // Remove plant from bucket
                case 14:
                    break;

                // Remove bucket from garden
                case 15:
                    break;
            }

            reader.readLine();
        }
    }
}
