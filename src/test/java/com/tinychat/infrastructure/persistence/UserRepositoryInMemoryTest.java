package com.tinychat.infrastructure.persistence;

import com.tinychat.app.chat.ChatApplicationFacadeTest;
import com.tinychat.domain.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;


public class UserRepositoryInMemoryTest {

    private UserRepositoryInMemory userRepository;

    @Before
    public void setUp() {
        userRepository = new UserRepositoryInMemory();
    }

    @Test
    public void shouldReturnEmptyResult() {
        assertThat(userRepository.findAll().size(), is(0));
    }

    @Test
    public void shouldReturnNullUser() {
        assertThat(userRepository.findById("notExistUsername"), nullValue());
    }

    @Test
    public void shouldReturnAddedUserByFindAll() {
        userRepository.save(new User(ChatApplicationFacadeTest.FIRST_USERNAME));
        assertThat(userRepository.findAll().size(), is(1));
        assertThat(userRepository.findAll().get(0).getUsername(), is(ChatApplicationFacadeTest.FIRST_USERNAME));
    }

    @Test
    public void shouldReturnAddedUserByFindById() {
        userRepository.save(new User(ChatApplicationFacadeTest.SECOND_USERNAME));
        assertThat(userRepository.findById(ChatApplicationFacadeTest.SECOND_USERNAME), not(nullValue()));
        assertThat(
            userRepository.findById(ChatApplicationFacadeTest.SECOND_USERNAME).getUsername(),
            is(ChatApplicationFacadeTest.SECOND_USERNAME)
        );
    }

    @Test
    public void shouldBeEmptyAfterRemoveAddedUserByFindAll() {
        User user = new User(ChatApplicationFacadeTest.THIRD_USERNAME);

        userRepository.save(user);
        assertThat(userRepository.findAll().size(), is(1));

        userRepository.remove(user);
        assertThat(userRepository.findAll().size(), is(0));
    }

}
