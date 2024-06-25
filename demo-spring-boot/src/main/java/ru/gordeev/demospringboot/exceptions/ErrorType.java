package ru.gordeev.demospringboot.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorType {
    INVALID_REQUEST,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR,
    MISSING_REQUIRED_FIELDS
}
