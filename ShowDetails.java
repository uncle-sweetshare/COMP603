/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */
package p06_14873443_21145466;

import java.sql.*;
import java.util.Scanner;

public class ShowDetails {

    Seats seatsObj = new Seats(); //new instance of Seats class, for the methods. do we need this??

    private Connection connection;

    public ShowDetails(Connection connection) {
        this.connection = connection;
    }

    //M 7/6: added this method to populate the show table (duh). still testing.
        public void populateShowsTable() {
        try {
            //trying to create then populate in the same method
            String createShowsTableStatement = "CREATE TABLE IF NOT EXISTS shows ("
                    + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "description VARCHAR(255) NOT NULL, "
                    + "date DATE NOT NULL)";

            connection.createStatement().executeUpdate(createShowsTableStatement);

            //SQL statement to insert show details
            String insertShowStatement = "INSERT INTO shows (name, description, date) VALUES (?, ?, ?)";

            //Show data
            String[] showNames = {"Cats", "Little Shop of Horrors", "Fiddler on the Roof", "Evita"};
            String[] descriptions = {"Cats (1981): Based on the universally popular poetry of T.S. Eliot, Cats tells the story of the annual gathering of Jellicle cats at which time one special cat is selected to ascend to the Heaviside layer.",
                "Little Shop of Horrors (1982): The meek floral assistant, Seymour Krelborn, raises a new breed of carnivorous plant he names “Audrey II” after his coworker crush. Over time, Seymour discovers Audrey II’s evil intent...",
                "Fiddler on the Roof (1971): In pre-revolutionary Russia, Tevye, a poor Jewish peasant living in Anatevka, is faced with the challenge of marrying off his five daughters amidst the growing tension in his village.",
                "Evita (1978): Based on the life of Eva Peron, a B-picture Argentinian actress who eventually became the wife of Argentinian president Juan Domingo Perón, and the most beloved and hated woman in Argentina."};
            String[][] dates = {
                {"24 June", "01 July", "19 July"}, //Dates for Show 1
                {"21 June", "10 July", "26 July"}, //Dates for Show 2
                {"12 June", "27 June", "04 July"}, //Dates for Show 3
                {"18 June", "03 July", "18 July"} //Dates for Show 4
            };

            //Execute SQL statement for each show
            try (PreparedStatement statement = connection.prepareStatement(insertShowStatement)) {
                for (int i = 0; i < showNames.length; i++) {
                    for (int j = 0; j < dates[i].length; j++) {
                        // Set the parameter values for each show and date
                        statement.setString(1, showNames[i]);
                        statement.setString(2, descriptions[i]);
                        statement.setDate(3, Date.valueOf(dates[i][j]));
                        statement.executeUpdate();
                    }
                }
            }
            System.out.println("test print: Shows table populated successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }
    
    //this is meant to like. encompass all the other methods because it pulls data from the DB i think. but don't trust it. it's not right.
    public void printShowDetails() {
        try {
            //run SQL query to retrieve show details from DB
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM shows");
            ResultSet resultSet = statement.executeQuery();

            //display show details
            while (resultSet.next()) {
                String showName = resultSet.getString("name");
                String showTime = resultSet.getString("time");
                String showDate = resultSet.getString("date");
                System.out.println("Show: " + showName + ", Time: " + showTime + ", Date: " + showDate);
            }

            //close the result set and statement NO I DON'T REMEMBER WHAT THAT'S MEANT TO MEAN
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    //the book method, which i somehow DELETED INSTEAD OF JUST CHANGING IT
    public boolean book(Connection connection, String username) {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        System.out.println("Enter the name of the show you want to book: ");
        String showName = scanner.nextLine();

        try {
            //check if the show exists in DB
            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM shows WHERE title = ?");
            checkStatement.setString(1, showName);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                //exists = true, proceed with booking
                int showId = checkResult.getInt("id");

                //check if user has already booked that show
                PreparedStatement bookingCheckStatement = connection.prepareStatement(
                        "SELECT * FROM bookings WHERE username = ? AND showId = ?");
                bookingCheckStatement.setString(1, username);
                bookingCheckStatement.setInt(2, showId);
                ResultSet bookingCheckResult = bookingCheckStatement.executeQuery();

                if (bookingCheckResult.next()) {
                    //user has already booked it
                    System.out.println("You have already booked this show.");
                } else {
                    //user has not booked it, proceed with booking
                    PreparedStatement bookingStatement = connection.prepareStatement(
                            "INSERT INTO bookings (username, showId) VALUES (?, ?)");
                    bookingStatement.setString(1, username);
                    bookingStatement.setInt(2, showId);
                    bookingStatement.executeUpdate();

                    System.out.println("Booking successful. Enjoy the show!");
                }
            } else {
                //show does not exist in DB, maybe make this loop somehow?
                System.out.println("The show does not exist.");
            }

            //close result sets and statements no i don't remember what this does but apparently it needs to be here
            checkResult.close();
            checkStatement.close();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }

        System.out.println("Press 'x' to go back to the main menu or any other key to continue booking.");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("x")) {
            quit = true;
        }

        return quit;
    }
}