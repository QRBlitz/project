package kz.iitu.diploma.project.service;

import kz.iitu.diploma.project.model.Message;
import kz.iitu.diploma.project.model.dto.MessageDto;

import java.util.List;

public interface MessageService {

    Message create(MessageDto messageDto);

    Message findById(Long id);

    List<Message> findChatMessages(Long senderId, Long receiverId);

    Long countNewMessages(Long senderId, Long receiverId);

}
