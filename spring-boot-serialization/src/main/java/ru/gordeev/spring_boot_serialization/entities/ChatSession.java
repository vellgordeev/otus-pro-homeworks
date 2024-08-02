package ru.gordeev.spring_boot_serialization.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatSession {
    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("chat_identifier")
    private String chatIdentifier;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("is_deleted")
    private int isDeleted;

    @JsonProperty("members")
    private List<Member> members;

    @JsonProperty("messages")
    private List<Message> messages;
}
