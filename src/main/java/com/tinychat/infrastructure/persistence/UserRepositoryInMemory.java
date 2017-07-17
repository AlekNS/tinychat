package com.tinychat.infrastructure.persistence;

import com.tinychat.domain.model.IUserRepository;
import com.tinychat.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class UserRepositoryInMemory implements IUserRepository {
    private ConcurrentMap<String, User> repository;

    UserRepositoryInMemory() {
        repository = new ConcurrentHashMap<>();
    }

    @Override
    public User findById(String id) {
        return repository.get(id);
    }

    @Override
    public List<User> findAll() {
        ArrayList<User> result = new ArrayList<>(repository.values());
        result.sort(Comparator.comparing(User::getUsername));
        return result;
    }

    @Override
    public void save(User user) {
        repository.put(user.getUsername(), user);
    }

    @Override
    public void remove(User user) {
        repository.remove(user.getUsername());
    }
}
