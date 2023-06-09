/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466
 */

package p06_14873443_21145466;

import java.io.*;

public class Seats {

    /* LoadSeats method:
     * Load seat map from the text file. Show option and date input by user are passed in, these are
     * to select the correct file to load from. The seat configuration in that file is written to an
     * array which is then returned so it can be used to select seats to book.
    */
    public boolean[][] loadSeats(String option, String date) {
        boolean[][] seats = new boolean[10][20];
        String fileLoc = "";

        //Switch to select the file
        switch (option.toLowerCase()) {
            case "a":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsAdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsAdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsAdate3.txt";
                }
                break;
            case "b":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsBdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsBdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsBdate3.txt";
                }
                break;
            case "c":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsCdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsCdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsCdate3.txt";
                }
                break;
            case "d":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsDdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsDdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsDdate3.txt";
                }
                break;
        }

        try {
            java.util.Scanner scanner = new java.util.Scanner(new java.io.File(fileLoc));
            int row = 0;
            while (scanner.hasNextLine() && row < seats.length) {
                String line = scanner.nextLine().trim();
                String[] tokens = line.split(" ");
                for (int col = 0; col < tokens.length && col < seats[0].length; col++) {
                    if (tokens[col].equals("x")) {
                        seats[row][col] = true;
                    }
                }
                row++;
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error loading seats from file! " + e.getMessage());
        }

        return seats;
    }
    
    /* SaveSeats method:
     * Saves seat map to the text file. Show option and date input by user are passed in, these are
     * to select the correct file to save to. The array input parameter is used to read the seat
     * configuration and then write it to the correct seat file.
    */
    public void saveSeats(boolean[][] seats, String option, String date) {
        String fileLoc = "";

        switch (option.toLowerCase()) {
            case "a":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsAdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsAdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsAdate3.txt";
                }
                break;
            case "b":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsBdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsBdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsBdate3.txt";
                }
                break;
            case "c":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsCdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsCdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsCdate3.txt";
                }
                break;
            case "d":
                if ("a".equals(date)) {
                    fileLoc = "./resources/seatsDdate1.txt";
                } else if ("b".equals(date)) {
                    fileLoc = "./resources/seatsDdate2.txt";
                } else if ("c".equals(date)) {
                    fileLoc = "./resources/seatsDdate3.txt";
                }
                break;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileLoc))) {
            for (int row = 0; row < seats.length; row++) {
                for (int col = 0; col < seats[0].length; col++) {
                    if (seats[row][col]) {
                        writer.print("x ");
                    } else {
                        writer.print("o ");
                    }
                }
                writer.println(); //Print \n after each row
            }
        } catch (IOException e) {
            System.out.println("Error saving seats to file! " + e.getMessage());
        }
    }
    
    //M 9/6: minor update to method.
    /* ChooseSeat method:
     * User chooses a seat in the array to book. An array is passed in, along with user's
     * selected row and column. The corresponding seat in the array is flagged to true.
     * If the seat is not available (already flagged to true), a message is printed.
    */
    public void chooseSeat(boolean[][] toBook, int row, int column) {
        boolean[][] seats = toBook;
        
        arrayString(toBook); //Now using arrayString method for GUI

        if (!seats[row][column]) {
            seats[row][column] = true;
        } else {
            System.out.println("Seat " + row + ", " + column + " already booked!");
        }
    }

    //M 9/6: updated method. now uses stringbuilder to turn seating array into a string which is then returned
    public String arrayString(boolean[][] seatsArray) {
        StringBuilder result = new StringBuilder();

        result.append("   "); //For correctly spacing the numbers
        for (int col = 0; col < seatsArray[0].length; col++) {
            result.append(String.format("%2d ", col)); //Append the column number
        }
        result.append("\n");

        for (int row = 0; row < seatsArray.length; row++) {
            result.append(String.format("%2d", row)); //Append the row number
            for (int col = 0; col < seatsArray[0].length; col++) {
                if (seatsArray[row][col]) {
                    result.append("  x");
                } else {
                    result.append("  o");
                }
            }
            result.append("\n"); //Append \n after each row so it's displayed as a grid
        }

        return result.toString(); //Return the created array-string
    }

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
