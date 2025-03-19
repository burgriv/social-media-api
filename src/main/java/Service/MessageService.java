package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Message;

public class MessageService {

    SocialMediaDAO socialMediaDAO;

    public MessageService(){
        socialMediaDAO = new SocialMediaDAO();
    }
    public Message createMessage(Message message){
        if (!isValidMessageBody(message.getMessage_text())) return null;
        if (!socialMediaDAO.isValidUserID(message.getPosted_by())) return null;

        return socialMediaDAO.addMessage(message);
    }

    public Message updateMessageByID(int message_id, String new_body){
        if (!isValidMessageBody(new_body)) return null;
        if (!socialMediaDAO.messageIDExists(message_id)) return null;

        return socialMediaDAO.updateMessageByID(message_id, new_body);
    }

    public List<Message> getAllMessages(){
        return socialMediaDAO.getAllMessages();
    }

    public List<Message> getAllMessagesByUser(int account_id){
        return socialMediaDAO.getAllMessagesByUser(account_id);
    }

    public Message getMessageByID(int message_id){
        return socialMediaDAO.getMessageByID(message_id);
    }

    public Message deleteMessageByID(int message_id){
        return socialMediaDAO.deleteMessageByID(message_id);
    }

    public boolean isValidMessageBody(String message_body){
        if (message_body.length() <= 0) return false;
        if (message_body.length() >= 255) return false;
        return true;
    }
}

