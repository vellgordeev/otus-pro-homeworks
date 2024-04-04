import ru.gordeev.AnnotationProccessor;

public class MainTest {
    private static final AnnotationProccessor ANNOTATION_PROCCESSOR = new AnnotationProccessor();

    public static void main(String[] args) {
        ANNOTATION_PROCCESSOR.run(Tests.class);
    }
}
