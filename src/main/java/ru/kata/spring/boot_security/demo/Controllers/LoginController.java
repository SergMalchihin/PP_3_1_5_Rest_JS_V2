package ru.kata.spring.boot_security.demo.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "loginPage";
    }
    @PostMapping("/login")
    public String processLogin() {
        return "redirect:/";
    }

}

