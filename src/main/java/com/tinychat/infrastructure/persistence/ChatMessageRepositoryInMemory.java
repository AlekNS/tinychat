package com.tinychat.infrastructure.persistence;

import com.tinychat.domain.model.ChatMessage;
import com.tinychat.domain.model.IChatMessageRepository;
import com.tinychat.domain.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Qualifier("ChatMessageRepositoryInMemory")
public class ChatMessageRepositoryInMemory implements IChatMessageRepository {
    private ConcurrentLinkedDeque<ChatMessage> repository;
    private AtomicLong idCounter;

    @Value("${app.repository.chatMessages.inMemory.maxStorageSize}")
    private Integer maxStorageSize = 1024;

    public ChatMessageRepositoryInMemory() {
        repository = new ConcurrentLinkedDeque<>();
        idCounter = new AtomicLong(0);
    }

    public Long generateId() {
        return this.idCounter.incrementAndGet();
    }

    @Override
    public List<ChatMessage> findAllForUser(final User user, int lastCount) {
        List<ChatMessage> result = repository.stream()
                .filter((chatMessage -> chatMessage.getTo().size() == 0 || chatMessage.getTo().contains(user)))
                .limit(lastCount)
                .collect(Collectors.toList());

        Collections.reverse(result);

        return result;
    }

    @Override
    public ChatMessage save(ChatMessage originalChatMessage) {
        // stupid removing of old messages
        if (repository.size() >= maxStorageSize) {
            repository.pollLast();
        }

        ChatMessage chatMessage = new ChatMessage(
                this.generateId(),
                originalChatMessage.getStamp(),
                originalChatMessage.getFrom(),
                originalChatMessage.getTo(),
                originalChatMessage.getMessage()
        );

        repository.addFirst(chatMessage);

        return chatMessage;
    }
}
