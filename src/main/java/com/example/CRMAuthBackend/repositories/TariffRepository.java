package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TariffRepository extends JpaRepository<Tariff, Integer> {
    Tariff findByType(String type);
    List<Tariff> findByParkId(int parkId);

    @Query(value = "SELECT MIN(cost) FROM tariffs WHERE park_id = :parkId AND cost > 0", nativeQuery = true)
    Short findMinCostByParkId(@Param("parkId") int parkId);
}
