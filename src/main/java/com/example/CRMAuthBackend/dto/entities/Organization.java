package com.example.CRMAuthBackend.dto.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "organizations")
@Data
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
