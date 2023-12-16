package com.example.CRMAuthBackend.dto.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tariffs")
@Data
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;
    private short cost;

    @ManyToOne
    @JoinColumn(name = "park_id")
    private Park park;
}
