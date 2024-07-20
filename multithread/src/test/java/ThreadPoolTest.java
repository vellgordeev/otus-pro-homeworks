import org.junit.jupiter.api.Test;
import ru.gordeev.ThreadPool;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ThreadPoolTest {

    @Test
    public void testThreadPoolExecution() throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(3);
        AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                counter.incrementAndGet();
                try {
                    Thread.sleep(100);  //Imitation of work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        Thread.sleep(2000); // Time to execute all tasks
        assertEquals(10, counter.get());

        threadPool.shutdown();
    }

    @Test
    public void testShutdownPreventsNewTasks() {
        ThreadPool threadPool = new ThreadPool(2);
        threadPool.shutdown();

        assertThrows(IllegalStateException.class, () -> {
            threadPool.execute(() -> System.out.println("This should not be printed"));
        });
    }

    @Test
    public void testShutdownCompletesExistingTasks() throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(2);
        AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < 5; i++) {
            threadPool.execute(() -> {
                counter.incrementAndGet();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        threadPool.shutdown();

        Thread.sleep(1000);
        assertEquals(5, counter.get());
    }
}
