package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.ParkingPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParkingPlaceRepository extends JpaRepository<ParkingPlace, Integer> {

    ParkingPlace findByParkingPlaceNameAndParkId(String ParkingPlaceName, int parkId);

    @Query("SELECT p FROM parking_places p " +
            "WHERE p.is_free = true AND p.park_id = :parkId" +
            "ORDER BY SQRT((p.coordinate_x) * (p.coordinate_x) + " +
            "(p.coordinate_y) * (p.coordinate_y)) " +
            "LIMIT 1")
    ParkingPlace findNearestFreePlace(@Param("parkId") int parkId);
}