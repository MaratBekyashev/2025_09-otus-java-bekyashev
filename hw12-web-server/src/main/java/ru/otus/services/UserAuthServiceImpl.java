package ru.otus.services;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ru.otus.dao.UserDao;
import ru.otus.model.User;

@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    @Override
    public Optional<User> authenticate(String login, String password) {
        Optional<User> user = userDao.findByLogin(login);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}
