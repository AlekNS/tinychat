package com.tinychat.app.chat.commands;

import org.springframework.util.Assert;

public class ToggleUserConnectionCommand {
    private boolean isConnected;
    private String username;

    public ToggleUserConnectionCommand(boolean isConnected, String username) {
        this.isConnected = isConnected;
        this.setUsername(username);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Assert.hasLength(username,"username can't be empty");
        this.username = username;
    }
}
