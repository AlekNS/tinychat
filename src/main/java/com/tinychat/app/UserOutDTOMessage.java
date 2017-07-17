package com.tinychat.app;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class UserOutDTOMessage {

    private String id;

    @JsonProperty("is_private")
    private Boolean isPrivate;

    private String stamp;
    private String from;
    private String message;

    public UserOutDTOMessage() {
    }

    public UserOutDTOMessage(String id, Boolean isPrivate, String stamp, String from, String message) {
        this.id = id;
        this.isPrivate = isPrivate;
        this.stamp = stamp;
        this.from = from;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public String getStamp() {
        return stamp;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

}