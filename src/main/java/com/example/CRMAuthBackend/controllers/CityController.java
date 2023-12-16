package com.example.CRMAuthBackend.controllers;

import com.example.CRMAuthBackend.dto.entities.City;
import com.example.CRMAuthBackend.services.CityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<City>>> listCities() {
        return cityService.listCities()
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<City>> createCity(@RequestBody City city) {
        return cityService.createCity(city)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<City>> updateCity(@RequestBody City cityData, @PathVariable int id) {
        return cityService.updateCity(cityData, id)
                .thenApply(city -> {
                    if (city != null) {
                        return ResponseEntity.ok(city);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<City>> deleteCity(@PathVariable int id) {
        return cityService.deleteCity(id)
                .thenApply(city -> {
                    if (city != null) {
                        return ResponseEntity.ok(city);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }
}
