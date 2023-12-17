package com.example.CRMAuthBackend.dto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Codes2FADto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String phoneNumber;
    private String code;

    public Codes2FADto(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }
}

