package com.danilohbp.taskflowapi.application.service;

import com.danilohbp.taskflowapi.domain.model.Task;
import com.danilohbp.taskflowapi.domain.model.User;

public final class TaskPermission {

    private TaskPermission() {}

    // =====================================================
    // GENERIC EDIT
    // =====================================================
    public static boolean canEdit(User actor, Task task) {
        if (!isActive(actor)) return false;
        if (actor.canManageAllTasks()) return true;

        return isReporter(actor, task)
                || isAssignee(actor, task);
    }

    // =====================================================
    // ASSIGN
    // =====================================================
    public static boolean canAssign(User actor, Task task) {
        if (!isActive(actor)) return false;

        // Admin/Manager sempre pode
        if (actor.canManageAllTasks()) return true;

        // Reporter pode atribuir
        return isReporter(actor, task);
    }

    // =====================================================
    // START
    // =====================================================
    public static boolean canStart(User actor, Task task) {
        if (!isActive(actor)) return false;

        if (actor.canManageAllTasks()) return true;

        return isAssignee(actor, task);
    }

    // =====================================================
    // COMPLETE
    // =====================================================
    public static boolean canComplete(User actor, Task task) {
        if (!isActive(actor)) return false;

        if (actor.canManageAllTasks()) return true;

        return isAssignee(actor, task);
    }

    // =====================================================
    // CANCEL
    // =====================================================
    public static boolean canCancel(User actor, Task task) {
        if (!isActive(actor)) return false;

        if (actor.canManageAllTasks()) return true;

        return isReporter(actor, task)
                || isAssignee(actor, task);
    }

    // =====================================================
    // HELPERS
    // =====================================================

    private static boolean isActive(User actor) {
        return actor != null && actor.isActive();
    }

    private static boolean isReporter(User actor, Task task) {
        return task.getReporter() != null
                && actor.getId().equals(task.getReporter().getId());
    }

    private static boolean isAssignee(User actor, Task task) {
        return task.getAssignee() != null
                && actor.getId().equals(task.getAssignee().getId());
    }
}
