package com.example.springboot3jwtsecuritydemo.config

import com.example.springboot3jwtsecuritydemo.domain.enums.Permission
import com.example.springboot3jwtsecuritydemo.domain.enums.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val logoutHandler: LogoutHandler
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/management/**").hasAnyRole(Role.ADMIN.name, Role.MANAGER.name)
                    .requestMatchers(HttpMethod.GET, "/management/**").hasAnyAuthority(Permission.ADMIN_READ.name, Permission.MANAGER_READ.name)
                    .requestMatchers(HttpMethod.POST, "/management/**").hasAnyAuthority(Permission.ADMIN_CREATE.name, Permission.MANAGER_CREATE.name)
                    .requestMatchers(HttpMethod.PUT, "/management/**").hasAnyAuthority(Permission.ADMIN_UPDATE.name, Permission.MANAGER_UPDATE.name)
                    .requestMatchers(HttpMethod.DELETE, "/management/**").hasAnyAuthority(Permission.ADMIN_DELETE.name, Permission.MANAGER_DELETE.name)
                    // .requestMatchers("/admin/**").hasRole(Role.ADMIN.name)
                    // .requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Permission.ADMIN_READ.name)
                    // .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Permission.ADMIN_CREATE.name)
                    // .requestMatchers(HttpMethod.PUT, "/admin/**").hasAuthority(Permission.ADMIN_UPDATE.name)
                    // .requestMatchers(HttpMethod.DELETE, "/admin/**").hasAuthority(Permission.ADMIN_DELETE.name)
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout {
                it.logoutUrl("/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler { _, _, _ -> SecurityContextHolder.clearContext() }
            }
        return http.build()
    }
}
