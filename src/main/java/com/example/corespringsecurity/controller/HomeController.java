package com.example.corespringsecurity.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String home(HttpSession session, Model model) throws Exception {
        Boolean success = (Boolean) session.getAttribute("success");
        model.addAttribute("success", success);
        if (success != null) {
            session.setAttribute("success", null);
        }
        return "home";
    }

}
