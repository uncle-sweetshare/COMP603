/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI
 */

package p06_14873443_21145466;

import java.sql.*;
import java.util.HashMap;

public class UserDetails
{
    private HashMap<String, User> storeUsers;

    public UserDetails() {
        this.storeUsers = new HashMap<>();
    }

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

    public boolean register(Connection connection, String username, String password) {
        boolean match = false;

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            match = true;
        } catch (SQLException e) {
            System.out.println("\nThis user already exists. Login or register a new user.");
        }

        return match;
    }

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

    public void printHistory(Connection connection, String username) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM bookings WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String showName = resultSet.getString("showName");
                System.out.println(showName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHistory(Connection connection, String username, String showName) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO bookings (username, showName) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, showName);
            statement.executeUpdate();
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