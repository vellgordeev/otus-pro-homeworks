package ru.gordeev.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gordeev.AnnotationProccessor;

import java.util.HashMap;

import static ru.gordeev.helpers.TestStatus.*;

public class Reporter {

    private final Logger logger;
    private HashMap<TestStatus, Integer> resultedReport;

    public Reporter() {
        this.resultedReport = new HashMap<>();
        this.logger = LogManager.getLogger(AnnotationProccessor.class);
    }

    public void clearResults() {
        resultedReport.clear();
        resultedReport.put(SUCCESSFUL, 0);
        resultedReport.put(FAILED, 0);
        resultedReport.put(SKIPPED, 0);
    }

    public void addSuccessful() {
        resultedReport.put(SUCCESSFUL, resultedReport.get(SUCCESSFUL) + 1);
    }

    public void addFailed() {
        resultedReport.put(FAILED, resultedReport.get(FAILED) + 1);
    }

    public void addSkipped() {
        resultedReport.put(SKIPPED, resultedReport.get(SKIPPED) + 1);
    }

    public void printResults() {
        StringBuilder report = new StringBuilder();

        report.append("\n================================ Test report =================================\n");

        // Зеленый для успешных тестов
        report.append("\u001B[32m").append(String.format("Successful tests: %d", resultedReport.get(SUCCESSFUL))).append("\u001B[0m\n");
        // Красный для неудачных
        report.append("\u001B[31m").append(String.format("Failed tests:     %d", resultedReport.get(FAILED))).append("\u001B[0m\n");
        // Стандартный цвет для пропущенных
        report.append(String.format("Skipped tests:    %d", resultedReport.get(SKIPPED))).append("\n");

        int totalTests = resultedReport.values().stream().mapToInt(Integer::intValue).sum();
        report.append(String.format("Total tests:      %d", totalTests)).append("\n");

        report.append("================================ End of report ================================\n");

        logger.info(report.toString());
    }

}
