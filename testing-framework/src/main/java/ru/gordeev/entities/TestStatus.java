package ru.gordeev.entities;

public enum TestStatus {
    SUCCESSFUL("Successful"), FAILED("Failed"), SKIPPED("Skipped");

    private final String status;

    TestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
