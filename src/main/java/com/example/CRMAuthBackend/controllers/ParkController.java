package com.example.CRMAuthBackend.controllers;

import com.example.CRMAuthBackend.dto.entities.Park;
import com.example.CRMAuthBackend.dto.parks.PlaceDto;
import com.example.CRMAuthBackend.services.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/parks")
public class ParkController {

    @Autowired
    private ParkService parkService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Park>>> getAllParks() {
        return parkService.getAllParks()
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Park>> createPark(@RequestBody Park park) {
        return parkService.createPark(park)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Park>> updatePark(@RequestBody Park parkData, @PathVariable int id) {
        return parkService.updatePark(parkData, id)
                .thenApply(park -> {
                    if (park != null) {
                        return ResponseEntity.ok(park);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Park>> deletePark(@PathVariable int id) {
        return parkService.deletePark(id)
                .thenApply(park -> {
                    if (park != null) {
                        return ResponseEntity.ok(park);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping("/{id}/freePlace")
    public CompletableFuture<ResponseEntity<PlaceDto>> getFreePlace(@PathVariable int id)
            throws ExecutionException, InterruptedException {
        return parkService.getFreePlace(id)
                .thenApply(ResponseEntity::ok);
    }
}
