import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.gordeev.Task;
import ru.gordeev.TaskMethods;
import ru.gordeev.TaskStatus;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.gordeev.TaskStatus.*;

class TaskMethodsTests {

    private static List<Task> tasks;

    @BeforeAll
    static void fillTasksList() {
        tasks = List.of(
                new Task(1, "WriteCode", IN_PROGRESS),
                new Task(2, "CleanRoom", OPEN),
                new Task(3, "WalkTheDog", CLOSED),
                new Task(4, "Eat", CLOSED),
                new Task(5, "PrepareTheFood", OPEN),
                new Task(6, "Study", IN_PROGRESS));
    }

    @Test
    void getTaskListByStatusTest() {
        List<Task> taskListByStatus = TaskMethods.getTaskListByStatus(tasks, OPEN);

        assertThat(taskListByStatus).isEqualTo(List.of(tasks.get(1), tasks.get(4)));
    }

    @Test
    void getTaskListByStatusTestNegative() {
        List<Task> taskListByStatus = TaskMethods.getTaskListByStatus(null, OPEN);

        assertThatList(taskListByStatus).isEmpty();
    }

    @Test
    void isTaskExistWithIdTestPositive() {
        assertTrue(TaskMethods.isTaskExistWithId(tasks, 1));
    }

    @Test
    void isTaskExistWithIdTestNull() {
        assertFalse(TaskMethods.isTaskExistWithId(null, 1));
    }

    @Test
    void isTaskExistWithIdTestNegative() {
        assertFalse(TaskMethods.isTaskExistWithId(tasks, 100));
    }

    @Test
    void testGetSortedListOfTasksByStatusTest() {
        List<Task> sortedTasks = TaskMethods.getSortedListOfTasksByStatus(tasks);

        assertThat(sortedTasks).isSortedAccordingTo(
                Comparator.comparingInt(task -> task.getStatus().ordinal())
        );
    }

    @Test
    void testGetSortedListOfTasksByStatusTestNull() {
        List<Task> sortedTasks = TaskMethods.getSortedListOfTasksByStatus(null);

        assertThatList(sortedTasks).isEmpty();
    }

    @Test
    void getTaskCountByStatusTest() {
        assertThat(TaskMethods.getTaskCountByStatus(tasks, OPEN)).isEqualTo(2);
    }

    @Test
    void getTaskCountByStatusTestNull() {
        assertThat(TaskMethods.getTaskCountByStatus(null, OPEN)).isZero();
    }
}
