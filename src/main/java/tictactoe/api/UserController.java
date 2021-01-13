package tictactoe.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tictactoe.service.UserService;

@RequestMapping("api/user")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void registerNewUser(@RequestParam("name") String name, @RequestParam("password") String password) {
        userService.registerNewUser(name, password);
    }

    @DeleteMapping
    public void removeUser(String name) {
        userService.removeUser(name);
    }

    @GetMapping
    public void getRegisteredUsers() {
        userService.getRegisteredUsers();
    }

//    @GetMapping(path = "{id}")
//    public void selectUser(@PathVariable("id") UUID id) {
//        userService.selectUser(UUID id);
//    }
}
