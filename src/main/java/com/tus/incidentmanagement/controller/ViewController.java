package com.tus.incidentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login.html";
    }

    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/login.html";
    }
}