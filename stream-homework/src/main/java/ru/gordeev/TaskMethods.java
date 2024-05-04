package ru.gordeev;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskMethods {

    private TaskMethods() {
    }

    public static List<Task> getTaskListByStatus(List<Task> taskList, TaskStatus taskStatus) {
        if (taskList == null || taskStatus == null) {
            return Collections.emptyList();
        }

        return taskList.stream()
                .filter(task -> task != null && task.getStatus() != null)
                .filter(task -> task.getStatus().equals(taskStatus))
                .toList();
    }

    public static boolean isTaskExistWithId(List<Task> taskList, int id) {
        if (taskList == null) {
            return false;
        }

        return taskList.stream()
                .anyMatch(task -> task != null && task.getId() == id);
    }

    public static List<Task> getSortedListOfTasksByStatus(List<Task> taskList) {
        if (taskList == null) {
            return Collections.emptyList();
        }

        return taskList.stream()
                .filter(task -> task != null && task.getStatus() != null)
                .sorted(Comparator.comparingInt(task -> task.getStatus().ordinal()))
                .toList();
    }

    public static long getTaskCountByStatus(List<Task> taskList, TaskStatus taskStatus) {
        if (taskList == null || taskStatus == null) {
            return 0L;
        }

        return taskList.stream()
                .filter(task -> task != null && task.getStatus() != null)
                .filter(task -> task.getStatus().equals(taskStatus))
                .count();
    }
}
