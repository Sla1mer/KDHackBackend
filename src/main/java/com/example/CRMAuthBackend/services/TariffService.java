package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.entities.Tariff;
import com.example.CRMAuthBackend.repositories.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Async
    public CompletableFuture<Tariff> findByType(String type) {
        return CompletableFuture.completedFuture(tariffRepository.findByType(type));
    }

    @Async
    public CompletableFuture<Tariff> createTariff(Tariff tariff) {
        return CompletableFuture.completedFuture(tariffRepository.save(tariff));
    }

    @Async
    public CompletableFuture<Tariff> updateTariff(Tariff tariffData, int id) {
        Tariff tariff = tariffRepository.findById(id).orElse(null);

        if (tariff != null) {
            tariff.setCost(tariffData.getCost());
            tariff.setPark(tariffData.getPark());
            tariff.setType(tariffData.getType());

            return CompletableFuture.completedFuture(tariff);
        }

        return null;
    }

    @Async
    public CompletableFuture<Tariff> deleteTariff(int id) {
        Tariff tariff = tariffRepository.findById(id).orElse(null);

        if (tariff != null) {
            tariffRepository.deleteById(id);
            return CompletableFuture.completedFuture(tariff);
        }

        return null;
    }

    @Async
    public CompletableFuture<List<Tariff>> findAllTariffByPark(int parkId) {
        return CompletableFuture.completedFuture(tariffRepository.findByParkId(parkId));
    }

}
