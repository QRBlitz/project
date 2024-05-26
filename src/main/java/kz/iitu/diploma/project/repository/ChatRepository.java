package kz.iitu.diploma.project.repository;

import kz.iitu.diploma.project.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUniqueIdAndSenderId(String uniqueId, Long senderId);

    Optional<Chat> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
