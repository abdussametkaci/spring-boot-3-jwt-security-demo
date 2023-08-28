package com.example.springboot3jwtsecuritydemo.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/management")
class ManagementController {

    @GetMapping
    fun get() = "GET:: management controller"

    @PostMapping
    fun post() = "POST:: management controller"

    @PutMapping
    fun put() = "PUT:: management controller"

    @DeleteMapping
    fun delete() = "DELETE:: management controller"
}
