package com.saniazt.restapi.RestApp1.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // = @Controller + @ResponseBody in each method
@RequestMapping("/api")
public class FirstRestController {


    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello world!";
    }
}
