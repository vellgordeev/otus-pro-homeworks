package ru.gordeev.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
public class EntityField {
    private Field field;
    private String actualFieldName;
    private Method getter;
    private Method setter;
}
