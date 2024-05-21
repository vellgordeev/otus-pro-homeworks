import org.junit.jupiter.api.Test;
import ru.gordeev.iterator_builder.Box;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class BoxTest {

    @Test
    void testEmptyBox() {
        Box box = new Box();
        Iterator<String> it = box.iterator();
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void testSingleElement() {
        Box box = new Box();
        box.getFirstList().add("Hello");
        Iterator<String> it = box.iterator();

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("Hello");
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void testMultipleLists() {
        Box box = new Box();
        box.getFirstList().add("First");
        box.getSecondList().add("Second");
        box.getThirdList().add("Third");
        box.getFourthList().add("Fourth");

        Iterator<String> it = box.iterator();

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("First");
        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("Second");
        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("Third");
        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("Fourth");
        assertThat(it.hasNext()).isFalse();
    }

    @Test
    void testNoSuchElementException() {
        Box box = new Box();
        box.getFirstList().add("OnlyOne");
        Iterator<String> it = box.iterator();

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("OnlyOne");
        assertThat(it.hasNext()).isFalse();
        assertThatThrownBy(it::next).isInstanceOf(NoSuchElementException.class);
    }
}