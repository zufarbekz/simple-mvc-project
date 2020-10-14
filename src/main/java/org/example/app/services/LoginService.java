package org.example.app.services;

import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;;


@Service
public class LoginService {

    private final Logger logger = Logger.getLogger(LoginService.class);
    private static Map<String, String> users = new HashMap<>();

    public boolean authenticate(LoginForm loginForm){
       //Интерфейс и логика регистрации нового пользователя
        if (loginForm.getUsername().equals("") || loginForm.getPassword().equals("")){
            logger.info("Necessary information is NOT entered!");
            return false;
        }

        if (!users.containsKey(loginForm.getUsername()) && !users.containsValue(loginForm.getPassword())){
            logger.info("New user created: " + loginForm);
            users.put(loginForm.getUsername(),loginForm.getPassword());
        }

        for (String username : users.keySet()) {
            if (username.equals(loginForm.getUsername()) && users.get(username).equals(loginForm.getPassword())) {
                return true;
            }
            if (username.equals(loginForm.getUsername()) && !users.get(username).equals(loginForm.getPassword())) {
                logger.info("Wrong password!");
                return false;
            }
        }
        return false;
    }
}
