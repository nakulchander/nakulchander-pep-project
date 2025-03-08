package DAO;

import java.sql.*;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    //Method for new user account and returning added account object
    public Account addUser(Account account) {
        String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getUsername());   //setting username parameter
            stmt.setString(2, account.getPassword());   //setting password parameter
            // Execute the update and check if one row was affected
            if (stmt.executeUpdate() == 1) {
                try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        account.setAccount_id(resultSet.getInt(1));
                    }
                }
                return account; //return updated account object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //return null is no user added
    }

    // Method to search for an account by username and return the found account object
    // Get account_id from result set
    // Get username from result set
    // Get password from result set
    public Account searchByUser(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);    // Set the username parameter in the SQL statement
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new Account(
                            resultSet.getInt("account_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    // Return null if no account is found
    }

    // Method to search for an account by user ID and return the found account object
    // Get account_id from result set
    // Get username from result set
    // Get password from result set
    public Account searchById(int userId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new Account(
                            resultSet.getInt("account_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();    // Print stack trace if exception occurs
        }
        return null;    // Return null if no account is found
    }
}
