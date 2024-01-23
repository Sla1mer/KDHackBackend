package com.example.CRMAuthBackend.dto.parkingPlace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusParkingPlace {
    private String name;
    private int parkId;
    private boolean isFree;
}
