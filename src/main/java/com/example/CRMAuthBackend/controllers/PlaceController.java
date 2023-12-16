package com.example.CRMAuthBackend.controllers;

import com.example.CRMAuthBackend.dto.entities.Place;
import com.example.CRMAuthBackend.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @PostMapping
    public CompletableFuture<ResponseEntity<Place>> createPlace(@RequestBody Place place) {
        return placeService.createPlace(place)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Place>> updatePlace(@RequestBody Place placeData, @PathVariable int id) {
        return placeService.updatePlace(placeData, id)
                .thenApply(place -> {
                    if (place != null) {
                        return ResponseEntity.ok(place);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }
}
