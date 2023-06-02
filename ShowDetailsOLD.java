/*
 * COMP603/03 Project 1, Group 6. Marina Newman 14873443 and Erin Thomas 21145466
 */

package p06_14873443_21145466;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ShowDetailsOLD
{
    Seats seatsObj = new Seats(); //Create instance of Seats to integrate the methods
    
    public ArrayList storeShows;
    
    public ShowDetailsOLD()
    {
        storeShows = new ArrayList();
    }
    
    /*RetrieveShows method:
     * Retrieves the information in the show text files and uses that information to make a new Show object.
    */
    public void retrieveShows()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("./resources/showA.txt")))
        {
            Show showA = new Show();
            showA.setName(reader.readLine());
            showA.setSummary(reader.readLine());
            String[] temp = reader.readLine().split(",");
            showA.setDates(temp);
            storeShows.add(showA);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
                
        try (BufferedReader reader = new BufferedReader(new FileReader("./resources/showB.txt")))
        {
            Show showB = new Show();
            showB.setName(reader.readLine());
            showB.setSummary(reader.readLine());
            String[] temp = reader.readLine().split(",");
            showB.setDates(temp);
            storeShows.add(showB);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader("./resources/showC.txt")))
        {
            Show showC = new Show();
            showC.setName(reader.readLine());
            showC.setSummary(reader.readLine());
            String[] temp = reader.readLine().split(",");
            showC.setDates(temp);
            storeShows.add(showC);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader("./resources/showD.txt")))
        {
            Show showD = new Show();
            showD.setName(reader.readLine());
            showD.setSummary(reader.readLine());
            String[] temp = reader.readLine().split(",");
            
            showD.setDates(temp);
            storeShows.add(showD);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /* Book method:
     * Takes in username as a parameter so it can be passed to the saveHistory method, which saves the show to the user's file.
     * User selects a show to book along with a date, and the correct seating array is loaded based on these choices. The
     * user chooses a seat which is then flagged to booked and saved into the array, and the show is also saved into the user's
     * history file. A ticket is also generated, with the price determined on the seat loaction that the user selected.
    */
    public void book(String username)
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nSelect a show to book");
        
        //Prints a list for the user to choose from
        printShowList();
        
        String selectShow = scan.nextLine();
        
        //User's choice of show:
        if (selectShow.equalsIgnoreCase("a") || selectShow.equalsIgnoreCase("b") ||
        selectShow.equalsIgnoreCase("c") || selectShow.equalsIgnoreCase("d")) {

        int showIndex = selectShow.toLowerCase().charAt(0) - 'a'; //Convert letter to index
        Show selectedShow = (Show) storeShows.get(showIndex);
        
        System.out.println("\nSelect an available date (A-C):");
        String[] dateArray = selectedShow.getDates();
        char letter = 'A';
        
        for (int i = 0; i < dateArray.length; i++) {
            System.out.println(letter + ". " + dateArray[i]);
            letter++;
        }
        
        String selectedDate = scan.nextLine();
        
        UserDetails userDetailsObj = new UserDetails();
        boolean[][] choosingSeats = seatsObj.loadSeats(selectShow,selectedDate); //Load the correct show's seat array based on show and date option chosen
        
        int row = 0;
        int col = 0;
        boolean success = false;
        
        System.out.println("\nSeat availability:\n");
        seatsObj.printArray(choosingSeats);

            do {
                System.out.println("\nEnter seat number to reserve\nRow: ");
                if (scan.hasNextInt()) {
                    row = scan.nextInt();
                    success = true;
                } else {
                    System.out.println("Invalid input! Please enter a row number: ");
                    scan.next();
                }
            } while (!success);


        System.out.println("Column: ");
        col = scan.nextInt();
             
        seatsObj.chooseSeat(choosingSeats, row, col);
        seatsObj.saveSeats(choosingSeats, selectShow, selectedDate); 
        
        int dateIndex = selectedDate.toLowerCase().charAt(0) - 'a';

            if (dateIndex >= 0 && dateIndex < dateArray.length) //If selected date is valid, save booked show to user's file and go to ticket creation
            {
                String showName = selectedShow.getName();
                userDetailsObj.saveHistory(username, showName);
                
                if(row <= 2) //Depending on which row they pick, the tickets will be different prices. Expensive in the front and cheaper at the back.
                {
                    PlatinumTicket ticket = new PlatinumTicket(row);
                    ticket.calcPrice(row);
                    ticket.printPrice();
                    
                }
                else if(row > 2 && row <= 5)
                {
                    GoldTicket ticket = new GoldTicket(row);
                    ticket.calcPrice(row);
                    ticket.printPrice();
                }
                else if(row > 5 && row <= 9)
                {
                    StandardTicket ticket = new StandardTicket(row);
                    ticket.calcPrice(row);
                    ticket.printPrice();
                }
            }
            else
            {
                System.out.println("Invalid date selection.");
            }
        } else {
            System.out.println("Invalid show selection.");
        }
    }
    
    /* PrintShowList method:
     * Prints the name of each show in a list.
    */
    public void printShowList()
    {
        //used chatgpt for the incrimenting letters
        char letter = 'A';
        for(int i = 0;i < storeShows.size(); i++)
        {
            System.out.println(letter+ ". " +storeShows.get(i).toString());
            letter++;
        }
    }
    
    /* PrintShowDetails method:
     * Based on the show chosen by the user, reads the second line from the appropriate
     * text file and prints details to the console.    
    */
    public void printShowDetails(){
        Scanner scan = new Scanner(System.in);
        
        String fileLoc = "";
        
        System.out.println("Select a show to see the description: ");
        String option = scan.nextLine();

        switch (option) {
            case "a":
                fileLoc = "./resources/showA.txt";
                break;
            case "b":
                fileLoc = "./resources/showB.txt";
                break;
            case "c":
                fileLoc = "./resources/showC.txt";
                break;
            case "d":
                fileLoc = "./resources/showD.txt";
                break;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLoc))) {
            reader.readLine(); //Read and discard the first line, which contains just the show's name

            String secondLine = reader.readLine(); //Read and print the second line, which contains the relevant show details
            if (secondLine != null) {
                System.out.println(secondLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}