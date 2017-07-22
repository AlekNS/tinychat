package com.tinychat.app.chat;

import org.springframework.util.Assert;

import java.util.List;

public class UserInDTOMessage {

    private String message;
    private List<String> to;

    public UserInDTOMessage() {
    }

    public UserInDTOMessage(String message, List<String> to) {
        this.setMessage(message);
        this.setTo(to);
    }

    public String getMessage() {
        return message;
    }

    public List<String> getTo() {
        return to;
    }

    private void setMessage(String message) {
        Assert.hasLength(message, "The message can't be null or empty");
        this.message = message;
    }

    private void setTo(List<String> to) {
        Assert.notNull(to, "Parameter 'to' shouldn't be null");
        this.to = to;
    }

}
