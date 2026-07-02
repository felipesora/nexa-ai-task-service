package com.nexa.task.infra.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalArgumentException("Usuário não autenticado.");
        }

        return user;
    }

    public boolean isAdmin() {
        return getAuthenticatedUser().role().equals("ADMIN");
    }

    public void validateOwnerOrAdmin(Long ownerId) {
        AuthenticatedUser user = getAuthenticatedUser();

        if (!isAdmin() && !user.id().equals(ownerId)) {
            throw new ForbiddenException("Você só pode acessar recursos pertencentes ao seu usuário.");
        }
    }
}
