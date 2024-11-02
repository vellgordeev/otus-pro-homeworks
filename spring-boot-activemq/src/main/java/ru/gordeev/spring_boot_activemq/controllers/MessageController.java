package ru.gordeev.spring_boot_activemq.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gordeev.spring_boot_activemq.entities.ApiResponse;
import ru.gordeev.spring_boot_activemq.entities.Message;
import ru.gordeev.spring_boot_activemq.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);

        ApiResponse response = new ApiResponse("Message sent successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }
}
