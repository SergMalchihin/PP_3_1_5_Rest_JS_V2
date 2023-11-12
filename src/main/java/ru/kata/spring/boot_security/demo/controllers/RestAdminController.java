package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RestAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/showAccount")
    public ResponseEntity<User> showInfoUser(Principal principal) {
        return ResponseEntity.ok(userService.findByUsername(principal.getName()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<Collection<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Collection<Role>> getRole(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findUserById(id).getRoles(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addNewUser(@RequestBody @Valid User newUser, BindingResult bindingResult) {
        userService.saveUser(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.OK);

    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User userFromWebPage, @PathVariable("id") Long id) {
        userService.update(id, userFromWebPage);
        return new ResponseEntity<>(userFromWebPage, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
