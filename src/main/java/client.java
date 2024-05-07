import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class client {

    public static void main(String[] args) {
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
                    0: Close
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
                    16: Remove Height from plant
                    17: Remove Garden
                    18: Remove Bucket
                    19: Remove Plant
                    20: Update Garden
                    21: Update Bucket
                    22: Update Plant
                    """);

            String sql = "";
            PreparedStatement preparedStatement;
            String input = "";
            int inputInt = Integer.parseInt(reader.readLine());
            Date dateinput = new Date(0);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("You inserted: ");

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
                    while (set.next()) {
                        int id = set.getInt("plantid");
                        int height = set.getInt("currentheight");
                        p = GardenController.getPlantByID(id);
                        System.out.println("Current tallest plant is: (" + p + ") at " + height + "cm");
                    }
                    statement.close();
                    break;

                // Add new Garden
                case 8:
                    sql = "INSERT INTO garden (gardenid, description) VALUES (?, ?)";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    System.out.println("Provide garden id:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    // Create a prepared statement
                    System.out.println("Provide garden description:");
                    input = reader.readLine();
                    preparedStatement.setString(2, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);


                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    preparedStatement.executeUpdate();

                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // Add new Bucket
                case 9:
                    sql = "INSERT INTO bucket (bucketid, description) VALUES (?, ?)";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    System.out.println("Provide bucket id:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    // Create a prepared statement
                    System.out.println("Provide bucket description:");
                    input = reader.readLine();
                    preparedStatement.setString(2, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    preparedStatement.executeUpdate();
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // Add new Plant
                case 10:
                    sql = "INSERT INTO plant (plantid, Name, Description, LastWatered, DatePlanted, TotalFruit, HarvestedFruit, FailedFruit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    System.out.println("Provide plant ID:");
                    inputInt = Integer.valueOf(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide plant name:");
                    input = reader.readLine();
                    preparedStatement.setString(2, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);

                    System.out.println("Provide plant Description:");
                    input = reader.readLine();
                    preparedStatement.setString(3, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);

                    System.out.println("Provide plant LastWatered (yyyy-mm-dd):");
                    dateinput = Date.valueOf(reader.readLine());
                    preparedStatement.setDate(4, dateinput);
                    stringBuilder.append(" ");
                    stringBuilder.append(dateinput);

                    System.out.println("Provide plant DatePlanted (yyyy-mm-dd):");
                    dateinput = Date.valueOf(reader.readLine());
                    preparedStatement.setDate(5, dateinput);
                    stringBuilder.append(" ");
                    stringBuilder.append(dateinput);

                    System.out.println("Provide plant TotalFruit:");
                    inputInt = Integer.valueOf(reader.readLine());
                    preparedStatement.setInt(6, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide plant HarvestedFruit:");
                    inputInt = Integer.valueOf(reader.readLine());
                    preparedStatement.setInt(7, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide plant FailedFruit:");
                    inputInt = Integer.valueOf(reader.readLine());
                    preparedStatement.setInt(8, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // Add height to plant
                case 11:
                    sql = "INSERT INTO plantheight (PHID, PlantID, Height, Date) VALUES (?, ?, ?, ?)";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide PlantHeight ID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide PlantID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(2, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide Height:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(3, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide Date:");
                    Date dnput = Date.valueOf(reader.readLine());
                    preparedStatement.setDate(4, dnput);
                    stringBuilder.append(" ");
                    stringBuilder.append(dnput.toString());

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // Add plant to bucket
                case 12:
                    sql = "INSERT INTO BucketPlant (BPID, BucketID, PlantID) VALUES (?, ?, ?)";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide BucketPlant ID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide BucketID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(2, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide PlantID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(3, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // Add bucket to garden
                case 13:
                    sql = "INSERT INTO GardenBucket (GBID, GardenID, BucketID) VALUES (?, ?, ?)";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide GardenBucket ID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide GardenID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(2, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide BucketID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(3, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // Remove plant from bucket
                case 14:
                    sql = "DELETE FROM BucketPlant WHERE BucketID = ? AND PlantID = ?";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide BucketID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide PlantID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(2, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // Remove bucket from garden
                case 15:
                    sql = "DELETE FROM GardenBucket WHERE GardenID = ? AND BucketID = ?";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide GardenID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide BucketID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(2, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);
                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // 16: Remove Height from plant
                case 16:
                    sql = "DELETE FROM plantheight WHERE PHID = ?";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide PlantHeight ID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // 17: Remove Garden
                case 17:
                    sql = "DELETE FROM garden WHERE GardenID = ?";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide GardenID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // 18: Remove Bucket
                case 18:
                    sql = "DELETE FROM bucket WHERE BucketID = ?";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide BucketID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // 19: Remove Plant
                case 19:
                    sql = "DELETE FROM plant WHERE plantID = ?";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide plantID:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(1, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // 20: Update Garden
                case 20:
                    sql = """
                            UPDATE garden
                            set description = ?
                            WHERE gardenID = ?
                            """;
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide gardenID to update:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(2, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide updated description:");
                    input = reader.readLine();
                    preparedStatement.setString(1, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // 21: Update Bucket
                case 21:
                    sql = """
                            UPDATE bucket
                            set description = ?
                            WHERE bucketID = ?
                            """;
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide bucketID to update:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(2, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide updated description:");
                    input = reader.readLine();
                    preparedStatement.setString(1, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;

                // 22: Update Plant
                case 22:
                    sql = "UPDATE plant SET Name = ?, Description = ?, LastWatered = ?, DatePlanted = ?, TotalFruit = ?, HarvestedFruit = ?, FailedFruit = ? WHERE plantID = ?";
                    preparedStatement = DatabaseManager.getConnection().prepareStatement(sql);

                    // Create a prepared statement
                    System.out.println("Provide plantID to update:");
                    inputInt = Integer.parseInt(reader.readLine());
                    preparedStatement.setInt(8, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide plant name:");
                    input = reader.readLine();
                    preparedStatement.setString(1, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);

                    System.out.println("Provide plant Description:");
                    input = reader.readLine();
                    preparedStatement.setString(2, input);
                    stringBuilder.append(" ");
                    stringBuilder.append(input);

                    System.out.println("Provide plant LastWatered (yyyy-mm-dd):");
                    dateinput = Date.valueOf(reader.readLine());
                    preparedStatement.setDate(3, dateinput);
                    stringBuilder.append(" ");
                    stringBuilder.append(dateinput);

                    System.out.println("Provide plant DatePlanted (yyyy-mm-dd):");
                    dateinput = Date.valueOf(reader.readLine());
                    preparedStatement.setDate(4, dateinput);
                    stringBuilder.append(" ");
                    stringBuilder.append(dateinput);

                    System.out.println("Provide plant TotalFruit:");
                    inputInt = Integer.valueOf(reader.readLine());
                    preparedStatement.setInt(5, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide plant HarvestedFruit:");
                    inputInt = Integer.valueOf(reader.readLine());
                    preparedStatement.setInt(6, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    System.out.println("Provide plant FailedFruit:");
                    inputInt = Integer.valueOf(reader.readLine());
                    preparedStatement.setInt(7, inputInt);
                    stringBuilder.append(" ");
                    stringBuilder.append(inputInt);

                    preparedStatement.executeUpdate();

                    System.out.println(stringBuilder);
                    System.out.println("Confirm Commit? y/n");
                    input = reader.readLine().toLowerCase();
                    if (input.equals("y")) {
                        try {
                            DatabaseManager.getConnection().commit();
                        } catch (SQLException se) {
                            System.err.println("Error committing! Rolling back: " + se);
                            DatabaseManager.getConnection().rollback();
                        }
                    } else {
                        System.out.println("Rolling Back");
                        DatabaseManager.getConnection().rollback();
                    }
                    break;
            }

            System.out.println("\nPress [Enter] to continue...");
            reader.readLine();
        }
    }
}
