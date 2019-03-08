package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.ConflictException;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    User getUser(@PathVariable long id) { return service.getUser(id); }

    @DeleteMapping("/users/{id}")
    User deleteUser(@PathVariable long id) {
        service.deleteUser(id);
        return null;
    }

    // Putting to a single id means trying to change info about it
    @PostMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changeUser(@PathVariable long id, @RequestBody User changeUser) { service.changeUser(id, changeUser); }

    // Posting to /users means creating a new user
    // This method is weird because the test for the service requires service.createUser to return a user, and the
    // REST specification requires this method to return a http header containing the new user's profile location
    @PostMapping("/users")
    ResponseEntity<String> createUser(@RequestBody User newUser) {
        try {
            User result = this.service.createUser(newUser);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Location", "/users?id=" + result.getId());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
        } catch (ConflictException e) {
            throw e;
        }
    }


    // Posting to /login means trying to login, duh
    @PostMapping("/login")
    User loginUser(@RequestBody User loginUser) {
        return service.attemptLogin(loginUser);
    }

}
