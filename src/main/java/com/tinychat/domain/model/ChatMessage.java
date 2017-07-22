package com.tinychat.domain.model;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Chat message.
 */
public class ChatMessage {

    private Long id;
    private Date stamp;
    private User from;
    private List<User> to;
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(Long id, Date stamp, User from, List<User> to, String message) {
        this.setId(id);
        this.setStamp(stamp);
        this.setFrom(from);
        this.setTo(to);
        this.setMessage(message);
    }

    public ChatMessage(Date stamp, User from, List<User> to, String message) {
        this(null, stamp, from, to, message);
    }

    public Long getId() {
        return id;
    }

    public Date getStamp() {
        return stamp;
    }

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private void setStamp(Date stamp) {
        Assert.notNull(stamp, "Chat massage must have a valid time stamp");
        this.stamp = stamp;
    }

    private void setFrom(User from) {
        Assert.notNull(from, "Chat massage must have a valid initiate user");
        this.from = from;
    }

    private void setTo(List<User> to) {
        this.to = to == null ? Collections.emptyList() : to;
    }

    private void setMessage(String message) {
        Assert.hasLength(message, "Chat massage must have a valid not empty message");
        this.message = message;
    }
}
