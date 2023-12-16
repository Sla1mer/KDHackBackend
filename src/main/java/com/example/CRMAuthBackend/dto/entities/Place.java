package com.example.CRMAuthBackend.dto.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "places")
@Data
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int countInvalid;
    private int countNormal;
}
