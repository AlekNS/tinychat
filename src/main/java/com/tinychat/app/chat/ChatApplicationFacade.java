package com.tinychat.app.chat;

import com.tinychat.app.chat.commands.RefreshUserListCommand;
import com.tinychat.app.chat.commands.SendMessageCommand;
import com.tinychat.app.chat.commands.ToggleUserConnectionCommand;
import com.tinychat.app.chat.queries.AllActiveUsersQuery;
import com.tinychat.app.chat.queries.LastMessagesForUsernameQuery;
import com.tinychat.domain.model.ChatMessage;
import com.tinychat.domain.model.IChatMessageRepository;
import com.tinychat.domain.model.IUserRepository;
import com.tinychat.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application layer facade.
 */
@Service
public class ChatApplicationFacade {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IChatMessageRepository chatMessageRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Value("${app.startup.messages.loadLastCount}")
    Integer startupLoadLastMessagesCount = 20;

    @Value("${app.ui.message.dateFormatter}")
    String messageDateFormatter = "HH:mm:ss";

    private UserOutDTOMessage convertFromChatMessage(ChatMessage chatMessage) {
        String formattedDate = new SimpleDateFormat(messageDateFormatter).format(chatMessage.getStamp());
        return new UserOutDTOMessage(
                chatMessage.getId().toString(),
                !chatMessage.getTo().isEmpty(),
                formattedDate,
                chatMessage.getFrom().getUsername(),
                chatMessage.getMessage()
        );
    }

    /**
     * Toggle user connection state.
     * @param toggleUserConnectionCommand
     * @return
     */
    public boolean execute(ToggleUserConnectionCommand toggleUserConnectionCommand) {
        User user = userRepository.findById(toggleUserConnectionCommand.getUsername());
        if (toggleUserConnectionCommand.isConnected() && user == null) {
            userRepository.save(new User(toggleUserConnectionCommand.getUsername()));
            return true;
        } else if (user != null) {
            userRepository.remove(user);
            return true;
        }
        return false;
    }

    /**
     * Send public or private message
     * @param sendMessageCommand
     * @return
     */
    public boolean execute(SendMessageCommand sendMessageCommand) {
        User initiateUser = userRepository.findById(sendMessageCommand.getFromUsername());
        if (initiateUser == null) {
            return false;
        }

        List<User> users = sendMessageCommand.getUserInDTOMessage().getTo().stream()
            .map((String username) -> userRepository.findById(username))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        ChatMessage chatMessage = chatMessageRepository.save(
            new ChatMessage(new Date(), initiateUser, users, sendMessageCommand.getUserInDTOMessage().getMessage())
        );

        if (sendMessageCommand.getUserInDTOMessage().getTo().size() > 0) {
            users.add(initiateUser);
            for (User toUser : users) {
                simpMessagingTemplate.convertAndSendToUser(toUser.getUsername(), "/queue/reply", convertFromChatMessage(chatMessage));
            }
        } else {
            simpMessagingTemplate.convertAndSend("/topic/messages", convertFromChatMessage(chatMessage));
        }

        return true;
    }

    /**
     * Send to refresh list of active users.
     * @param refreshUserListCommand
     * @return
     */
    public boolean execute(RefreshUserListCommand refreshUserListCommand) {
        List<User> users = userRepository.findAll();
        simpMessagingTemplate.convertAndSend("/topic/users", users);
        return true;
    }

    /**
     * Get all active users.
     * @param query
     * @return
     */
    public List<User> query(AllActiveUsersQuery query) {
        return userRepository.findAll();
    }

    /**
     * Get last messages.
     * @param query
     * @return
     */
    public List<UserOutDTOMessage> query(LastMessagesForUsernameQuery query) {
        User user = userRepository.findById(query.getUsername());
        if (user == null) {
            return new ArrayList<>();
        }

        return chatMessageRepository.findAllForUser(user, startupLoadLastMessagesCount)
                .stream()
                .map(this::convertFromChatMessage)
                .collect(Collectors.toList());
    }

}
