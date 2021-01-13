package tictactoe.dataaccessobject;

import tictactoe.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDao {
    boolean registerNewUser(User user);
    boolean removeUser(UUID id);
    List<User> getRegisteredUsers();
}

//TODO: add: update
//TODO: add: select
