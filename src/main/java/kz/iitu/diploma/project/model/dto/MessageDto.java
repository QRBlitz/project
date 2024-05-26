package kz.iitu.diploma.project.model.dto;

import kz.iitu.diploma.project.model.Message;
import kz.iitu.diploma.project.model.MessageStatus;
import kz.iitu.diploma.project.model.User;
import lombok.Builder;

@Builder
public class MessageDto {

    public Long id;
    public Long senderId;
    public Long receiverId;
    public String messageText;
    public String chatUniqueId;
    public MessageStatus status;
    public String hexCode;

    public Message toCreateEntity(User sender, User receiver) {
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .chatUniqueId(chatUniqueId)
                .messageText(messageText)
                .build();
    }

}
