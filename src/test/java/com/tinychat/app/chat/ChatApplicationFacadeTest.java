package com.tinychat.app.chat;

import com.tinychat.app.chat.commands.SendMessageCommand;
import com.tinychat.app.chat.commands.ToggleUserConnectionCommand;
import com.tinychat.app.chat.queries.AllActiveUsersQuery;
import com.tinychat.app.chat.queries.LastMessagesForUsernameQuery;
import com.tinychat.domain.model.IChatMessageRepository;
import com.tinychat.domain.model.IUserRepository;
import com.tinychat.domain.model.User;
import com.tinychat.infrastructure.persistence.ChatMessageRepositoryInMemory;
import com.tinychat.infrastructure.persistence.UserRepositoryInMemory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChatApplicationFacadeTest {

    public static final String FIRST_USERNAME = "username100";
    public static final String SECOND_USERNAME = "username200";
    public static final String THIRD_USERNAME = "username300";

    public static final String PUBLIC_MESSAGE = "Public Message";
    public static final String PRIVATE_MESSAGE = "Private Message";

    @Autowired
    ChatApplicationFacade chatApplication;

    @Configuration
    @ComponentScan(basePackages = {"com.tinychat"})
    public static class TestConfig
    {
        @Bean
        @Primary
        public SimpMessagingTemplate mockedSimpMessagingTemplate()
        {
            SimpMessagingTemplate svc = mock(SimpMessagingTemplate.class);
            doAnswer(invocationOnMock -> null)
                .when(svc)
                .convertAndSend(anyString(), any(UserOutDTOMessage.class));
            doAnswer(invocationOnMock -> null)
                .when(svc)
                .convertAndSendToUser(anyString(), anyString(), any(UserOutDTOMessage.class));
            return svc;
        }

        @Bean
        @Primary
        public IChatMessageRepository inMemoryChatMessageRepository() {
            return new ChatMessageRepositoryInMemory();
        }

        @Bean
        @Primary
        public IUserRepository inMemoryUserRepository() {
            return new UserRepositoryInMemory();
        }
    }

    @Before
    public void setUpIt() {
        chatApplication.execute(new ToggleUserConnectionCommand(true, FIRST_USERNAME));
        chatApplication.execute(new ToggleUserConnectionCommand(true, SECOND_USERNAME));
        chatApplication.execute(new ToggleUserConnectionCommand(true, THIRD_USERNAME));
    }

    @Test
    public void executeToggleUserConnectionCommand() {
        chatApplication.execute(new ToggleUserConnectionCommand(false, FIRST_USERNAME));
        chatApplication.execute(new ToggleUserConnectionCommand(false, SECOND_USERNAME));

        List<User> users = chatApplication.query(new AllActiveUsersQuery());
        assertThat(users.size(), is(1));
        assertThat(users.get(0).getUsername(), is(THIRD_USERNAME));
    }

    @Test
    public void executeSendMessageCommandAsPublic() {
        assertThat(
            chatApplication.execute(
                new SendMessageCommand(FIRST_USERNAME, new UserInDTOMessage(PUBLIC_MESSAGE, null))
            ),
            is(true)
        );

        List<UserOutDTOMessage> result = chatApplication.query(new LastMessagesForUsernameQuery(THIRD_USERNAME));
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getMessage(), is(PUBLIC_MESSAGE));
        assertThat(result.get(0).getFrom(), is(FIRST_USERNAME));
    }

    @Test
    public void executeSendMessageCommandAsPrivate() {
        assertThat(
            chatApplication.execute(
                new SendMessageCommand(
                    FIRST_USERNAME,
                    new UserInDTOMessage(PRIVATE_MESSAGE, Arrays.asList(SECOND_USERNAME))
                )
            ),
            is(true)
        );

        List<UserOutDTOMessage> result = chatApplication.query(new LastMessagesForUsernameQuery(THIRD_USERNAME));
        assertThat(result.size(), is(0));

        result = chatApplication.query(new LastMessagesForUsernameQuery(SECOND_USERNAME));
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getMessage(), is(PRIVATE_MESSAGE));
        assertThat(result.get(0).getFrom(), is(FIRST_USERNAME));
    }


    @Test
    public void executeSendMessageCommandsAsPrivateThenPublic() {
        assertThat(
                chatApplication.execute(
                        new SendMessageCommand(
                                FIRST_USERNAME,
                                new UserInDTOMessage(PRIVATE_MESSAGE, Arrays.asList(SECOND_USERNAME))
                        )
                ),
                is(true)
        );

        assertThat(
            chatApplication.execute(
                new SendMessageCommand(FIRST_USERNAME, new UserInDTOMessage(PUBLIC_MESSAGE, null))
            ),
            is(true)
        );

        List<UserOutDTOMessage> result = chatApplication.query(new LastMessagesForUsernameQuery(THIRD_USERNAME));
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getMessage(), is(PUBLIC_MESSAGE));
        assertThat(result.get(0).getFrom(), is(FIRST_USERNAME));

        result = chatApplication.query(new LastMessagesForUsernameQuery(SECOND_USERNAME));
        assertThat(result.size(), is(2));
        assertThat(result.get(0).getMessage(), is(PRIVATE_MESSAGE));
        assertThat(result.get(0).getFrom(), is(FIRST_USERNAME));
    }

}
