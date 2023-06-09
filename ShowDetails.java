/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */
package p06_14873443_21145466;

import java.sql.*;
import java.util.Scanner;

public class ShowDetails {

    Seats seatsObj = new Seats();

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

    //M 9/6: i hate this method i am working on it but i hate it
    /* Book method:
     * Takes in username as a parameter so it can be passed to the saveHistory method, which saves the show to the user's file.
     * User selects a show to book along with a date, and the correct seating array is loaded based on these choices. The
     * user chooses a seat which is then flagged to booked and saved into the array, and the show is also saved into the user's
     * history file. A ticket is also generated, with the price determined on the seat loaction that the user selected.
     */
    public boolean book(String username) {
        Scanner scan = new Scanner(System.in);
        boolean quit = false;

        System.out.println("\nEnter a show name to book:");
        String showNameInput = scan.nextLine();

        String showChoice = "";
        String dateChoice = "";

        if (showNameInput.equalsIgnoreCase("x")) {
            quit = true;
        } else if (showNameInput != null) {
            switch (showNameInput.toLowerCase()) {
                case "cats":
                    showChoice = "a";
                    break;
                case "little shop of horrors":
                    showChoice = "b";
                    break;
                case "fiddler on the roof":
                    showChoice = "c";
                    break;
                case "evita":
                    showChoice = "d";
                    break;
                default:
                    System.out.println("Show not found!");
                    break;
            }
        } else {
            System.out.println("Show not found!");
        }
        if (!quit) {
            System.out.println("\nEnter a date:");
            String dateInput = scan.nextLine();

            if (showChoice.equals("a")) {
                switch (dateInput.toLowerCase()) {
                    case "24 june":
                        dateChoice = "a";
                        break;
                    case "01 july":
                        dateChoice = "b";
                        break;
                    case "13 july":
                        dateChoice = "c";
                        break;
                    default:
                        System.out.println("Invalid date!");
                        break;
                }
            } else if (showChoice.equals("b")) {
                switch (dateInput.toLowerCase()) {
                    case "21 june":
                        dateChoice = "a";
                        break;
                    case "10 july":
                        dateChoice = "b";
                        break;
                    case "17 july":
                        dateChoice = "c";
                        break;
                    default:
                        System.out.println("Invalid date!");
                        break;
                }
            } else if (showChoice.equals("c")) {
                switch (dateInput.toLowerCase()) {
                    case "12 june":
                        dateChoice = "a";
                        break;
                    case "27 june":
                        dateChoice = "b";
                        break;
                    case "07 july":
                        dateChoice = "c";
                        break;
                    default:
                        System.out.println("Invalid date!");
                        break;
                }
            } else if (showChoice.equals("d")) {
                switch (dateInput.toLowerCase()) {
                    case "18 june":
                        dateChoice = "a";
                        break;
                    case "03 july":
                        dateChoice = "b";
                        break;
                    case "19 july":
                        dateChoice = "c";
                        break;
                    default:
                        System.out.println("Invalid date!");
                        break;
                }
            }
        }

                boolean[][] choosingSeats = seatsObj.loadSeats(showChoice, dateChoice); //Load the correct show's seat array based on show and date option chosen

                System.out.println("\nSeat availability:\n");
                seatsObj.printArray(choosingSeats);

                int row = 0;
                int col = 0;
                
                boolean success = false;
                
                do {
                    System.out.println("\nEnter seat number to reserve\nRow: ");
                    if (scan.hasNextInt()) {
                        row = scan.nextInt();
                        success = true;
                    } else {
                        String input = scan.nextLine();
                        if(input.equalsIgnoreCase("x")){
                            quit = true;
                            break;
                        }
                        else{
                            System.out.println("Invalid input! Please enter a row number: ");
                        }
                    }
                } while (!success);
                
                if(!quit)
                {
                    success = false;
                    
                    do {
                        System.out.println("\nColumn: ");
                        if (scan.hasNextInt()) {
                            col = scan.nextInt();
                            success = true;
                        } else {
                            String input = scan.nextLine();
                            if(input.equalsIgnoreCase("x")){
                                quit = true;
                                break;
                            }
                            else{
                                System.out.println("Invalid input! Please enter a column number: ");
                            }
                        }
                    } while (!success);
                }
                
                //fixing stuff below here
                
                
                if(!quit)
                {
                    seatsObj.chooseSeat(choosingSeats, row, col);
                    seatsObj.saveSeats(choosingSeats, showChoice, dateChoice);

                    int dateIndex = dateChoice.toLowerCase().charAt(0) - 'a';

                    if (dateIndex >= 0 && dateIndex < dateArray.length) //If selected date is valid, save booked show to user's file and go to ticket creation
                    {
                        String showName = selectedShow.getName();
                        userDetailsObj.saveHistory(connection, username, showName);

                        if (row <= 2) //Depending on which row they pick, the tickets will be different prices. Expensive in the front and cheaper at the back.
                        {
                            PlatinumTicket ticket = new PlatinumTicket(row);
                            ticket.calcPrice(row);
                            ticket.printPrice();

                        } else if (row > 2 && row <= 5) {
                            GoldTicket ticket = new GoldTicket(row);
                            ticket.calcPrice(row);
                            ticket.printPrice();
                        } else if (row > 5 && row <= 9) {
                            StandardTicket ticket = new StandardTicket(row);
                            ticket.calcPrice(row);
                            ticket.printPrice();
                        }
                    } else {
                        System.out.println("Invalid date selection.");
                    }
                }
        } else {
            System.out.println("Invalid show selection.");
        }
    return quit ;
}
    }