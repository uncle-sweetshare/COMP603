/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */

package p06_14873443_21145466;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDetails {

    private HashMap<String, User> storeUsers;

    public UserDetails() {
        this.storeUsers = new HashMap<>();
    }

    //M 8/6: creates a user table. WORKING!
    public void createUserTable(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true); //Start connection

            //Check if the users table exists
            ResultSet usersTableResult = connection.getMetaData().getTables(null, null, "users", null);
            boolean usersTableExists = usersTableResult.next();

            if (!usersTableExists) { //If users table doesn't exist, create it
                statement.executeUpdate("CREATE TABLE users ("
                        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "username VARCHAR(255) UNIQUE NOT NULL, "
                        + "password VARCHAR(255) NOT NULL)"
                );

                //Dummy users for testing purposes
                statement.executeUpdate("INSERT INTO users (username, password) VALUES ('user1', 'pass1')");
                statement.executeUpdate("INSERT INTO users (username, password) VALUES ('user2', 'pass2')");

                System.out.println("Users table created successfully.");
            } else {
                System.out.println("Users table already exists.");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while creating database tables: " + e.getMessage());
        }
    }
    
    //yeah i think this works
    public boolean login(Connection connection, String username, String password) {
        boolean matching = false;

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(password)) {
                    matching = true;
                } else {
                    System.out.println("Uh oh, incorrect password :(\n");
                }
            } else {
                System.out.println("Uh oh, username or password is incorrect.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matching;
    }

    //think this works ok
    public boolean register(Connection connection, String username, String password) {
        boolean match = false;

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            storeUsers.put(username, new User(username, password));
            match = true;
        } catch (SQLException e) {
            System.out.println("\nThis user already exists. Login or register a new user.");
        }

        return match;
    }

    //think this works - it should retrieve info from the DB to populate the storeUsers hashmap. just make sure the map is instantiated before calling it
    public void retrieveUsers(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                storeUsers.put(username, new User(username, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //meant to getShowsBooked from the userobj
    public void printHistory(Connection connection, String username) throws SQLException {
        User user = storeUsers.get(username);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        ArrayList<Show> showsBooked = user.getShowsBooked(); //is it an arraylist? it's an arraylist right?
        for (Show show : showsBooked) {
            System.out.println(show.getName());
        }
    }

    //ummmm
    public void saveHistory(Connection connection, String username, String showName) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO bookings (username, showName) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, showName);
            statement.executeUpdate();

            User user = storeUsers.get(username);
            if (user != null) {
                Show addShow = new Show(); //M: "fixed" these couple of lines.
                addShow.setName(showName);
                user.bookShow(addShow); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //toString:
    @Override
    public String toString() {
        return "UserDetails{" + "storeUsers=" + storeUsers + '}';
    }
}