package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.RegistrationException;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    User getUser(@PathVariable long id) { return service.getUser(id);}

    @DeleteMapping("/users/{id}")
    User deleteUser(@PathVariable long id) {
        service.deleteUser(id);
        return null;
    }

    // Posting to /users means creating a new user
    @PostMapping("/users")
    @ExceptionHandler({RegistrationException.class})
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

}
