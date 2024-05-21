package ru.gordeev;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Task {

    private int id;

    private String name;

    private TaskStatus status;
}
