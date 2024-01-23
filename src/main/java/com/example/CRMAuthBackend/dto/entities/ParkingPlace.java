package com.example.CRMAuthBackend.dto.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_places")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;

    private String parkPlaceName;
    private int coordinateX;
    private int coordinateY;
    private boolean isFree;
}
