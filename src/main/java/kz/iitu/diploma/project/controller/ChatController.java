package kz.iitu.diploma.project.controller;

import kz.iitu.diploma.project.model.ChatNotification;
import kz.iitu.diploma.project.model.Message;
import kz.iitu.diploma.project.model.dto.MessageDto;
import kz.iitu.diploma.project.service.impl.ChatServiceImpl;
import kz.iitu.diploma.project.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageServiceImpl messageService;
    private final ChatServiceImpl chatService;

    @PostMapping("/processMessage")
    @MessageMapping("/processMessage")
    public void processMessage(@Payload MessageDto messageDto) {
        try {
            var chatUniqueId = chatService
                    .getChatId(messageDto.senderId, messageDto.receiverId, true);
            messageDto.chatUniqueId = chatUniqueId.get();

            Message message = messageService.create(messageDto);
            messagingTemplate.convertAndSendToUser(
                    message.getReceiver().getId().toString(), "/queue/messages",
                    new ChatNotification(
                            message.getId(),
                            message.getSender().getId(),
                            message.getSender().getFirstName()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
