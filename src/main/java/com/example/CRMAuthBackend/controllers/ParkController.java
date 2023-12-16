package com.example.CRMAuthBackend.controllers;

import com.example.CRMAuthBackend.dto.entities.Park;
import com.example.CRMAuthBackend.dto.parks.ParksDto;
import com.example.CRMAuthBackend.dto.parks.PlaceDto;
import com.example.CRMAuthBackend.services.ParkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public CompletableFuture<ResponseEntity<List<ParksDto>>> getAllParks(@RequestParam("userLat") double lat,
                                                                         @RequestParam("userLon") double lon,
                                                                         @RequestParam(value = "isPrice", required = false,
                                                                                 defaultValue = "false") boolean isPrice,
                                                                         @RequestParam(value = "isBusy", required = false,
                                                                                defaultValue = "false") boolean isBusy) {
        return parkService.getAllParks(lat, lon, isPrice, isBusy)
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
        return parkService.getBusyPlace(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Park>> smartSearch(@RequestParam String searchTerm) {
        List<Park> parks = parkService.smartSearch(searchTerm.toLowerCase());
        return new ResponseEntity<>(parks, HttpStatus.OK);
    }
}
