package ru.gordeev.demospringboot.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private ErrorType type;
    private String message;
    private String description;
}
