package com.danilohbp.taskflowapi.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TaskPriority priority;

    private LocalDateTime dueDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;


    protected Task() { }

    public Task(User reporter, String title, TaskPriority priority, LocalDateTime dueDate) {
        this.reporter = Objects.requireNonNull(reporter, "Reporter is required");
        this.title = normalizeTitle(title);
        this.priority = Objects.requireNonNull(priority, "Priority is required");
        this.dueDate = dueDate;
        this.status = TaskStatus.TO_DO;
    }

    public void updateDetails(String title, String description, TaskPriority priority, LocalDateTime dueDate) {
        this.title = normalizeTitle(title);
        this.description = normalizeDescription(description);
        this.priority = Objects.requireNonNull(priority, "Priority is required");
        this.dueDate = dueDate;
    }

    public void assignTo(User assignee) {
        this.assignee = assignee;
    }

    public void start() {
        if (this.status != TaskStatus.TO_DO) {
            throw new IllegalStateException("Task can only be started from TODO");
        }
        this.status = TaskStatus.IN_PROGRESS;
    }

    public void complete() {
        if (this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException("Task can only be completed from IN_PROGRESS");
        }
        this.status = TaskStatus.DONE;
    }

    public void cancel() {
        if (this.status == TaskStatus.DONE) {
            throw new IllegalStateException("Done task cannot be canceled");
        }
        this.status = TaskStatus.CANCELED;
    }

    public void reopen() {
        if (this.status != TaskStatus.DONE && this.status != TaskStatus.CANCELED) {
            throw new IllegalStateException("Only DONE or CANCELED Tasks can be reopened");
        }
        this.status = TaskStatus.TO_DO;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = TaskStatus.TO_DO;
        if (this.priority == null) this.priority = TaskPriority.MEDIUM;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private static String normalizeTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        String t = title.trim();
        if (t.length() > 120) {
            throw new IllegalArgumentException("Title must be <= 120 chars");
        }
        return t;
    }

    private static String normalizeDescription(String description) {
        if (description == null) return null;
        String d = description.trim();
        if (d.isEmpty()) return null;
        if (d.length() > 2000) {
            throw new IllegalArgumentException("Description must be <= 2000 chars");
        }
        return d;
    }
}
