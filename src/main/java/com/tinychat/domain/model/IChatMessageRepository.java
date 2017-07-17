package com.tinychat.domain.model;

import java.util.List;

/**
 * Chat message repository.
 */
public interface IChatMessageRepository {
    public List<ChatMessage> findAllForUser(User user, int lastCount);
    public ChatMessage save(ChatMessage chatMessage);
}
