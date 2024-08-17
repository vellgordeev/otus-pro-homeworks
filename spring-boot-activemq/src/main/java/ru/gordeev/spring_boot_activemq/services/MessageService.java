package ru.gordeev.spring_boot_activemq.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.gordeev.spring_boot_activemq.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final List<Message> receivedMessages = new ArrayList<>();

    private final JmsTemplate jmsTemplate;

    @Autowired
    public MessageService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(Message message) {
        logger.info("Sending message: UUID={}, Text={}", message.getUuid(), message.getText());
        jmsTemplate.convertAndSend("message-topic", message);
    }

    @JmsListener(destination = "message-topic")
    public void receiveMessage(Message message) {
        logger.info("Received message: UUID={}, Text={}", message.getUuid(), message.getText());
        receivedMessages.add(message);
    }

    public List<Message> getAllMessages() {
        return Collections.unmodifiableList(receivedMessages);
    }
}
