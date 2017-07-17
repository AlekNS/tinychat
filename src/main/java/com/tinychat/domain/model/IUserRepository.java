package com.tinychat.domain.model;

import java.util.List;

/**
 * User repository.
 */
public interface IUserRepository {
    public List<User> findAll();
    public User findById(String id);
    public void save(User user);
    public void remove(User user);
}
