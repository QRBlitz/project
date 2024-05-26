package kz.iitu.diploma.project.repository;

import kz.iitu.diploma.project.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    @Query(value = "select * from verification_code where email = :email and extract(epoch from (now() - created_date)) < 300 limit 1", nativeQuery = true)
    VerificationCode checkIsAlreadySentByEmail(String email);

}
