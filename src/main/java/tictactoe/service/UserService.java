package tictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tictactoe.dataaccessobject.UserDao;
import tictactoe.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("dao") UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean registerNewUser(String name, String password) {
        return userDao.registerNewUser(name, password);
    }

    public boolean removeUser(String name) {
        return userDao.removeUser(name);
    }

    public List<User> getRegisteredUsers() {
        return userDao.getRegisteredUsers();
    }
}
