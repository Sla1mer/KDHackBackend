package com.example.CRMAuthBackend.dto.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "parks")
@Data
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private double lat;
    private double lon;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne
    @JoinColumn(name = "place_id")
    private Place place;
}
