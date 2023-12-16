package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkRepository extends JpaRepository<Park, Integer> {

    @Query(value = "SELECT p FROM Park p " +
            "ORDER BY FUNCTION('ST_DISTANCE', " +
            "FUNCTION('POINT', :userLon, :userLat), " +
            "FUNCTION('POINT', p.lon, p.lat)) ASC")
    List<Park> findNearestParks(@Param("userLat") double userLat, @Param("userLon") double userLon);

    @Query(value = "SELECT p.* FROM parks p " +
            "JOIN users u ON p.owner_id = u.id " +
            "JOIN organizations o ON u.organization_id = o.id " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', LOWER(:searchTerm), '%')) " +
            "OR LOWER(o.name) LIKE LOWER(CONCAT('%', LOWER(:searchTerm), '%'))" +
            "OR LOWER(p.address) LIKE LOWER(CONCAT('%', LOWER(:searchTerm), '%'))", nativeQuery = true)
    List<Park> smartSearch(@Param("searchTerm") String searchTerm);



}
