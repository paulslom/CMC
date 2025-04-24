package com.pas.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController 
{
	// handler method to handle home page request
    @GetMapping("/login")
    public String home()
    {
        return "login";
    }

}
