package ru.gordeev.spring_boot_serialization.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Message {
    @JsonProperty("ROWID")
    private Long rowId;

    @JsonProperty("attributedBody")
    private String attributedBody;

    @JsonProperty("belong_number")
    private String belongNumber;

    @JsonProperty("date")
    private Long date;

    @JsonProperty("date_read")
    private Long dateRead;

    @JsonProperty("guid")
    private String guid;

    @JsonProperty("handle_id")
    private Long handleId;

    @JsonProperty("has_dd_results")
    private int hasDdResults;

    @JsonProperty("is_deleted")
    private int isDeleted;

    @JsonProperty("is_from_me")
    private int isFromMe;

    @JsonProperty("send_date")
    private String sendDate;

    @JsonProperty("send_status")
    private int sendStatus;

    @JsonProperty("service")
    private String service;

    @JsonProperty("text")
    private String text;
}
