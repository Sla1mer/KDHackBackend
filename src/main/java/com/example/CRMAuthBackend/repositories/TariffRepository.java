package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TariffRepository extends JpaRepository<Tariff, Integer> {
    Tariff findByType(String type);
    List<Tariff> findByParkId(int parkId);
}
