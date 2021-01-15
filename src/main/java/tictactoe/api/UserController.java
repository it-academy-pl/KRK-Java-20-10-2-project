package tictactoe.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tictactoe.model.User;
import tictactoe.service.UserService;

import java.util.UUID;

@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping
//    public void registerNewUser(@RequestParam("name") String name, @RequestParam("password") String password) {
//        userService.registerNewUser(name, password);
//    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.registerNewUser(user);
    }

    @GetMapping(path = "{id}")
    @DeleteMapping
    public void removeUser(@PathVariable("id") UUID id) {
        userService.removeUser(id);
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
