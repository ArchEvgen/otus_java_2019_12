package ru.otus.hw13.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw13.services.GreetingService;

import java.util.Map;

@RestController
public class GreetingRestController {
    private static final Logger logger = LoggerFactory.getLogger(GreetingRestController.class);
    private final GreetingService greetingService;

    public GreetingRestController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    //http://localhost:8080/DIhello/hello?name=ddd
    @GetMapping("/hello")
    //@ResponseBody
    public String sayHelloOldSchoolWithGsonMapping(@RequestParam(name = "name") String name) {
        return new Gson().toJson(this.greetingService.sayHello(name));
    }

    //http://localhost:8080/DIhello/hello/jone
    @RequestMapping(method = RequestMethod.GET, value = "/hello/{name}")
    public Map<String, String> sayHelloRestApiStyleWithJacksonMapping(@PathVariable("name") String name) {
        return this.greetingService.sayHello(name);
    }

}
