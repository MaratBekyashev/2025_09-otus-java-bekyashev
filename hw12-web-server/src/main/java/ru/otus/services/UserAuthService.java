package ru.otus.services;

import java.util.Optional;
import ru.otus.model.User;

public interface UserAuthService {
    Optional<User> authenticate(String login, String password);
}
