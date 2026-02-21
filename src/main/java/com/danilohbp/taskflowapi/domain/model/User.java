package com.danilohbp.taskflowapi.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Enumeration;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected User() { }

    public User(String name, String email, UserRole role) {
        this.name = normalizeName(name);
        this.email = normalizeEmail(email);
        this.role = role == null ? UserRole.USER : role;
        this.active = true;
    }

    public boolean canManageAllTasks() {
        return role == UserRole.ADMIN || role == UserRole.MANAGER;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public void changeName(String name) {
        this.name = normalizeName(name);
    }

    public void changeEmail(String email) {
        this.email = normalizeEmail(email);
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private static String normalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        return name.trim();
    }

    private static String normalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        String e = email.trim().toLowerCase();

        if (!e.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        return e;
    }
}
