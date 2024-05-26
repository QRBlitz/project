package kz.iitu.diploma.project.model;

import jakarta.persistence.*;
import kz.iitu.diploma.project.model.core.BaseEntity;
import lombok.*;

@Entity
@Table(name = "chat")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseEntity {

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "unique_id", nullable = false)
    private String uniqueId;

}