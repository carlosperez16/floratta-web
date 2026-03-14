package com.floratta.repository;

import com.floratta.model.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByCorreo(String correo);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.fechaExpiracion < :now")
    void deleteExpiredTokens(LocalDateTime now);
}