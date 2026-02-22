package com.danilohbp.taskflowapi.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String name;
    private final String email;
    private final String passwordHash;
    private final boolean active;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(
            Long id,
            String name,
            String email,
            String passwordHash,
            boolean active,
            String role
    ) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.name = Objects.requireNonNull(name, "name is required");
        this.email = Objects.requireNonNull(email, "email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash is required");
        this.active = active;

        // padrão do Spring: roles devem ser "ROLE_X"
        String normalizedRole = normalizeRole(role);
        this.authorities = List.of(new SimpleGrantedAuthority(normalizedRole));
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "ROLE_USER";
        }
        String r = role.trim().toUpperCase();
        if (r.startsWith("ROLE_")) {
            return r;
        }
        return "ROLE_" + r;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Username usado pelo Spring Security.
     * Recomendo usar email como username.
     */
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // você pode implementar regra real depois
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // você pode implementar regra real depois
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // você pode implementar regra real depois
    }

    /**
     * Aqui é o ponto mais importante:
     * usuário inativo => não autentica (ou fica "disabled")
     */
    @Override
    public boolean isEnabled() {
        return active;
    }
}
