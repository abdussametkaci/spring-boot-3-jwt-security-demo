package com.example.springboot3jwtsecuritydemo.domain.enums

import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class Role(val permissions: Set<Permission>) {
    USER(emptySet()),
    MANAGER(
        setOf(
            Permission.MANAGER_READ,
            Permission.MANAGER_CREATE,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_DELETE
        )
    ),
    ADMIN(
        setOf(
            Permission.ADMIN_READ,
            Permission.ADMIN_CREATE,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE,
            *MANAGER.permissions.toTypedArray()
        )
    )
}

fun Role.getAuthorities(): List<SimpleGrantedAuthority> {
    return this.permissions.map { SimpleGrantedAuthority(it.permission) } + listOf(SimpleGrantedAuthority("ROLE_${this.name}"))
}
