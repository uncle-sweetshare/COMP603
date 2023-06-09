/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */
package p06_14873443_21145466;

import java.util.Scanner;
import java.sql.*;

public class MainMethod {

    public static void main(String[] args) throws SQLException {
        //UserDetails userDetails = new UserDetails();
        Scanner scan = new Scanner(System.in);

        //Start database connection
        try (Connection connection = DriverManager.getConnection("jdbc:derby:booking_boss;create=true")) {

            //dropAllTables(connection); //be careful with this one................ feel free to comment it out
            ShowDetails showDetails = new ShowDetails(connection);
            showDetails.createShowTable(connection); //M 8/6: create the shows table - WORKING
            showDetails.populateShowsTable(); //M 8/6: populate the shows table - WORKING

            //showDetails.printShowDetails(); // M 8/6: retrieve show details from database & print - WORKING
            UserDetails userDetails = new UserDetails();
            userDetails.createUserTable(connection); //M 8/6: create the user table - WORKING
            userDetails.retrieveUsers(connection); //retrieves users from DB - WORKING
            
            LogInAndRegisterJFrame login = new LogInAndRegisterJFrame(showDetails, userDetails);
            login.setVisible(true);
        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database: " + e.getMessage());
        }
    }

    //M 8/6: added this method for testing, don't randomly call it because it will delete all the tables lol
    public static void dropAllTables(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});

            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String dropStatement = "DROP TABLE " + tableName;

                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(dropStatement);
                    System.out.println("Dropped table: " + tableName);
                }
            }

            System.out.println("All tables dropped successfully.");
        } catch (SQLException e) {
            System.out.println("An error occurred while dropping tables: " + e.getMessage());
        }
    }
}