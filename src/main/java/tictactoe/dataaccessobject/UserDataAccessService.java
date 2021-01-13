package tictactoe.dataaccessobject;

import org.springframework.stereotype.Repository;
import tictactoe.exceptions.UserAlreadyRegisteredException;
import tictactoe.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository("dao")
public class UserDataAccessService implements UserDao {

    private static final List<User> registeredUsersDB = new ArrayList<>();

    @Override
    public boolean registerNewUser(String name, String password) {
        for(User user : registeredUsersDB) {
            if(user.getName().equals(name)) {
                throw new UserAlreadyRegisteredException(name);
//                return false;
            }
        }
        registeredUsersDB.add(new User(name,password));
        return true;
    }

    @Override
    public boolean removeUser(String name) {
        return registeredUsersDB.removeIf(user -> user.getName().equals(name));
    }

    @Override
    public List<User> getRegisteredUsers() {
        return Collections.unmodifiableList(registeredUsersDB);
    }
}
