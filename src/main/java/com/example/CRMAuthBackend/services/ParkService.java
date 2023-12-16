package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.Park;
import com.example.CRMAuthBackend.dto.entities.Place;
import com.example.CRMAuthBackend.dto.parks.ParksDto;
import com.example.CRMAuthBackend.dto.parks.PlaceDto;
import com.example.CRMAuthBackend.repositories.ParkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
    public CompletableFuture<List<ParksDto>> getAllParks(double lat,
                                                     double lon) {

        List<ParksDto> result = new ArrayList<>();
        parkRepository.findNearestParks(lat, lon).forEach(
                elem -> {
                    ParksDto parksDto = new ParksDto();
                    try {
                        parksDto.setPlaceBusy(getFreePlace(elem.getId()).get());
                        parksDto.setPlace(elem.getPlace());
                        parksDto.setCity(elem.getCity());
                        parksDto.setLat(elem.getLat());
                        parksDto.setLon(elem.getLon());
                        parksDto.setAddress(elem.getAddress());
                        parksDto.setId(elem.getId());
                        parksDto.setName(elem.getName());
                        parksDto.setOwner(elem.getOwner());

                        result.add(parksDto);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );
        return CompletableFuture.completedFuture(result);
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

    public List<Park> smartSearch(String searchTerm) {
        System.out.println(searchTerm);
        return parkRepository.smartSearch(searchTerm);
    }
}
