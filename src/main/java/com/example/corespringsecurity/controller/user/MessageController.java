package com.example.corespringsecurity.controller.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {


    @GetMapping("/messages")
    public String messages() throws Exception {

        return "user/messages";
    }

    @PostMapping("/api/messages")
    @ResponseBody
    public ResponseEntity<String> apiMessage() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("messages OK");
    }


}
