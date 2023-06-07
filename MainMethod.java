/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */

package p06_14873443_21145466;

import java.util.Scanner;
import java.sql.*;

public class MainMethod {

    public static void main(String[] args) {
        //ET: feel free to undo what i've done, im just havin a bit of a wiggle.
        //ShowDetails showDetails = new ShowDetails(); //ET: testing to see if this needs to be moved down til after the database is created.
        UserDetails userDetails = new UserDetails();
        Scanner scan = new Scanner(System.in);
        
        //start database connection
        try (Connection connection = DriverManager.getConnection("jdbc:derby:booking_boss;create=true")) {
           //create database tables if they don't exist
            createTables(connection);

            ShowDetails showDetails = new ShowDetails(connection);
            showDetails.populateShowsTable(); //M 7/6: calling method to test
            showDetails.printShowDetails();
            
            userDetails.retrieveUsers(connection); //retrieves users from DB
            showDetails.printShowDetails(); //M: changed the method call here. //retrieves shows from DB

            String userInput = "";
            String username = "";
            String password = "";
            boolean matching = false;

            System.out.println("Welcome to Booking Boss. Press 'x' to exit.");

            do {
                System.out.println("A. Log in");
                System.out.println("B. Register");
                userInput = scan.nextLine();

                if (userInput.equalsIgnoreCase("a")) {
                    System.out.println("\nUsername:");
                    username = scan.nextLine();

                    if (username.equalsIgnoreCase("x")) {
                        break;
                    }

                    System.out.println("Password:");
                    password = scan.nextLine();

                    if (password.equalsIgnoreCase("x")) {
                        break;
                    }

                    matching = userDetails.login(connection, username, password); //it's fixed now but doesn't work for echo
                } else if (userInput.equalsIgnoreCase("b")) {
                    System.out.println("Username:");
                    username = scan.nextLine();

                    if (username.equalsIgnoreCase("x")) {
                        break;
                    }

                    System.out.println("Password:");
                    password = scan.nextLine();

                    if (password.equalsIgnoreCase("x")) {
                        break;
                    }

                    matching = userDetails.register(connection, username, password); //as above
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

                    String selected = scan.nextLine();
                    if (selected.equalsIgnoreCase("x")) {
                        break;
                    }

                    if (selected.equalsIgnoreCase("a")) {
                        System.out.println("\nAvailable shows:");
                        showDetails.printShowDetails(); //changed this from List to Details, need to test
                        showDetails.printShowDetails(); //ehhhhh i dunno
                    } else if (selected.equalsIgnoreCase("b")) {
                        System.out.println("\n" + username + "'s previously booked shows: ");
                        userDetails.printHistory(connection, username);
                    } else if (selected.equalsIgnoreCase("c")) {
                        boolean quit = showDetails.book(connection, username); //ET: i changed the constructer to include connection. Just so the error went away, it doesn't do anything different.

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

    //M 7/6: added this to replace old GARBAGE method. it does indeed create the table properly
    public static void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false); //Start connection

            //Check if the users table exists
            ResultSet usersTableResult = connection.getMetaData().getTables(null, null, "users", null);
            boolean usersTableExists = usersTableResult.next();
            
            /*
            //Check if the shows table exists
            ResultSet showsTableResult = connection.getMetaData().getTables(null, null, "shows", null);
            boolean showsTableExists = showsTableResult.next(); */

            if (!usersTableExists) { //If users table doesn't exist, create it
                statement.executeUpdate("CREATE TABLE users ("
                        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "username VARCHAR(255) UNIQUE NOT NULL, "
                        + "password VARCHAR(255) NOT NULL)"
                );

                //Dummy users for testing
                statement.executeUpdate("INSERT INTO users (username, password) VALUES ('user1', 'pass1')");
                statement.executeUpdate("INSERT INTO users (username, password) VALUES ('user2', 'pass2')");

                System.out.println("Users table created successfully.");
            } else {
                System.out.println("Users table already exists.");
            }

            /*
            if (!showsTableExists) { //If shows table doesn't exist, create it
                statement.executeUpdate("CREATE TABLE shows ("
                        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "name VARCHAR(255) NOT NULL, "
                        + "description VARCHAR(255) NOT NULL, "
                        + "date DATE NOT NULL)"
                );

                System.out.println("Shows table created successfully.");
            } else {
                System.out.println("Shows table already exists.");
            } */

            connection.commit(); //Commit!

        } catch (SQLException e) {
            System.out.println("An error occurred while creating database tables: " + e.getMessage());
        }
    }

    //insert new user into the users table - call this somewhere or maybe move to UserDetails class? did i already put something there?
    private static void insertUser(Connection connection, String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }
    }
}