package kz.iitu.diploma.project.service.impl;

import kz.iitu.diploma.project.model.Chat;
import kz.iitu.diploma.project.repository.ChatRepository;
import kz.iitu.diploma.project.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Optional<String> getChatId(Long senderId, Long receiverId, boolean createIfNotExists) {
        return chatRepository.findBySenderIdAndReceiverId(senderId, receiverId).map(Chat::getUniqueId).or(() -> {
            if (!createIfNotExists) {
                return Optional.empty();
            }
            String uniqueId = String.format("%s_%s", senderId, receiverId);

            Chat senderChat = Chat.builder()
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .uniqueId(uniqueId)
                    .build();

            Chat receiverChat = Chat.builder()
                    .senderId(receiverId)
                    .receiverId(senderId)
                    .uniqueId(uniqueId)
                    .build();

            chatRepository.save(senderChat);
            chatRepository.save(receiverChat);

            return Optional.of(uniqueId);
        });
    }

}
