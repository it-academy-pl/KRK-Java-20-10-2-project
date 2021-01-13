package tictactoe.dataaccessobject;

import tictactoe.model.User;

import java.util.List;

public interface UserDao {
    boolean registerNewUser(String name, String password);
    boolean removeUser(String name);
    List<User> getRegisteredUsers();
}

//TODO: add: update
//TODO: add: select
