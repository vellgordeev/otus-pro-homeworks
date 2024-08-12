package ru.gordeev.spring_boot_serialization.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ProcessedMessageDTO {
    private String chatIdentifier;
    private String memberLastName;
    private String belongNumber;
    private String sendDate;
    private String text;
}
