package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountService accountService;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountService = new AccountService();
    }

    public MessageService(MessageDAO messageDAO, AccountService accountService) {
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    /* Process creation of a new message if:
    - message_text is not blank,
     - message_text is not over 255 characters,
     - posted_by refers to a real, existing user.
    */
    public Message createMessage(Message message) {
        // Validate the message
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Error: Message not processed");
        }

        // Check if user exists
        Account user = accountService.searchById(message.getPosted_by());
        if (user == null) {
            throw new IllegalArgumentException("No User found");
        }

        return messageDAO.createMessage(message);
    }

    // Retrieve all messages.
    public List<Message> retrieveAllMessages() {
        return messageDAO.retrieveAllMessages();
    }

    // Retrieve a message by its mId.
    public Message retrieveMessageById(int mId) {
        return messageDAO.retrieveMessageById(mId);
    }

    // Delete a message by its mId if it exists.
    public Message deleteMessageByMessageId(int mId) {
        return messageDAO.deleteMessageByMessageId(mId);
    }

    /* Update a message by ID if:
     - message_id already exists,
     - new message_text is not blank,
     - new message_text is less than 255 characters.
     */
    public Message updateMessageByMessageId(int mId, String messageText) throws IllegalArgumentException {
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            throw new IllegalArgumentException("Error: Message not processed");
        }

        Message message = new Message();
        message.setMessage_text(messageText);

        Message updatedMessage = messageDAO.updateMessageByMessageId(mId, message);
        if (updatedMessage == null) {
            throw new IllegalArgumentException("Error: Message not updated");
        }
        return updatedMessage;
    }

    // Retrieve all messages for user ID.
    public List<Message> retrieveAllMessagesForUserId(int userId) {
        return messageDAO.retrieveAllMessagesForUserId(userId);
    }
}
