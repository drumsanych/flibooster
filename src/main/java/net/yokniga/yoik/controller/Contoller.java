package net.yokniga.yoik.controller;

import lombok.AllArgsConstructor;
import net.yokniga.yoik.services.HTMLParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class Contoller {
    private HTMLParser htmlParser;

    @GetMapping("/")
    public String getMainPage(@RequestParam(required = false) String search) throws IOException {
        if (search != null) {
            htmlParser.printHTML(search);
        }
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
