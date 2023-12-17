package com.example.CRMAuthBackend.controllers;

import com.example.CRMAuthBackend.dto.busies.BusyDto;
import com.example.CRMAuthBackend.dto.busies.CodeDto;
import com.example.CRMAuthBackend.dto.entities.ParkingTransport;
import com.example.CRMAuthBackend.dto.exceptions.IncorrectTokenException;
import com.example.CRMAuthBackend.services.ParkingTransportService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/parking")
public class ParkingTransportController {

    @Autowired
    private ParkingTransportService parkingTransportService;

    @PostMapping
    public ResponseEntity<Void> createParkingTransport(@RequestBody ParkingTransport parkingTransport) {
        parkingTransportService.createParkingTransport(parkingTransport);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingTransport(@PathVariable int id) {
        parkingTransportService.deleteParkingTransport(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{carNumber}")
    public CompletableFuture<ResponseEntity<ParkingTransport>> findByCarNumber(@PathVariable String carNumber) {
        return parkingTransportService.findByCarNumber(carNumber)
                .thenApply(parkingTransport -> {
                    if (parkingTransport != null) {
                        return ResponseEntity.ok(parkingTransport);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @PostMapping("/busyTransport")
    public CompletableFuture<ResponseEntity<String>> busyTransport(@RequestBody BusyDto busyDto) throws MessagingException, IOException {
        return parkingTransportService.sendCodeToPhone(busyDto.getPhoneNumber(), busyDto.getParkId(), busyDto.getNumberCar())
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/validateCode")
    public CompletableFuture<ResponseEntity<String>> busyTransport(HttpServletRequest request, @RequestBody CodeDto codeDto) throws MessagingException, IOException, IncorrectTokenException {
        return parkingTransportService.validate2FACode(request, codeDto.getCode())
                .thenApply(elem -> {
                    if (elem) {
                        return ResponseEntity.ok("Успешно забронировано");
                    } else {
                        return ResponseEntity.badRequest().body("Не правильный код");
                    }
                });
    }


    @GetMapping("/{id}/calculateCost")
    public CompletableFuture<ResponseEntity<Integer>> calculateCost(@PathVariable int id, @RequestParam LocalDateTime timeExit) {
        return parkingTransportService.calculatingCost(id, timeExit)
                .thenApply(ResponseEntity::ok);
    }
}
