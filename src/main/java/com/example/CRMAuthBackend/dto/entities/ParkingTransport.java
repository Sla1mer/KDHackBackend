package com.example.CRMAuthBackend.dto.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "parking_transport")
@Data
public class ParkingTransport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String carNumber;
    private boolean isInvalid;

    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    private LocalDateTime timeEntry;
}
