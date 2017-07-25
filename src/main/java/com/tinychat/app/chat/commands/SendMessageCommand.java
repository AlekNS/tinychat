package com.tinychat.app.chat.commands;

import com.tinychat.app.chat.UserInDTOMessage;
import org.springframework.util.Assert;

final public class SendMessageCommand {

    private String fromUsername;
    private UserInDTOMessage userInDTOMessage;

    public SendMessageCommand(String fromUsername, UserInDTOMessage userInDTOMessage) {
        this.setFromUsername(fromUsername);
        this.setUserInDTOMessage(userInDTOMessage);
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public UserInDTOMessage getUserInDTOMessage() {
        return userInDTOMessage;
    }

    private void setFromUsername(String fromUsername) {
        Assert.hasLength(fromUsername, "Initiate username cant't be null or empty");
        this.fromUsername = fromUsername;
    }

    private void setUserInDTOMessage(UserInDTOMessage userInDTOMessage) {
        Assert.notNull(userInDTOMessage, "UserInDtoMessage cant't be null");
        this.userInDTOMessage = userInDTOMessage;
    }

}
