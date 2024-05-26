package kz.iitu.diploma.project.service.impl;

import com.sun.jdi.InternalException;
import kz.iitu.diploma.project.exception.CustomException;
import kz.iitu.diploma.project.model.Chat;
import kz.iitu.diploma.project.model.Message;
import kz.iitu.diploma.project.service.MessageService;
import kz.iitu.diploma.project.model.MessageStatus;
import kz.iitu.diploma.project.model.User;
import kz.iitu.diploma.project.model.dto.MessageDto;
import kz.iitu.diploma.project.repository.ChatRepository;
import kz.iitu.diploma.project.repository.MessageRepository;
import kz.iitu.diploma.project.repository.UserRepository;
import kz.iitu.diploma.project.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;

    public Message create(MessageDto messageDto) {
        log.info(messageDto.chatUniqueId);
            Optional<Chat> chatExist = chatRepository.findByUniqueIdAndSenderId(messageDto.chatUniqueId, messageDto.senderId);
            if (chatExist.isEmpty()) {
                throw new NotFoundException(CustomException.CHAT_NOT_FOUND);
            }

            User sender = userRepository.findById(messageDto.senderId).get();
            User receiver = userRepository.findById(messageDto.receiverId).get();

            messageDto.status = MessageStatus.RECEIVED;
            Message message = messageDto.toCreateEntity(sender, receiver);
            message = messageRepository.save(message);

            return message;
    }

    public Message findById(Long id) {
        return messageRepository.findById(id).map(message -> {
            message.setStatus(MessageStatus.DELIVERED);
            return messageRepository.save(message);
        }).orElseThrow(() -> new NotFoundException(CustomException.MESSAGE_NOT_FOUND));
    }

    public List<Message> findChatMessages(Long senderId, Long receiverId) {
        var chatUniqueId = chatService.getChatId(senderId, receiverId, false);

        var messages =
                chatUniqueId.map(messageRepository::findByChatUniqueId).orElse(new ArrayList<>());

        if (!messages.isEmpty()) {
            messageRepository.updateStatus(senderId, receiverId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public Long countNewMessages(Long senderId, Long receiverId) {
        return messageRepository.countBySender_IdAndReceiver_IdAndStatus(
                senderId, receiverId, MessageStatus.RECEIVED);
    }

}
