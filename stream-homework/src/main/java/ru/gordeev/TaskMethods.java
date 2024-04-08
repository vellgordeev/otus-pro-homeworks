package ru.gordeev;

import java.util.Comparator;
import java.util.List;

public class TaskMethods {

    private TaskMethods() {
    }

    public static List<Task> getTaskListByStatus(List<Task> taskList, TaskStatus taskStatus) {
        return taskList.stream()
                .filter(task -> task.getStatus().equals(taskStatus))
                .toList();
    }

    public static boolean isTaskExistWithId(List<Task> taskList, int id) {
        return taskList.stream()
                .anyMatch(task -> task.getId() == id);
    }

    public static List<Task> getSortedListOfTasksByStatus(List<Task> taskList) {
        return taskList.stream()
                .sorted(Comparator.comparingInt(task -> task.getStatus().ordinal()))
                .toList();
    }

    public static long getTaskCountByStatus(List<Task> taskList, TaskStatus taskStatus) {
        return taskList.stream()
                .filter(task -> task.getStatus().equals(taskStatus))
                .count();
    }
}
