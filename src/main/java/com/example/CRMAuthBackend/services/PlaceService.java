package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.Place;
import com.example.CRMAuthBackend.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Async
    public CompletableFuture<Place> updatePlace(Place placeData, int id) {
        Place place = placeRepository.findById(id).orElse(null);

        if (place != null) {
            place.setCountNormal(placeData.getCountNormal());
            place.setCountInvalid(placeData.getCountInvalid());

            return CompletableFuture.completedFuture(place);
        }

        return null;
    }

    @Async
    public CompletableFuture<Place> createPlace(Place place) {
        return CompletableFuture.completedFuture(placeRepository.save(place));
    }
}
