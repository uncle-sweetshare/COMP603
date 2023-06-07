/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */
package p06_14873443_21145466;

import java.sql.*;
import java.util.Scanner;

public class ShowDetails {

    Seats seatsObj = new Seats(); //new instance of Seats class, for the methods.

    private Connection connection;

    public ShowDetails(Connection connection) {
        this.connection = connection;
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