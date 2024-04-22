import ru.gordeev.AnnotationProcessor;

public class MainTest {
    private static final AnnotationProcessor ANNOTATION_PROCESSOR = new AnnotationProcessor();

    public static void main(String[] args) {
        ANNOTATION_PROCESSOR.run(Tests.class);
    }
}
