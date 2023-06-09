/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */
package p06_14873443_21145466;

import java.util.Scanner;
import java.sql.*;

public class MainMethod {

    public static void main(String[] args) throws SQLException {
        //ET: feel free to undo what i've done, im just havin a bit of a wiggle.
        //ShowDetails showDetails = new ShowDetails(); //ET: testing to see if this needs to be moved down til after the database is created.
        UserDetails userDetails = new UserDetails();
        Scanner scan = new Scanner(System.in);

        //Start database connection
        try (Connection connection = DriverManager.getConnection("jdbc:derby:booking_boss;create=true")) {
            
            //dropAllTables(connection); //be careful with this one................ feel free to comment it out
            
            ShowDetails showDetails = new ShowDetails(connection);
            showDetails.createShowTable(connection); //M 8/6: create the shows table - WORKING
            showDetails.populateShowsTable(); //M 8/6: populate the shows table - WORKING
            
            //showDetails.printShowDetails(); // M 8/6: retrieve show details from database & print - WORKING
            userDetails.createUserTable(connection); //M 8/6: create the user table - WORKING
            userDetails.retrieveUsers(connection); //retrieves users from DB - WORKING
            
            String userInput = "";
            String username = "";
            String password = "";
            boolean matching = false;

            System.out.println("Welcome to Booking Boss. Press 'x' to exit.");

        do {
            System.out.println("A. Log in");
            System.out.println("B. Register");

            if (scan.hasNextLine()) {
                userInput = scan.nextLine();
            } else {
                System.out.println("No input available. Exiting...");
                break;
            }

            if (userInput.equalsIgnoreCase("a")) {
                System.out.println("\nUsername:");

                if (scan.hasNextLine()) {
                    username = scan.nextLine();
                } else {
                    System.out.println("No input available. Exiting...");
                    break;
                }

                if (username.equalsIgnoreCase("x")) {
                    break;
                }

                System.out.println("Password:");

                if (scan.hasNextLine()) {
                    password = scan.nextLine();
                } else {
                    System.out.println("No input available. Exiting...");
                    break;
                }

                if (password.equalsIgnoreCase("x")) {
                    break;
                }

                matching = userDetails.login(connection, username, password);
            } else if (userInput.equalsIgnoreCase("b")) {
                System.out.println("Username:");

                if (scan.hasNextLine()) {
                    username = scan.nextLine();
                } else {
                    System.out.println("No input available. Exiting...");
                    break;
                }

                if (username.equalsIgnoreCase("x")) {
                    break;
                }

                System.out.println("Password:");

                if (scan.hasNextLine()) {
                    password = scan.nextLine();
                } else {
                    System.out.println("No input available. Exiting...");
                    break;
                }

                if (password.equalsIgnoreCase("x")) {
                    break;
                }

                matching = userDetails.register(connection, username, password);
            } else if (userInput.equalsIgnoreCase("x")) {
                System.out.println("Thanks for using Booking Boss!");
                break;
            } else {
                System.out.println("\nPlease select one of the options or press 'x' to exit!");
            }
        } while (!matching && !userInput.equalsIgnoreCase("x"));

            if (matching) {
                System.out.println("\nWelcome " + username + "!");
                do {
                    System.out.println("\nPlease select an action (A - C) or press 'x' to exit.");
                    System.out.println("A. List available shows");
                    System.out.println("B. View booked shows");
                    System.out.println("C. Purchase show tickets");

                    if (scan.hasNextLine()) {
                        String selected = scan.nextLine();
                        if (selected.equalsIgnoreCase("x")) {
                            break;
                        }

                        if (selected.equalsIgnoreCase("a")) {
                            System.out.println("\nAvailable shows:");
                        showDetails.printShowDetails();
                    } else if (selected.equalsIgnoreCase("b")) {
                        System.out.println("\n" + username + "'s previously booked shows: ");
                        userDetails.printHistory(connection, username);
                    } else if (selected.equalsIgnoreCase("c")) {
                        boolean quit = showDetails.book(username);
                        
                        if (quit) {
                            break;
                        }
                    }
                } while (true);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database! " + e.getMessage());
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