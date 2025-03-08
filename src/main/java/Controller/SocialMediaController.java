package Controller;

import com.fasterxml.jackson.databind.ObjectMapper; // Library for JSON processing.
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

// Social Media controller class for handling API endpoints related to social media api functionality.
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;
    private final ObjectMapper mapper;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.mapper = new ObjectMapper();
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //Below are the endpoints for this program
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::retrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::retrieveMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByMessageIdHandler);
        app.get("/accounts/{account_id}/messages", this::retrieveAllMessagesForUserHandler);

        return app;
    }

    // Handler method for the "/register" endpoint.
    private void registerHandler(Context context) {
        try {
            Account account = mapper.readValue(context.body(), Account.class);  // Parse JSON request body to Account object.
            Account createdAccount = accountService.register(account);
            context.json(createdAccount).status(200);   // Return created account as JSON with 200 status.
        } catch (Exception e) {
            context.status(400);    // Return 400 status if an error occurs during registration.
        }
    }

    // Handler method for the "/login" endpoint. Logs in an existing user.
    private void loginHandler(Context context) {
        try {
            Account account = mapper.readValue(context.body(), Account.class);
            Account createdAccount = accountService.login(account);
            context.json(createdAccount).status(200);   // Return logged-in account as JSON with 200 status.
        } catch (Exception e) {
            context.status(401);     // Return 401 status if login fails or credentials are invalid.
        }
    }

    // Handler method for the "/messages" endpoint. Creates a new message.
    private void createMessageHandler(Context context) {
        try {
            Message message = mapper.readValue(context.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
            context.json(createdMessage).status(200);   // Return created message as JSON with 200 status.
        } catch (Exception e) {
            context.status(400);         // Return 400 status if an error occurs during message creation.
        }
    }

    // Handler method for the "/messages" endpoint. Retrieves all messages.
    private void retrieveAllMessagesHandler(Context context) {
        List<Message> messages = messageService.retrieveAllMessages();  // Call service layer to get all messages.
        context.json(messages).status(200);         // Return list of messages as JSON with 200 status.
    }

    // Handler method for the "/messages/{message_id}" endpoint. Retrieves a specific message by ID.
    private void retrieveMessageByIdHandler(Context context) {
        try {
            int mId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.retrieveMessageById(mId);
            if (message != null) {
                context.json(message).status(200);  // Return message as JSON with 200 status if found.
            } else {
                context.status(200);
            }
        } catch (Exception e) {
            context.status(400);
        }
    }

    // Handler method for the "/messages/{message_id}" endpoint. Deletes a message by ID.
    private void deleteMessageByMessageIdHandler(Context context) {
        try {
            int mId = Integer.parseInt(context.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessageByMessageId(mId);
            if (deletedMessage != null) {
                context.json(deletedMessage).status(200);
            } else {
                context.status(200);
            }
        } catch (Exception e) {
            context.status(400);
        }
    }

    // Handler method for the "/messages/{message_id}" endpoint. Updates an existing message by ID.
    private void updateMessageByMessageIdHandler(Context context) {
        try {
            int mId = Integer.parseInt(context.pathParam("message_id"));
            Message message = mapper.readValue(context.body(), Message.class);
            Message updatedMessage = messageService.updateMessageByMessageId(mId, message.getMessage_text());
            context.json(updatedMessage).status(200);   // Return updated message as JSON with 200 status
        } catch (Exception e) {
            context.status(400);
        }
    }

    // Handler method for the "/accounts/{account_id}/messages" endpoint. Retrieves all messages for a specific user ID.
    private void retrieveAllMessagesForUserHandler(Context context) {
        try {
            int userId = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.retrieveAllMessagesForUserId(userId);
            context.json(messages).status(200); // Return list of messages as JSON with 200 status.
        } catch (Exception e) {
            context.status(200);
        }
    }
}
