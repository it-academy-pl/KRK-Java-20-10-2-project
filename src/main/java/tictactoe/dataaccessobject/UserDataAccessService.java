package tictactoe.dataaccessobject;

import org.springframework.stereotype.Repository;
import tictactoe.exceptions.UserAlreadyRegisteredException;
import tictactoe.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository("userDao")
public class UserDataAccessService implements UserDao {

    private final List<User> registeredUsersDB = new ArrayList<>();

    @Override
    public boolean registerNewUser(User user) {
        if(registeredUsersDB.stream().anyMatch(registeredUser -> registeredUser.getId().equals(user.getId()))) {
            throw new UserAlreadyRegisteredException(user.getName());
//            return false;
        }
        registeredUsersDB.add(user);
        return true;
    }

    @Override
    public boolean removeUser(UUID id) {
        return registeredUsersDB.removeIf(user -> user.getId().equals(id));
    }

    @Override
    public List<User> getRegisteredUsers() {
        return Collections.unmodifiableList(registeredUsersDB);
    }
}
