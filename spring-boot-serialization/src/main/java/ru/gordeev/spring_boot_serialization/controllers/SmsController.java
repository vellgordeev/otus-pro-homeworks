package ru.gordeev.spring_boot_serialization.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.gordeev.spring_boot_serialization.dto.GroupedMessagesDTO;
import ru.gordeev.spring_boot_serialization.entities.SmsData;
import ru.gordeev.spring_boot_serialization.service.SmsService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {
    private final SmsService smsService;

    @Autowired
    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> processSmsFile(@RequestParam("file") MultipartFile file,
                                            @RequestHeader("Accept") String acceptHeader) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        SmsData smsData = objectMapper.readValue(file.getBytes(), SmsData.class);

        List<GroupedMessagesDTO> processedMessages = smsService.processSmsData(smsData);

        if (MediaType.APPLICATION_XML_VALUE.equals(acceptHeader)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(xmlMapper.writeValueAsString(processedMessages));
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(processedMessages);
        }
    }
}
