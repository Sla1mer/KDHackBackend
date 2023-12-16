package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.Park;
import com.example.CRMAuthBackend.dto.entities.Place;
import com.example.CRMAuthBackend.dto.parks.PlaceDto;
import com.example.CRMAuthBackend.repositories.ParkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ParkService {

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private ParkingTransportService parkingTransportService;

    @Async
    public CompletableFuture<List<Park>> getAllParks() {
        return CompletableFuture.completedFuture(parkRepository.findAll());
    }

    @Async
    public CompletableFuture<Park> createPark(Park park) {
        return CompletableFuture.completedFuture(parkRepository.save(park));
    }

    @Async
    public CompletableFuture<Park> updatePark(Park parkData, int id) {
        Park park = parkRepository.findById(id).orElse(null);

        if (park != null) {
            park.setCity(parkData.getCity());
            park.setLat(parkData.getLat());
            park.setLon(parkData.getLon());
            park.setPlace(park.getPlace());

            return CompletableFuture.completedFuture(park);
        }

        return null;
    }

    @Async
    public CompletableFuture<Park> deletePark(int id) {
        Park park = parkRepository.findById(id).orElse(null);

        if (park != null) {
            parkRepository.deleteById(id);
            return CompletableFuture.completedFuture(park);
        }
        return null;
    }

    @Async
    public CompletableFuture<PlaceDto> getFreePlace(int id) throws ExecutionException, InterruptedException {
        Park park = parkRepository.findById(id).orElse(null);

        PlaceDto allPlace = PlaceDto.transformToPlaceDtoFromPlace(park.getPlace());
        PlaceDto busyPlace = parkingTransportService.getBusyPlace(id).get();


        return CompletableFuture.completedFuture(allPlace.subtract(busyPlace));
    }
}
