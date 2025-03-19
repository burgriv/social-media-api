package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Message;

public class MessageService {

    SocialMediaDAO socialMediaDAO;

    public MessageService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    /**
     * Checks to make sure message is valid and user exists, then calls the DAO to persist it to the database.
     * @param message The Message object to create in the database.
     * @return The Message object added to the database. Returns null on failure.
     */
    public Message createMessage(Message message){
        if (!isValidMessageBody(message.getMessage_text())) return null;
        if (!socialMediaDAO.accountIDExists(message.getPosted_by())) return null;

        return socialMediaDAO.addMessage(message);
    }

    /**
     * Checks to make sure message is valid and exists, then calls the DAO to persist the update to the database.
     * @param message_id The message_id of the message to be updated.
     * @param new_body The new message_text to replace the existing text.
     * @return The Message object updated in the database. Returns null on failure.
     */
    public Message updateMessageByID(int message_id, String new_body){
        if (!isValidMessageBody(new_body)) return null;
        if (!socialMediaDAO.messageIDExists(message_id)) return null;

        return socialMediaDAO.updateMessageByID(message_id, new_body);
    }

    /**
     * Calls DAO to request all messages from the database.
     * @return The List of all Message objects obtained by the DAO.
     */
    public List<Message> getAllMessages(){
        return socialMediaDAO.getAllMessages();
    }

    /**
     * Calls DAO to request all messages from the user specified by account_id.
     * @param account_id The account_id of the user whose messages are requested.
     * @return The List of all Message objects, written by the user with account_id, obtained by the DAO.
     */
    public List<Message> getAllMessagesByUser(int account_id){
        return socialMediaDAO.getAllMessagesByUser(account_id);
    }

    /**
     * Calls DAO to request the message specified by message_id.
     * @param message_id The message_id of the requested message.
     * @return The Message object requested from the database. Returns null on failure.
     */
    public Message getMessageByID(int message_id){
        return socialMediaDAO.getMessageByID(message_id);
    }

    /**
     * Calls DAO to delete the message specified by message_id.
     * @param message_id The message_id of the requested message to delete.
     * @return The Message object that was deleted.
     */
    public Message deleteMessageByID(int message_id){
        return socialMediaDAO.deleteMessageByID(message_id);
    }

    /**
     * Validates a message. A message is valid if it is not blank and its length is fewer than 255 characters.
     * @param message_body The message_text to validate.
     * @return true is message is valid, false otherwise.
     */
    public boolean isValidMessageBody(String message_body){
        if (message_body.length() <= 0) return false;
        if (message_body.length() >= 255) return false;
        return true;
    }
}

