package kz.iitu.diploma.project.model.dto;

import kz.iitu.diploma.project.model.Chat;
import lombok.Builder;

@Builder
public class ChatDto {

    public Long id;
    public Long senderId;
    public Long receiverId;
    public String uniqueId;

    public Chat toCreateEntity() {
        return Chat.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .uniqueId(uniqueId)
                .build();
    }

}
