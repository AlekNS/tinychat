package com.tinychat.app;

import java.util.List;

public class UserInDTOMessage {

    private String message;
    private List<String> to;

    public UserInDTOMessage() {
    }

    public UserInDTOMessage(String message, List<String> to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getTo() {
        return to;
    }

}
