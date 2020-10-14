package org.example.web.controllers;

import org.example.web.dto.LoginForm;
import org.example.app.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.apache.log4j.Logger;

@Controller
public class LoginController {
    private LoginService loginService;
    private final Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /*Сюда будет записываться username & password*/
    @GetMapping(value = "/login")
    public String login(Model model){
        logger.info("GET /login returns login_page.html");
        model.addAttribute("loginForm", new LoginForm());
        return "login_page";
    }

    @PostMapping("/login/auth")
    public String authenticate(LoginForm loginForm){
        if (loginService.authenticate(loginForm)){
            logger.info("Login OK! Redirected to /books/shelf");
            return "redirect:/books/shelf";
        }else{
            logger.info("Login Failed! Redirected to /login again");
            return "redirect:/login";
        }
    }
}
