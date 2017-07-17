package com.tinychat.app.commands;

import com.tinychat.app.UserInDTOMessage;
import com.tinychat.domain.model.User;

public class SendMessageCommand {
    private String fromUsername;
    private UserInDTOMessage userInDTOMessage;

    public SendMessageCommand(String fromUsername, UserInDTOMessage userInDTOMessage) {
        this.fromUsername = fromUsername;
        this.userInDTOMessage = userInDTOMessage;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public UserInDTOMessage getUserInDTOMessage() {
        return userInDTOMessage;
    }
}
