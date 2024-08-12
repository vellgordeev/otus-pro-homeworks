package ru.gordeev.spring_boot_serialization.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.gordeev.spring_boot_serialization.dto.GroupedMessagesDTO;
import ru.gordeev.spring_boot_serialization.dto.ProcessedMessageDTO;
import ru.gordeev.spring_boot_serialization.entities.SmsData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class SmsServiceImpl implements SmsService {

    public List<GroupedMessagesDTO> processSmsData(SmsData smsData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

        return smsData.getChatSessions().stream()
                .flatMap(chatSession -> chatSession.getMessages().stream()
                        .map(message -> {
                            ProcessedMessageDTO dto = new ProcessedMessageDTO();
                            dto.setChatIdentifier(chatSession.getChatIdentifier());
                            dto.setMemberLastName(chatSession.getMembers().get(0).getLastName());
                            dto.setBelongNumber(message.getBelongNumber());
                            dto.setSendDate(message.getSendDate());
                            dto.setText(message.getText());
                            return dto;
                        })
                )
                .collect(Collectors.groupingBy(ProcessedMessageDTO::getBelongNumber))
                .entrySet().stream()
                .map(entry -> {
                    List<ProcessedMessageDTO> sortedMessages = entry.getValue().stream()
                            .sorted(Comparator.comparing(dto -> LocalDateTime.parse(dto.getSendDate(), formatter)))
                            .toList();
                    GroupedMessagesDTO groupedMessagesDTO = new GroupedMessagesDTO();
                    groupedMessagesDTO.setBelongNumber(entry.getKey());
                    groupedMessagesDTO.setMessages(sortedMessages);
                    return groupedMessagesDTO;
                })
                .toList();
    }
}
