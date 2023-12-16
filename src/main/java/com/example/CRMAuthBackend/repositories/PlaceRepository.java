package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
