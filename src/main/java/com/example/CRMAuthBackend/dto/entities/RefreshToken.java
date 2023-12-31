package com.example.CRMAuthBackend.dto.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "refresh_token")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
