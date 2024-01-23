package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.ParkingPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParkingPlaceRepository extends JpaRepository<ParkingPlace, Integer> {

    ParkingPlace findByParkPlaceNameAndParkId(String parkPlaceName, int parkId);

    @Query(value = "SELECT p FROM parking_places p " +
            "WHERE p.is_free = true AND p.park_id = :parkId " +
            "ORDER BY SQRT((p.coordinatex) * (p.coordinatex) + " +
            "(p.coordinatey) * (p.coordinatey)) " +
            "LIMIT 1", nativeQuery = true)
    ParkingPlace findNearestFreePlace(@Param("parkId") int parkId);
}
