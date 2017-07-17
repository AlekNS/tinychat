package com.tinychat.app.commands;

import com.tinychat.domain.model.User;

public class ToggleUserConnectionCommand {
    private boolean isConnected;
    private String username;

    public ToggleUserConnectionCommand(boolean isConnected, String username) {
        this.isConnected = isConnected;
        this.username = username;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getUsername() {
        return username;
    }
}
