package com.tinychat.app;

import com.tinychat.app.commands.RefreshUserListCommand;
import com.tinychat.app.commands.SendMessageCommand;
import com.tinychat.app.commands.ToggleUserConnectionCommand;
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

@Service
public class ChatApplication {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IChatMessageRepository chatMessageRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Value("${app.startup.messages.loadLastCount}")
    Integer startupLoadLastMessagesCount = 20;

    private UserOutDTOMessage convertFromChatMessage(ChatMessage chatMessage) {
        String stamp = new SimpleDateFormat("HH:mm:ss dd/M/yyyy").format(chatMessage.getStamp());
        return new UserOutDTOMessage(
                chatMessage.getId().toString(),
                !chatMessage.getTo().isEmpty(),
                stamp,
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
        User user = userRepository.findById(sendMessageCommand.getFromUsername());
        List<User> users = Collections.emptyList();

        if (sendMessageCommand.getUserInDTOMessage().getTo().size() > 0) {
            users = sendMessageCommand.getUserInDTOMessage().getTo()
                    .stream()
                    .map((String username) -> userRepository.findById(username))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        ChatMessage chatMessage = chatMessageRepository.save(
            new ChatMessage(new Date(), user, users, sendMessageCommand.getUserInDTOMessage().getMessage())
        );

        if (sendMessageCommand.getUserInDTOMessage().getTo().size() > 0) {
            for (User toUser : users) {
                simpMessagingTemplate.convertAndSendToUser(toUser.getUsername(), "/queue/reply", convertFromChatMessage(chatMessage));
            }
            simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/reply", convertFromChatMessage(chatMessage));
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
     * @return
     */
    public List<User> queryAllActiveUsers() {
        return userRepository.findAll();
    }

    /**
     * Get last messages.
     * @param username
     * @return
     */
    public List<UserOutDTOMessage> queryLastMessagesForUsername(String username) {
        User user = userRepository.findById(username);
        if (user == null) {
            return new ArrayList<>();
        }

        return chatMessageRepository.findAllForUser(user, startupLoadLastMessagesCount)
                .stream()
                .map(this::convertFromChatMessage)
                .collect(Collectors.toList());

    }
}
