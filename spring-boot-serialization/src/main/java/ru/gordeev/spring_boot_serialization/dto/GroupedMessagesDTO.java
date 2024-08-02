package ru.gordeev.spring_boot_serialization.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class GroupedMessagesDTO {
    private String belongNumber;
    private List<ProcessedMessageDTO> messages;
}
