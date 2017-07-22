package com.tinychat.app.chat.quries;

public class LastMessagesForUsernameQuery {
    private String username;

    public LastMessagesForUsernameQuery(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
