package kz.iitu.diploma.project.repository;

import jakarta.transaction.Transactional;
import kz.iitu.diploma.project.model.Message;
import kz.iitu.diploma.project.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    @Transactional
    @Modifying
    @Query(value = "update Message m set m.status = :status where m.sender.id = :senderId and m.receiver.id = :receiverId")
    void updateStatus(Long senderId, Long receiverId, MessageStatus status);

    List<Message> findByChatUniqueId(String chatUniqueId);

    Long countBySender_IdAndReceiver_IdAndStatus(Long sender, Long receiver, MessageStatus status);

}
