import org.junit.jupiter.api.Test;
import ru.gordeev.Box;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BoxTest {

    @Test
    void testEmptyBox() {
        Box box = new Box();
        Iterator<String> it = box.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void testSingleElement() {
        Box box = new Box();
        box.firstList.add("Hello");
        Iterator<String> it = box.iterator();

        assertTrue(it.hasNext());
        assertEquals("Hello", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testMultipleLists() {
        Box box = new Box();
        box.firstList.add("First");
        box.secondList.add("Second");
        box.thirdList.add("Third");
        box.fourthList.add("Fourth");

        Iterator<String> it = box.iterator();

        assertTrue(it.hasNext());
        assertEquals("First", it.next());
        assertTrue(it.hasNext());
        assertEquals("Second", it.next());
        assertTrue(it.hasNext());
        assertEquals("Third", it.next());
        assertTrue(it.hasNext());
        assertEquals("Fourth", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testNoSuchElementException() {
        Box box = new Box();
        box.firstList.add("OnlyOne");
        Iterator<String> it = box.iterator();

        assertTrue(it.hasNext());
        assertEquals("OnlyOne", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
}
