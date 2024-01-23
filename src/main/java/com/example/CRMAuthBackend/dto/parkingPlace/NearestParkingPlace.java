package com.example.CRMAuthBackend.dto.parkingPlace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearestParkingPlace {
    private String name;
    private int coordinateX;
    private int coordinateY;
}
