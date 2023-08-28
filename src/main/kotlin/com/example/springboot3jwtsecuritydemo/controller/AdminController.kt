package com.example.springboot3jwtsecuritydemo.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    fun get() = "GET:: admin controller"

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    fun post() = "POST:: admin controller"

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    fun put() = "PUT:: admin controller"

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    fun delete() = "DELETE:: admin controller"
}
