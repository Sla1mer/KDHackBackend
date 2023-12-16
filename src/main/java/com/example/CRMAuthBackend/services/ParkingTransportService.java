package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.ParkingTransport;
import com.example.CRMAuthBackend.dto.parks.PlaceDto;
import com.example.CRMAuthBackend.repositories.ParkingTransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

@Service
public class ParkingTransportService {

    @Autowired
    private ParkingTransportRepository parkingTransportRepository;

    @Async
    public void createParkingTransport(ParkingTransport parkingTransport) {
        parkingTransportRepository.save(parkingTransport);
    }

    @Async
    public void deleteParkingTransport(int id) {
        parkingTransportRepository.deleteById(id);
    }

    @Async
    public CompletableFuture<ParkingTransport> findByCarNumber(String carNumber) {
        return CompletableFuture.completedFuture(parkingTransportRepository.findByCarNumber(carNumber));
    }

    @Async
    public CompletableFuture<Integer> calculatingCost(int id, LocalDateTime timeExit) {
        ParkingTransport parkingTransport = parkingTransportRepository.findById(id).orElse(null);

        if (parkingTransport != null) {
            return CompletableFuture.completedFuture(calculateHoursDifference(parkingTransport.getTimeEntry(), timeExit) * parkingTransport.getTariff().getCost());
        }

        return CompletableFuture.completedFuture(0);
    }

    @Async
    public CompletableFuture<PlaceDto> getBusyPlace(int id) {
        int normalCount = parkingTransportRepository.countByParkIdAndIsInvalid(id, false);
        int invalidCount = parkingTransportRepository.countByParkIdAndIsInvalid(id, true);

        return CompletableFuture.completedFuture(new PlaceDto(invalidCount, normalCount));
    }


    private int calculateHoursDifference(LocalDateTime timeEntry, LocalDateTime timeExit) {
        long minutesDifference = ChronoUnit.MINUTES.between(timeEntry, timeExit);

        // Рассчитываем часы с округлением вверх
        int hoursDifference = (int) Math.ceil(minutesDifference / 60.0);

        return hoursDifference;
    }
}
