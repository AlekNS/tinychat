package com.tinychat.app.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

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
        this.setId(id);
        this.setIsPrivate(isPrivate);
        this.setStamp(stamp);
        this.setFrom(from);
        this.setMessage(message);
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

    private void setId(String id) {
        Assert.hasLength(id, "id can't be null or empty");
        this.id = id;
    }

    private void setIsPrivate(Boolean aPrivate) {
        Assert.notNull(aPrivate, "isPrivate can't be null or empty");
        isPrivate = aPrivate;
    }

    private void setStamp(String stamp) {
        Assert.hasLength(stamp, "stamp can't be null or empty");
        this.stamp = stamp;
    }

    private void setFrom(String from) {
        Assert.hasLength(from, "from can't be null or empty");
        this.from = from;
    }

    private void setMessage(String message) {
        Assert.hasLength(message, "message can't be null or empty");
        this.message = message;
    }
}