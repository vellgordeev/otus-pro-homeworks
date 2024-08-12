package ru.gordeev.spring_boot_serialization.service;

import ru.gordeev.spring_boot_serialization.dto.GroupedMessagesDTO;
import ru.gordeev.spring_boot_serialization.entities.SmsData;

import java.util.List;

public interface SmsService {

    public List<GroupedMessagesDTO> processSmsData(SmsData smsData);
}
