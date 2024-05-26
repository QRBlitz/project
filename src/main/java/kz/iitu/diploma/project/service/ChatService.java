package kz.iitu.diploma.project.service;

import java.util.Optional;

public interface ChatService {

    Optional<String> getChatId(Long senderId, Long receiverId, boolean createIfNotExists);

}
