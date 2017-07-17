package com.tinychat.infrastructure.persistence;

import com.tinychat.domain.model.ChatMessage;
import com.tinychat.domain.model.IChatMessageRepository;
import com.tinychat.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ChatMessageRepositoryInMemory implements IChatMessageRepository {
    private ConcurrentLinkedDeque<ChatMessage> repository;
    private AtomicLong counter;

    @Value("${app.repository.chatMessages.inMemory.maxStorageSize}")
    private Integer maxStorageSize = 1024;

    ChatMessageRepositoryInMemory() {
        repository = new ConcurrentLinkedDeque<>();
        counter = new AtomicLong(0);
    }

    public Long generateId() {
        return this.counter.incrementAndGet();
    }

    @Override
    public List<ChatMessage> findAllForUser(User user, int lastCount) {
        ArrayList<ChatMessage> result = new ArrayList<>();

        for(ChatMessage chatMessage : repository) {
            List<User> toUsers = chatMessage.getTo();

            // hide private messages
            if (toUsers != null && toUsers.size() > 0 && !toUsers.contains(user)) {
                continue;
            }

            // limit last messages
            if (lastCount-- < 0) {
                break;
            }

            result.add(chatMessage);
        }

        Collections.reverse(result);

        return result;
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        // stupid removing of old messages
        if (repository.size() >= maxStorageSize) {
            repository.pollLast();
        }

        Long id = this.generateId();

        chatMessage = new ChatMessage(
                id,
                chatMessage.getStamp(),
                chatMessage.getFrom(),
                chatMessage.getTo(),
                chatMessage.getMessage()
        );

        repository.addFirst(chatMessage);

        return chatMessage;
    }
}
