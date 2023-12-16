package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Park;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkRepository extends JpaRepository<Park, Integer> {
}
