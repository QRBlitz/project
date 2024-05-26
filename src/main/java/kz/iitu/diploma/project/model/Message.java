package kz.iitu.diploma.project.model;

import jakarta.persistence.*;
import kz.iitu.diploma.project.model.core.UpdateEntity;
import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message extends UpdateEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private User receiver;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "chat_unique_id")
    private String chatUniqueId;

    @Column(name = "status")
    @Builder.Default
    private MessageStatus status = MessageStatus.RECEIVED;

}
