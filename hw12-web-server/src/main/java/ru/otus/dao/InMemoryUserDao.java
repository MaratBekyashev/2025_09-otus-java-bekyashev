package ru.otus.dao;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import ru.otus.model.User;

@SuppressWarnings("java:S2068")
public class InMemoryUserDao implements UserDao {

    public static final String DEFAULT_PASSWORD = "1111";
    private final Map<Long, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "Пользователь", "user", DEFAULT_PASSWORD));
        users.put(2L, new User(1L, "Администратор", "admin", DEFAULT_PASSWORD));
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(users.get(login.toLowerCase()));
    }
}
