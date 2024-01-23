package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.ParkingPlace;
import com.example.CRMAuthBackend.dto.exceptions.NotFindNameParkingPlace;
import com.example.CRMAuthBackend.dto.parkingPlace.ChangeStatusParkingPlace;
import com.example.CRMAuthBackend.dto.parkingPlace.NearestParkingPlace;
import com.example.CRMAuthBackend.repositories.ParkingPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ParkingPlaceService {

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Async
    public void changeStatus(ChangeStatusParkingPlace parkingPlace) throws NotFindNameParkingPlace {
        ParkingPlace parkingPlaceFinded = parkingPlaceRepository.findByParkingPlaceNameAndParkId(parkingPlace.getName(), parkingPlace.getParkId());

        if (parkingPlaceFinded == null) throw new NotFindNameParkingPlace("Не правильное имя парковочного места или ID парковки");

        parkingPlaceFinded.setFree(parkingPlace.isFree());
        parkingPlaceRepository.save(parkingPlaceFinded);
    }

    @Async
    public CompletableFuture<NearestParkingPlace> getNearestParkingPlace(int parkId) throws NotFindNameParkingPlace {

        ParkingPlace parkingPlace = parkingPlaceRepository.findNearestFreePlace(parkId);

        if (parkingPlace == null) throw new NotFindNameParkingPlace("Не правильный ID парковки");

        NearestParkingPlace nearestParkingPlace = new NearestParkingPlace(parkingPlace.getParkPlaceName(),
                parkingPlace.getCoordinateX(), parkingPlace.getCoordinateY());

        return CompletableFuture.completedFuture(nearestParkingPlace);
    }
}
