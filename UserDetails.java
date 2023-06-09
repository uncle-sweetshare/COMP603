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

    //M 9/6: creates a user table. WORKING! added history field
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
                        + "password VARCHAR(255) NOT NULL, "
                        + "history VARCHAR(255) DEFAULT NULL)"
                );

                //Dummy users for testing purposes
                statement.executeUpdate("INSERT INTO users (username, password, history) VALUES ('user1', 'pass1', 'Cats')");
                statement.executeUpdate("INSERT INTO users (username, password, history) VALUES ('user2', 'pass2', 'Evita')");

                System.out.println("Users table created successfully.");
            } else {
                    System.out.println("Users table already exists.");
                }
            

        } catch (SQLException e) {
            System.out.println("An error occurred while creating database tables: " + e.getMessage());
        }
    }
    
    //M 9/8: updated slightly. still users table not found in GUI???
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

    //M 9/8: updated slightly, hopefully should be working now???
    public boolean register(Connection connection, String username, String password) {
        boolean match = false;

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, history) VALUES (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setNull(3, Types.VARCHAR); //Sets history to null
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                storeUsers.put(username, new User(username, password));
                match = true;
            }
        } catch (SQLException e) {
            System.out.println("\nError! This user already exists. Login or register a new user." + e.getMessage());
        }

        return match;
    }

    //M 9/8: think this works - it should retrieve info from the DB to populate the storeUsers hashmap. just make sure the map is instantiated before calling it
    public void retrieveUsers(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String history = resultSet.getString("history");
                storeUsers.put(username, new User(username, password));
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while retrieving users: " + e.getMessage());
        }
    }

    //M 9/8: updated, should return a string of the user's history. it doesn't really print the history any more but it's still called printHistory
    //gets user's history from database, uses stringbuilder to create a string to return - past shows on different lines. if no shows found, print an error
    public String printHistory(Connection connection, String username) throws SQLException {
        StringBuilder historyBuilder = new StringBuilder();

        try (PreparedStatement statement = connection.prepareStatement("SELECT history FROM users WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String history = resultSet.getString(1);
                if (history != null && !history.isEmpty()) {
                    historyBuilder.append(history);
                    historyBuilder.append("\n");
                } else {
                    historyBuilder.append("No history found for user ").append(username);
                }
            } else {
                historyBuilder.append("User not found: ").append(username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyBuilder.toString();
    }

    //M 9/8: append showName to user's history in user table
    public void saveHistory(Connection connection, String username, String showName) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE users SET history = CASE WHEN history IS NULL THEN ? ELSE history || ? END WHERE username = ?")) { //Concat with ||
            statement.setString(1, showName + "\n");
            statement.setString(2, showName + "\n");
            statement.setString(3, username);
            statement.executeUpdate();

            User user = storeUsers.get(username);
            if (user != null) {
                Show addShow = new Show();
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