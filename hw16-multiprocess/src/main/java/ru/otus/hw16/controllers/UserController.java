package ru.otus.hw16.controllers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw16.messagesystem.MessageSystemConfig;
import ru.otus.hw16.messagesystem.front.FrontendService;
import ru.otus.hw16.model.User;
import ru.otus.hw16.services.UserService;

import java.util.List;

@Controller
@ConditionalOnProperty(prefix = "message-system", name = "type", havingValue = MessageSystemConfig.FRONT_TYPE)
public class UserController {
    private final SimpMessagingTemplate template;
    private final UserService repository;
    private final FrontendService frontendService;

    public UserController(SimpMessagingTemplate template, UserService repository, FrontendService frontendService) {
        this.template = template;
        this.repository = repository;
        this.frontendService = frontendService;
    }

    @GetMapping({"/", "/user/list"})
    public String userListView(Model model) {
        List<User> users = repository.getAll();
        model.addAttribute("users", users);
        return "userList.html";
    }

    @MessageMapping("/new_user")
    public void newUser(User user) {
        frontendService.createUser(user, this::broadcastUser);
    }

    public void broadcastUser(User user) {
        this.template.convertAndSend("/topic/users", user);
    }
}
