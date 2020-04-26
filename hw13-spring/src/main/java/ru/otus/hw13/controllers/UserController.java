package ru.otus.hw13.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw13.model.User;
import ru.otus.hw13.services.TestDataService;
import ru.otus.hw13.services.UserService;

import java.util.List;

@Controller
public class UserController {

    private final UserService repository;
    private final TestDataService userTestDataService;

    public UserController(UserService repository, @Qualifier("UserTestDataService")TestDataService userTestDataService) {
        this.repository = repository;
        this.userTestDataService = userTestDataService;
    }

    @GetMapping({"/", "/user/list"})
    public String userListView(Model model) {
        List<User> users = repository.getAll();
        model.addAttribute("users", users);
        return "userList.html";
    }

    @GetMapping("/user/create")
    public String userCreateView(Model model) {
        model.addAttribute("user", new User());
        return "userCreate.html";
    }

    @PostMapping("/user/generate_test_data")
    public RedirectView generateTestData() {
        userTestDataService.fillDb();
        return new RedirectView("/user/list", true);
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute User user) {
        repository.saveUser(user);
        return new RedirectView("/user/list", true);
    }
}
