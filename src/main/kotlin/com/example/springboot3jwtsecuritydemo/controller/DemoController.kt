package com.example.springboot3jwtsecuritydemo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class DemoController {

    @GetMapping
    fun hello() = "Hello from secured endpoint"
}
