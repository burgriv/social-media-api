package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * This class handles the HTTP requests made to localhost:8080. Handlers are supplied for each supported request.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * Establishes the valid requests and their respective handlers.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::userRegistrationHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMsgByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
        return app;
    }

    /**
     * Handles user registration requests by communicating with an AccountService object.
     * Response contains the new Account object if successful (status 200), status 400 otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void userRegistrationHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.addAccount(account);
        if (newAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(newAccount));
        }
    }

    /**
     * Handles login requests by communicating with an AccountService object.
     * Response contains the new Account object if successful (status 200), status 401 otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void loginHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.verifyAccount(account);
        if (newAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(newAccount));
        }
    }

    /**
     * Handles message creation requests by communicating with a MessageService object.
     * Response contains the new Message object if successful (status 200), status 400 otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void createNewMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        if (newMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(newMessage));
        }
    }

    /**
     * Handles requests to fetch all messages by communicating with a MessageService object.
     * Response contains a List of Message objects. Status is always 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();
        ctx.json(mapper.writeValueAsString(messages));
    }

    /**
     * Handles fetching of a specific message by message ID by communicating with a MessageService object.
     * message_id is obtained through the path parameter.
     * Response contains the respective Message object if it exists, an empty body otherwise. Status is always 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     */
    private void getMsgByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message newMessage = messageService.getMessageByID(message_id);
        if (newMessage == null) {
            ctx.json("");
        } else {
            ctx.json(mapper.writeValueAsString(newMessage));
        }
    }

    /**
     * Handles message deletion requests by communicating with a MessageService object.
     * message_id is obtained through the path parameter.
     * Response contains the Message object that was deleted if it exists, an empty body otherwise. Status is always 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     */
    private void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByID(message_id);
        if (deletedMessage == null) {
            ctx.json("");
        } else {
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    /**
     * Handles message update requests by communicating with a MessageService object.
     * message_id is obtained through the path parameter.
     * Response contains the Message object that was updated if it exists (status 200), status 400 otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     */
    private void updateMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = mapper.readValue(ctx.body(), Message.class);
        String new_body = message.getMessage_text();
        Message updatedMessage = messageService.updateMessageByID(message_id, new_body);
        if (updatedMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    /**
     * Handles requests to fetch all messages by account_id by communicating with a MessageService object.
     * account_id is obtained through the path parameter.
     * Response contains the List of Message objects by user with account_id. Status is always 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     */
    private void getAllMessagesByUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUser(account_id);
        ctx.json(mapper.writeValueAsString(messages));
    }
}