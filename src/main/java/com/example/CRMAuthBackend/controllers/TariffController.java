package com.example.CRMAuthBackend.controllers;


import com.example.CRMAuthBackend.dto.entities.Tariff;
import com.example.CRMAuthBackend.services.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    @Autowired
    private TariffService tariffService;

    @GetMapping("/{type}")
    public CompletableFuture<ResponseEntity<Tariff>> findByType(@PathVariable String type) {
        return tariffService.findByType(type)
                .thenApply(tariff -> {
                    if (tariff != null) {
                        return ResponseEntity.ok(tariff);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Tariff>> createTariff(@RequestBody Tariff tariff) {
        return tariffService.createTariff(tariff)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Tariff>> updateTariff(@RequestBody Tariff tariffData, @PathVariable int id) {
        return tariffService.updateTariff(tariffData, id)
                .thenApply(tariff -> {
                    if (tariff != null) {
                        return ResponseEntity.ok(tariff);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Tariff>> deleteTariff(@PathVariable int id) {
        return tariffService.deleteTariff(id)
                .thenApply(tariff -> {
                    if (tariff != null) {
                        return ResponseEntity.ok(tariff);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping("/park/{parkId}")
    public CompletableFuture<ResponseEntity<List<Tariff>>> findAllTariffByPark(@PathVariable int parkId) {
        return tariffService.findAllTariffByPark(parkId)
                .thenApply(ResponseEntity::ok);
    }
}
