package com.tinychat.domain.model;

import org.springframework.util.Assert;

/**
 * Chat user
 */
public class User {

    private String username;

    public User() {
    }

    public User(String username) {
        this.setUsername(username);
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        Assert.hasLength(username, "User must have valid not empty username");
        this.username = username;
    }
}
