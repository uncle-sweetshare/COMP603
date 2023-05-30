/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */

package p06_14873443_21145466;

import java.sql.*;
import java.io.*;

public class Seats {
    
    //added this for db connectgion - oops i made it all network mode before instead of embedded *<:^) fkn clownmode
    private static final String DB_URL = "jdbc:derby:seats_database;create=true";

    //**********************UPDATED THIS METHOD FOR DB STUFF
    /* LoadSeats method:
     * Load seat map from the text file. Show option and date input by user are passed in, these are
     * to select the correct file to load from. The seat configuration in that file is written to an
     * array which is then returned so it can be used to select seats to book.
    */
public boolean[][] loadSeats(String option, String date) {
        boolean[][] seats = new boolean[10][20];

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM seats WHERE option = ? AND date = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, option);
            pstmt.setString(2, date);

            ResultSet rs = pstmt.executeQuery();

            int row = 0;
            while (rs.next() && row < seats.length) {
                String rowData = rs.getString("seat_data");
                String[] tokens = rowData.split(" ");
                for (int col = 0; col < tokens.length && col < seats[0].length; col++) {
                    if (tokens[col].equals("x")) {
                        seats[row][col] = true;
                    }
                }
                row++;
            }
        } catch (SQLException e) {
            System.out.println("Error loading seats from database: " + e.getMessage());
        }

        return seats;
    }
    
    //**********************UPDATED THIS METHOD FOR DB STUFF i don't know what happened to my switch
    /* SaveSeats method:
     * Saves seat map to the text file. Show option and date input by user are passed in, these are
     * to select the correct file to save to. The array input parameter is used to read the seat
     * configuration and then write it to the correct seat file.
    */
public void saveSeats(boolean[][] seats, String option, String date) {
        try (Connection conn = DriverManager.getConnection(DB_URL); //is this........ need to change....... placeholder URL??? no it'll create a new table if no exist
             Statement stmt = conn.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS seats (option VARCHAR(10), date VARCHAR(10), seat_data CLOB)";
            stmt.executeUpdate(createTableQuery);

            // Clear existing data for the given option and date
            String deleteQuery = "DELETE FROM seats WHERE option = ? AND date = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setString(1, option);
            deleteStmt.setString(2, date);
            deleteStmt.executeUpdate();

            // Insert new seat data
            String insertQuery = "INSERT INTO seats (option, date, seat_data) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, option);
            insertStmt.setString(2, date);

            StringBuilder seatDataBuilder = new StringBuilder();
            for (int row = 0; row < seats.length; row++) {
                for (int col = 0; col < seats[0].length; col++) {
                    seatDataBuilder.append(seats[row][col] ? "x " : "o ");
                }
                seatDataBuilder.append("\n");
            }

            insertStmt.setString(3, seatDataBuilder.toString().trim());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving seats to database: " + e.getMessage());
        }
    }
    
    //******************************NO CHANGES TO THIS (yet?)
    /* ChooseSeat method:
     * User chooses a seat in the array to book. An array is passed in, along with user's
     * selected row and column. The corresponding seat in the array is flagged to true.
     * If the seat is not available (already flagged to true), a message is printed.
    */
    public void chooseSeat(boolean[][] toBook, int row, int column) {
        boolean[][] seats = toBook;
        
        printArray(toBook);

        if (!seats[row][column]) {
            seats[row][column] = true;
        } else {
            System.out.println("Seat " + row + ", " + column + " already booked!");
        }
    }

    //******************************NO CHANGES TO THIS
    /* PrintArray method:
     * Prints the array that is passed in, with row and column numbers for clarity.
    */
    public void printArray(boolean[][] seatsArray) {
        System.out.print("   "); //For correctly spacing the numbers
        for (int col = 0; col < seatsArray[0].length; col++) {
            System.out.printf("%2d ", col); //Print the column number
        }
        System.out.println();

        for (int row = 0; row < seatsArray.length; row++) {
            System.out.printf("%2d", row); //Print the row number
            for (int col = 0; col < seatsArray[0].length; col++) {
                if (seatsArray[row][col]) {
                    System.out.print("  x");
                } else {
                    System.out.print("  o");
                }
            }
            System.out.println(); //Print \n after each row so it's displayed as a grid
        }
    }
}