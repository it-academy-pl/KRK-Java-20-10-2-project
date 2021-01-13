package tictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tictactoe.dataaccessobject.UserDao;
import tictactoe.model.User;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("userDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean registerNewUser(User user) {
        return userDao.registerNewUser(user);
    }

    public boolean removeUser(UUID id) {
        return userDao.removeUser(id);
    }

    public List<User> getRegisteredUsers() {
        return userDao.getRegisteredUsers();
    }
}
