package com.tinychat.domain.model;

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
        this.id = id;
        this.stamp = stamp;
        this.from = from;
        this.to = Collections.unmodifiableList(to);
        this.message = message;
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
}
