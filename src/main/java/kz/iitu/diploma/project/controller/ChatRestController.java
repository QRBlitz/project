package kz.iitu.diploma.project.controller;


import kz.iitu.diploma.project.model.Message;
import kz.iitu.diploma.project.model.dto.MessageDto;
import kz.iitu.diploma.project.service.impl.AesCipherImpl;
import kz.iitu.diploma.project.service.impl.ChatServiceImpl;
import kz.iitu.diploma.project.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final MessageServiceImpl messageService;
    private final AesCipherImpl aesCipherImpl;

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable Long senderId,
            @PathVariable Long recipientId) {
        return ResponseEntity
                .ok(messageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/findChatMessages")
    public ResponseEntity<?> findChatMessages(@RequestBody MessageDto messageDto) throws Exception {
        List<Message> list = messageService.findChatMessages(messageDto.senderId, messageDto.receiverId);
        for (Message message : list) {
            message.setMessageText(aesCipherImpl.decrypt(messageDto.hexCode, message.getMessageText()));
        }
        return ResponseEntity
                .ok(list);
    }

    @GetMapping("/messages/getById")
    public ResponseEntity<?> findMessage(@RequestBody MessageDto messageDto) throws Exception {
        Message message = messageService.findById(messageDto.id);
        message.setMessageText(aesCipherImpl.decrypt(messageDto.hexCode, message.getMessageText()));
        return ResponseEntity.ok(message);
    }

}
