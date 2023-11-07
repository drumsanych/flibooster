package org.flibooster.controller;

import lombok.AllArgsConstructor;
import org.flibooster.repository.FlibustaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {
    private FlibustaRepository htmlParser;

    @GetMapping("/")
    public String getMainPage(@RequestParam(required = false) String search) throws IOException {
        if (search != null) {
            htmlParser.searchBooks(search);
        }
        return "index";
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
