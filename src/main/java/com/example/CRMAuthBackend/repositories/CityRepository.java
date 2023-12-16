package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}
