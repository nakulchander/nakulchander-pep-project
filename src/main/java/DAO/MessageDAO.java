package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Method to create a new message in the database
// Prepare SQL statement with RETURN_GENERATED_KEYS to get the generated key
public class MessageDAO {
    public Message createMessage(Message message) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());   // Set the message_text parameter
            stmt.setLong(3, message.getTime_posted_epoch());

            int affectedRows = stmt.executeUpdate();    // Execute the SQL statement and get the number of rows affected
            if (affectedRows == 0) {
                throw new SQLException("create failed");    // Throw an exception if no rows were affected
            }

            try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    message.setMessage_id(resultSet.getInt(1));
                } else {
                    throw new SQLException("no generated key obtained.");
                }
            }
            return message; // Return the updated message object with the generated key
        } catch (SQLException e) {
            e.printStackTrace();
            return null;    // Return null if an error occurred
        }
    }

    // Method to retrieve all messages from the database
    // Prepare SQL statement to select all messages
    public List<Message> retrieveAllMessages() {
        List<Message> messages = new ArrayList<>();     // Create a list to hold the retrieved messages
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM message");
             ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;    // Return the list of retrieved messages
    }

    // Method to retrieve a message by its ID
    // Prepare SQL statement to select a specific message by ID
    public Message retrieveMessageById(int mId) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM message WHERE message_id = ?")) {
            stmt.setInt(1, mId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new Message(
                            resultSet.getInt("message_id"),
                            resultSet.getInt("posted_by"),
                            resultSet.getString("message_text"),
                            resultSet.getLong("time_posted_epoch")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    // Return null if no message was found with the given ID
    }

    // Method to delete a message by its ID
    // Prepare SQL statement to delete a specific message by ID
    public Message deleteMessageByMessageId(int mId) {
        Message message = retrieveMessageById(mId);
        if (message != null) {
            try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("DELETE FROM message WHERE message_id = ?")) {
                stmt.setInt(1, mId);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    return message;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;    // Return null if deletion failed
    }

    // Method to update a message by its ID
    // Prepare SQL statement to update a specific message by ID
    public Message updateMessageByMessageId(int mId, Message message) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?")) {
            stmt.setString(1, message.getMessage_text());
            stmt.setInt(2, mId);
            int updatedRows = stmt.executeUpdate();
            if (updatedRows > 0) {
                return retrieveMessageById(mId);
            } else {
                return null;    // Return null if no rows were affected
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;    // Return null if an error occurred during the update
        }
    }

    // Method to retrieve all messages posted by a specific user ID
    // Prepare SQL statement to select all messages by a specific user ID
    public List<Message> retrieveAllMessagesForUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM message WHERE posted_by = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    messages.add(new Message(
                            resultSet.getInt("message_id"),
                            resultSet.getInt("posted_by"),
                            resultSet.getString("message_text"),
                            resultSet.getLong("time_posted_epoch")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;    // Return the list of retrieved messages
    }
}