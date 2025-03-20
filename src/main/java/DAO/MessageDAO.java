package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /**
     * Attempts to connect to the database and add message as a new record to the message table.
     * @param message The Message object to persist to the database.
     * @return The Message object after persisting it to the database. Returns null on failure.
     */
    public Message addMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int message_id = rs.getInt(1);
                return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to connect to the database and fetch the message specified by message_id in the message table.
     * @param message_id The message_id to search for in the database.
     * @return The Message object related to message_id, if it exists. Returns null otherwise.
     */
    public Message getMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to connect to the database and fetch all messages in the message table.
     * @return The List of Message objects representing all rows in the message table.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                messages.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Attempts to connect to the database and fetch all messages by account_id in the message table.
     * @param account_id The account_id which specifies the author of the messages to be fetched.
     * @return The List of Message objects written by account_id in the message table.
     */
    public List<Message> getAllMessagesByUser(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                messages.add(new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4)));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Attempts to connect to the database and delete the message specified by message_id in the message table, if it exists.
     * @param message_id The message_id to search for in the database.
     * @return The Message object that was deleted, if it exists. Returns null otherwise.
     */
    public Message deleteMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            Message message = getMessageByID(message_id);

            String sql = "DELETE FROM message WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            preparedStatement.execute();
            // message will be null if it didn't exist in the first place.
            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to connect to the database and update the message specified by message_id in the message table, if it exists.
     * It will overwrite message_text with new_body.
     * @param message_id The message_id of the Message to be updated.
     * @param new_body The new message_text to replace the existing text.
     * @return The Message object that was updated, if it exists. Returns null otherwise.
     */
    public Message updateMessageByID(int message_id, String new_body){
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "UPDATE message SET message_text = ? WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, new_body);
            preparedStatement.setInt(2, message_id);
            preparedStatement.execute();
            Message message = getMessageByID(message_id);
            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to connect to the database and check if message_id is present in the message table.
     * @param message_id The message_id to search for in the database.
     * @return true if message_id is currently being used by an Account, false otherwise.
     */
    public boolean messageIDExists(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            if (preparedStatement.executeQuery().next()) return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
