package ru.otus.hw15.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw15.messagesystem.front.FrontendService;
import ru.otus.hw15.model.User;
import ru.otus.hw15.services.UserService;

import java.util.List;

@Controller
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
