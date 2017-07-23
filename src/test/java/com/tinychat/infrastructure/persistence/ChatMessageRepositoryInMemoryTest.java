package com.tinychat.infrastructure.persistence;

import com.tinychat.app.chat.ChatApplicationFacadeTest;
import com.tinychat.domain.model.ChatMessage;
import com.tinychat.domain.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class ChatMessageRepositoryInMemoryTest {

    private User firstUser;
    private User secondUser;
    private ChatMessageRepositoryInMemory chatMessageRepository;

    @Before
    public void setUp() {
        firstUser = new User(ChatApplicationFacadeTest.FIRST_USERNAME);
        secondUser = new User(ChatApplicationFacadeTest.SECOND_USERNAME);
        chatMessageRepository = new ChatMessageRepositoryInMemory();
    }

    @Test
    public void shouldReturnEmptyResult() {
        assertThat(chatMessageRepository.findAllForUser(firstUser, 10).size(), is(0));
    }

    @Test
    public void shouldAssignChatMessageId() {
        ChatMessage chatMessage = new ChatMessage(null, new Date(), firstUser, null, "Message");
        chatMessage = chatMessageRepository.save(chatMessage);
        assertThat(chatMessage.getId(), is(1L));
    }

    @Test
    public void shouldReturnOnlyPublicMessage() {
        User thirdUser = new User(ChatApplicationFacadeTest.THIRD_USERNAME);
        fillMessages();
        List<ChatMessage> messages = chatMessageRepository.findAllForUser(thirdUser, 10);

        assertThat(messages.size(), is(1));
        assertThat(messages.get(0).getMessage(), is(ChatApplicationFacadeTest.PUBLIC_MESSAGE));
    }

    @Test
    public void shouldReturnPublicAndPrivateMessage() {
        fillMessages();
        List<ChatMessage> messages = chatMessageRepository.findAllForUser(secondUser, 10);

        assertThat(messages.size(), is(2));
        assertThat(messages.get(0).getMessage(), is(ChatApplicationFacadeTest.PUBLIC_MESSAGE));
        assertThat(messages.get(1).getMessage(), is(ChatApplicationFacadeTest.PRIVATE_MESSAGE));
    }

    private void fillMessages() {
        chatMessageRepository.save(
            new ChatMessage(null, new Date(), firstUser, null, ChatApplicationFacadeTest.PUBLIC_MESSAGE)
        );
        chatMessageRepository.save(
            new ChatMessage(
                null,
                new Date(),
                firstUser,
                Collections.singletonList(secondUser),
                ChatApplicationFacadeTest.PRIVATE_MESSAGE
            )
        );
    }

}