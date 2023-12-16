package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.ParkingTransport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingTransportRepository extends JpaRepository<ParkingTransport, Integer> {
    ParkingTransport findByCarNumber(String carNumber);
    int countByParkIdAndIsInvalid(int parkId, boolean isInvalid);
}
