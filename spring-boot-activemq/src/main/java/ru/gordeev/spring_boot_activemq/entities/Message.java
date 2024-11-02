package ru.gordeev.spring_boot_activemq.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private String text;
}
