package ru.gordeev.spring_boot_serialization.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Member {
    @JsonProperty("first")
    private String firstName;

    @JsonProperty("handle_id")
    private Long handleId;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("last")
    private String lastName;

    @JsonProperty("middle")
    private String middleName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("service")
    private String service;

    @JsonProperty("thumb_path")
    private String thumbPath;
}
