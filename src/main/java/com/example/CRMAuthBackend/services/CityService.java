package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.City;
import com.example.CRMAuthBackend.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Async
    public CompletableFuture<List<City>> listCities() {
        return CompletableFuture.completedFuture(cityRepository.findAll());
    }

    @Async
    public CompletableFuture<City> createCity(City city) {
        return CompletableFuture.completedFuture(cityRepository.save(city));
    }

    @Async
    public CompletableFuture<City> updateCity(City cityData, int id) {
        City city=cityRepository.findById(id).orElse(null);

        if (city != null) {
            city.setName(cityData.getName());
            city.setRegion(cityData.getRegion());

            return CompletableFuture.completedFuture(city);
        }

        return null;
    }

    @Async
    public CompletableFuture<City> deleteCity(int id) {
        City city=cityRepository.findById(id).orElse(null);

        if (city != null) {
            cityRepository.deleteById(id);
            return  CompletableFuture.completedFuture(city);
        }

        return null;
    }
}
