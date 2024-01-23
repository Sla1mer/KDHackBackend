package com.example.CRMAuthBackend.controllers;

import com.example.CRMAuthBackend.dto.exceptions.NotFindNameParkingPlace;
import com.example.CRMAuthBackend.dto.parkingPlace.ChangeStatusParkingPlace;
import com.example.CRMAuthBackend.dto.parkingPlace.NearestParkingPlace;
import com.example.CRMAuthBackend.services.ParkingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/parkingPlace")
public class ParkingPlaceController {

    @Autowired
    private ParkingPlaceService parkingPlaceService;

    @PatchMapping
    private ResponseEntity<String> changeStatus(@RequestBody ChangeStatusParkingPlace parkingPlace) throws NotFindNameParkingPlace {
        parkingPlaceService.changeStatus(parkingPlace);
        return ResponseEntity.ok("Статус изменен");
    }

    @GetMapping
    private NearestParkingPlace getNearestParkingPlace(int parkId) throws NotFindNameParkingPlace, ExecutionException, InterruptedException {
        return parkingPlaceService.getNearestParkingPlace(parkId).get();
    }
}
