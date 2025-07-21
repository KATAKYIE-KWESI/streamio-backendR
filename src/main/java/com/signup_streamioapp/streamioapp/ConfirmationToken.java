package com.signup_streamioapp.streamioapp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.signup_streamioapp.streamioapp.streammodels.User;
import com.signup_streamioapp.streamioapp.streammodels.TokenType;

@Data
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType; // 👈 NEW FIELD

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    // Updated constructor
    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, TokenType tokenType, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.tokenType = tokenType;
        this.user = user;
    }
}
