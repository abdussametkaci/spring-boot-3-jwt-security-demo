package com.example.springboot3jwtsecuritydemo

import com.example.springboot3jwtsecuritydemo.domain.dto.request.RegisterRequest
import com.example.springboot3jwtsecuritydemo.domain.enums.Role
import com.example.springboot3jwtsecuritydemo.service.AuthenticationService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SpringBoot3JwtSecurityDemoApplication {

    @Bean
    fun commandLineRunner(authenticationService: AuthenticationService) = CommandLineRunner {
        val admin = RegisterRequest(
            name = "Admin",
            surname = "Admin",
            email = "admin@mail.com",
            password = "password",
            role = Role.ADMIN
        )

        println("Admin Access Token: ${authenticationService.register(admin).accessToken}")

        val manager = RegisterRequest(
            name = "Manager",
            surname = "Manager",
            email = "manager@mail.com",
            password = "password",
            role = Role.MANAGER
        )

        println("Manager Access Token: ${authenticationService.register(manager).accessToken}")
    }
}

fun main(args: Array<String>) {
    runApplication<SpringBoot3JwtSecurityDemoApplication>(*args)
}
