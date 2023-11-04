package net.yokniga.yoik.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Contoller {

    @GetMapping("/")
    public String getMainPage() {
        return "main";
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/add")
    public String getHelloPage() {
        return "add";
    }
}
