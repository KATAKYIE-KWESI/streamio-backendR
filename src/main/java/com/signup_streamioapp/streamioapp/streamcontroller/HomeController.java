package com.signup_streamioapp.streamioapp.streamcontroller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "✅ Streamio Backend is LIVE!";
    }
}

