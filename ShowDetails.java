/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */
package p06_14873443_21145466;

import java.sql.*;
import java.util.Scanner;

public class ShowDetails {

    Seats seatsObj = new Seats();
    UserDetails userDetailsObj = new UserDetails();

    private Connection connection;

    public ShowDetails(Connection connection) {
        this.connection = connection;
    }

    //M 8/6: yeah this works!
    public void createShowTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true); //Start connection

            ResultSet showsTableResult = connection.getMetaData().getTables(null, null, "shows", null);
            boolean showsTableExists = showsTableResult.next();

            //If shows table exists, drop it so it can be recreated
            if (showsTableExists) {
                statement.executeUpdate("DROP TABLE shows");
                System.out.println("Shows table dropped successfully.");
            }

            if (!showsTableExists) { //If shows table doesn't exist, create it
                statement.executeUpdate("CREATE TABLE shows ("
                        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "name VARCHAR(255) NOT NULL, "
                        + "description VARCHAR(255) NOT NULL, "
                        + "date VARCHAR(255) NOT NULL)"
                );

                System.out.println("test print: Shows table created successfully.");
            } else {
                System.out.println("test print: Shows table already exists.");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while creating database tables: " + e.getMessage());
        }
    }

    //M 8/6: populates the show table with details, dates, etc. WORKING!
    public void populateShowsTable() {
        try {
            //SQL statement to insert show details
            String insertShowStatement = "INSERT INTO shows (name, description, date) VALUES (?, ?, ?)";

            //Show data
            String[] showNames = {"Cats", "Little Shop of Horrors", "Fiddler on the Roof", "Evita"};
            String[] descriptions = {
                "Cats (1981): Based on the universally popular poetry of T.S. Eliot, Cats tells the story\nof the annual gathering of Jellicle cats at which time one special cat is selected to ascend to the Heaviside layer.",
                "Little Shop of Horrors (1982): The meek floral assistant, Seymour Krelborn, raises a new\nbreed of carnivorous plant he names “Audrey II” after his coworker crush. Over time, Seymour discovers Audrey II’s evil intent...",
                "Fiddler on the Roof (1971): In pre-revolutionary Russia, Tevye, a poor Jewish peasant living\nin Anatevka, is faced with the challenge of marrying off his five daughters amidst the growing tension in his village.",
                "Evita (1978): Based on the life of Eva Peron, a B-picture Argentinian actress who eventually\nbecame the wife of Argentinian president Juan Domingo Perón, and the most beloved and hated woman in Argentina."
            };
            String[][] dates = {
                {"24 June, 01 July, 13 July"}, // Dates for Show 1
                {"21 June, 10 July, 17 July"}, // Dates for Show 2
                {"12 June, 27 June, 07 July"}, // Dates for Show 3
                {"18 June, 03 July, 19 July"} // Dates for Show 4
            };

            //Execute SQL statement for each show
            try (PreparedStatement statement = connection.prepareStatement(insertShowStatement)) {
                for (int i = 0; i < showNames.length; i++) {
                    String showName = showNames[i];
                    String description = descriptions[i];
                    String[] showDates = dates[i];

                    //Set the parameter values for each show
                    statement.setString(1, showName);
                    statement.setString(2, description);

                    for (String date : showDates) { //Iterate with enhanced loop for the 3 different dates
                        statement.setString(3, date);
                        statement.executeUpdate();
                    }
                }
            }
            System.out.println("Shows table populated successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    //M 8/6: retrieves show details from DB and prints them. WORKING!
    public void printShowDetails() {
        try {
            //Run SQL query to retrieve show details from DB
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM shows");
            ResultSet resultSet = statement.executeQuery();

            //Display show details
            while (resultSet.next()) {
                String showName = resultSet.getString("name");
                String showDescription = resultSet.getString("description");
                String showDates = resultSet.getString("date");

                System.out.println("Name: " + showName);
                System.out.println("Description: " + showDescription);
                System.out.println("Dates: " + showDates);
                System.out.println();
            }

            //Close the result set and statement (no i still don't know what this means but apparently it is necessary)
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    //M 9/6: added new method that makes a string of the show details - it's kind of generic code and i haven't tested the formatting so if it prints weirdly, soz i will fix it
    public String getShowDetails(Connection connection) {
        StringBuilder result = new StringBuilder();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM shows");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String date = resultSet.getString("date");

                result.append("Show Name: ").append(name).append("\n");
                result.append("Description: ").append(description).append("\n");
                result.append("Date: ").append(date).append("\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }    
}