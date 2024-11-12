package com.example.hardware.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String redirectToDefaultPage() {
        return "redirect:/catalog/showComponentsCatalog";
    }
}
