package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Integer> {
}
