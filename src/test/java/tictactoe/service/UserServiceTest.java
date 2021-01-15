package tictactoe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tictactoe.dataaccessobject.UserDao;
import tictactoe.dataaccessobject.UserDataAccessService;
import tictactoe.exceptions.UserAlreadyRegisteredException;
import tictactoe.model.User;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        userDao = new UserDataAccessService();
    }

    @Test
    public void registerNewUser_savesUserToRepository() {
        User user = new User("Jan", "J@nek6");
        boolean registered = userDao.registerNewUser(user);
        assertThat(registered).isEqualTo(true);

        int index = 0;
        for(User searchedUser : userDao.getRegisteredUsers()) {
            if(searchedUser.getName().equals("Jan")) {
                index = userDao.getRegisteredUsers().indexOf(searchedUser);
            }
        }
        UUID expectedUserID = userDao.getRegisteredUsers().get(index).getId();
        assertThat(user.getId()).isEqualTo(expectedUserID);
    }

    @Test
    public void registerNewUser_userIdAlreadyExists_throwPlayerAlreadyRegisteredException() throws NoSuchFieldException, IllegalAccessException {
        User user = new User("Jan", "J@nek6");
        userDao.registerNewUser(user);

        User testUser = new User("Jan", "J@nek6");
        Field idField = testUser.getClass()
                .getDeclaredField("id");
        idField.setAccessible(true);
        UUID idHacked = user.getId();
        idField.set(testUser, idHacked);

        UserAlreadyRegisteredException exception = assertThrows(UserAlreadyRegisteredException.class,
                () -> userDao.registerNewUser(testUser));
        assertThat(exception.getMessage()).contains("User Jan, already registered.");
    }

    @Test
    void removeUser_removesUser_returnsTrue() {
        User user = new User("Jan", "J@nek6");
        userDao.registerNewUser(user);

        assertThat(userDao.removeUser(user.getId())).isEqualTo(true);
    }

    @Test
    void removeUser_userNotFound_returnsFalse() {
        User user = new User("Jan", "J@nek6");
        userDao.registerNewUser(user);

        assertThat(userDao.removeUser(UUID.randomUUID())).isEqualTo(false);
    }
}