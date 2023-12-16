package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkRepository extends JpaRepository<Park, Integer> {


    @Query(value = "SELECT p FROM Park p " +
            "LEFT JOIN Tariff t ON p.id = t.park.id AND t.cost != 0 " +
            "ORDER BY " +
            "CASE WHEN :isPrice = true THEN t.cost END ASC NULLS LAST, " +
            "ST_DISTANCE(POINT(:userLon, :userLat), POINT(p.lon, p.lat)) ASC")
    List<Park> findNearestParks(@Param("userLat") double userLat, @Param("userLon") double userLon,
                                @Param("isPrice") boolean isPrice);

    @Query(value = "SELECT ST_DISTANCE_SPHERE(POINT(:userLon, :userLat), POINT(p.lon, p.lat)) " +
            "FROM parks p " +
            "WHERE p.id = :parkId", nativeQuery = true)
    Double calculateDistance(@Param("userLat") double userLat,
                             @Param("userLon") double userLon,
                             @Param("parkId") int parkId);

    @Query(value = "SELECT p.* FROM parks p " +
            "JOIN users u ON p.owner_id = u.id " +
            "JOIN organizations o ON u.organization_id = o.id " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', LOWER(:searchTerm), '%')) " +
            "OR LOWER(o.name) LIKE LOWER(CONCAT('%', LOWER(:searchTerm), '%'))" +
            "OR LOWER(p.address) LIKE LOWER(CONCAT('%', LOWER(:searchTerm), '%'))", nativeQuery = true)
    List<Park> smartSearch(@Param("searchTerm") String searchTerm);



}
