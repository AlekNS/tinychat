package com.tinychat.web.controllers;

import com.tinychat.app.ChatApplication;
import com.tinychat.app.UserInDTOMessage;
import com.tinychat.app.UserOutDTOMessage;
import com.tinychat.app.commands.SendMessageCommand;
import com.tinychat.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.*;

@Controller
public class ChatController {
    @Autowired
    ChatApplication chatApplication;

    @SendTo("/topic/users")
    @MessageMapping("/users")
    public List<User> getCurrentUsers(Principal principal) throws Exception {
        return chatApplication.queryAllActiveUsers();
    }

    @SendTo("/topic/messages/last")
    @MessageMapping("/messages/last")
    public List<UserOutDTOMessage> lastMessages(Principal principal) throws Exception {
        return chatApplication.queryLastMessagesForUsername(principal.getName());
    }

    @MessageMapping("/messages")
    public void sendMessage(Principal principal, UserInDTOMessage message) throws Exception {
        chatApplication.execute(new SendMessageCommand(principal.getName(), message));
    }
}
