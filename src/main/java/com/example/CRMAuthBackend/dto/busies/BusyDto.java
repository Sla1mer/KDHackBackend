package com.example.CRMAuthBackend.dto.busies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusyDto {
    private String numberCar;
    private String phoneNumber;
    private int parkId;
}
