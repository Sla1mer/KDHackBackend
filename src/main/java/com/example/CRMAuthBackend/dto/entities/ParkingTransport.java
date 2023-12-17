package com.example.CRMAuthBackend.dto.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "parking_transport")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingTransport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String carNumber;
    private boolean isInvalid;
    private boolean isBusy;

    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    private LocalDateTime timeEntry;

    public ParkingTransport(String carNumber, boolean isBusy, Park park) {
        this.carNumber = carNumber;
        this.isBusy = isBusy;
        this.park = park;
    }
}
