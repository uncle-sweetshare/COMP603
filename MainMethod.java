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

        //showDetails.printShowList(); //this method needs to be redone in showDetails. or separate it from the dispay show stuff method or something
        //ET: isn't it called printShowDetails? there isn't a printShowList method.
        
        //DB changes begin here:
        //start database connection
        try (Connection connection = DriverManager.getConnection("jdbc:derby:database_name;create=true")) { //database_name is a placeholder, it needs to be the actual database's actual name ("booking_boss"?)m 
           //create database tables if they don't exist
            createTables(connection);

            ShowDetails showDetails = new ShowDetails(connection);
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
            System.out.println("An error occurred while connecting to the database: " + e.getMessage());
        }
    }

    //helper method creates DB tables if they don't exist
    private static void createTables(Connection connection) throws SQLException {
        String createUserTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "username VARCHAR(255) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL)";

        String createShowTable = "CREATE TABLE IF NOT EXISTS shows ("
                + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "title VARCHAR(255) NOT NULL, "
                + "date DATE NOT NULL, "
                + "time TIME NOT NULL)";

        //run SQL statements to create tables
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createUserTable);
            statement.executeUpdate(createShowTable);
        }
    }

    //insert new user into the users table - call this somewhere
    private static void insertUser(Connection connection, String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }
    }

    //insert new show into the shows table - call this somewhere
    private static void insertShow(Connection connection, String title, Date date, Time time) throws SQLException {
        String sql = "INSERT INTO shows (title, date, time) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setDate(2, date);
            statement.setTime(3, time);
            statement.executeUpdate();
        }
    }
}