package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.Park;
import com.example.CRMAuthBackend.dto.entities.Place;
import com.example.CRMAuthBackend.dto.parks.ParksDto;
import com.example.CRMAuthBackend.dto.parks.PlaceDto;
import com.example.CRMAuthBackend.repositories.ParkRepository;
import com.example.CRMAuthBackend.repositories.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ParkService {

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private TariffService tariffService;

    @Autowired
    private ParkingTransportService parkingTransportService;

    @Async
    public CompletableFuture<List<ParksDto>> getAllParks(double lat,
                                                     double lon, boolean isPrice, boolean isBusy) {

        List<ParksDto> result = new ArrayList<>();

        List<Park> nearestParks = parkRepository.findNearestParks(lat, lon, isPrice);
        if (isBusy) {
            nearestParks.sort(Comparator.comparingInt(park -> {
                try {
                    return getCountBusyPlace((Park) park);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 0;
            }).reversed());
        }

        nearestParks.forEach(
                elem -> {
                    ParksDto parksDto = new ParksDto();
                    try {
                        parksDto.setPlaceBusy(getBusyPlace(elem.getId()).get());
                        parksDto.setPlace(elem.getPlace());
                        parksDto.setCity(elem.getCity());
                        parksDto.setLat(elem.getLat());
                        parksDto.setLon(elem.getLon());
                        parksDto.setAddress(elem.getAddress());
                        parksDto.setId(elem.getId());
                        parksDto.setName(elem.getName());
                        parksDto.setOwner(elem.getOwner());
                        parksDto.setDistation((int) Math.round(parkRepository.calculateDistance(lat, lon, elem.getId())));
                        parksDto.setPrice(tariffService.findMinPriceByPark(elem.getId()).get());

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

    public Integer getCountBusyPlace(Park park) throws ExecutionException, InterruptedException {
        PlaceDto busyPlace = parkingTransportService.getBusyPlace(park.getId()).get();

        return busyPlace.getCountNormal() + busyPlace.getCountInvalid();
    }

    @Async
    public CompletableFuture<PlaceDto> getBusyPlace(int id) throws ExecutionException, InterruptedException {
        PlaceDto busyPlace = parkingTransportService.getBusyPlace(id).get();

        return CompletableFuture.completedFuture(busyPlace);
    }

    public List<Park> smartSearch(String searchTerm) {
        System.out.println(searchTerm);
        return parkRepository.smartSearch(searchTerm);
    }
}
