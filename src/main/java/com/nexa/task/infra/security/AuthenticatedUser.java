package com.nexa.task.infra.security;

public record AuthenticatedUser(
        Long id,
        String email,
        String role
) {
}
