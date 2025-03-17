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
        if (message.getMessage_text().length() <= 0) return null;
        if (message.getMessage_text().length() >= 255) return null;
        if (!socialMediaDAO.isValidUserID(message.getPosted_by())) return null;

        return socialMediaDAO.addMessage(message);
    }

    public List<Message> getAllMessages(){
        return socialMediaDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id){
        return socialMediaDAO.getMessageByID(message_id);
    }

    public Message deleteMessageByID(int message_id){
        return socialMediaDAO.deleteMessageByID(message_id);
    }
}

