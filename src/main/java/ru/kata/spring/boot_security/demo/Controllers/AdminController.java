package ru.kata.spring.boot_security.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController (UserService userService, RoleService roleService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showAllUsers(@AuthenticationPrincipal User user, Role role, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("new_user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("role", role);
        return "admin";
    }

    @GetMapping("/showUser")
    public String showUser(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "showUser";
    }
    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        System.out.println("Добавление");
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.showUser(id));
        return "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         BindingResult bindingResult, @PathVariable ("id") long id) {
        if (bindingResult.hasErrors())
            return "/edit";

        userService.updateUser(id, user);
        return "redirect:/admin/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/";
    }
}
