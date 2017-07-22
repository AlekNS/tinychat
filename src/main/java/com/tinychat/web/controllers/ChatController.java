package com.tinychat.web.controllers;

import com.tinychat.app.chat.ChatApplicationFacade;
import com.tinychat.app.chat.UserInDTOMessage;
import com.tinychat.app.chat.UserOutDTOMessage;
import com.tinychat.app.chat.commands.SendMessageCommand;
import com.tinychat.app.chat.quries.AllActiveUsersQuery;
import com.tinychat.app.chat.quries.LastMessagesForUsernameQuery;
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
    protected ChatApplicationFacade chatApplication;

    @SendTo("/topic/users")
    @MessageMapping("/users")
    public List<User> getCurrentUsers(Principal principal) throws Exception {
        return chatApplication.query(new AllActiveUsersQuery());
    }

    @SendTo("/topic/messages/last")
    @MessageMapping("/messages/last")
    public List<UserOutDTOMessage> lastMessages(Principal principal) throws Exception {
        return chatApplication.query(new LastMessagesForUsernameQuery(principal.getName()));
    }

    @MessageMapping("/messages")
    public void sendMessage(Principal principal, UserInDTOMessage message) throws Exception {
        chatApplication.execute(new SendMessageCommand(principal.getName(), message));
    }

}
