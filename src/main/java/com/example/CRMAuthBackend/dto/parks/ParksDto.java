package com.example.CRMAuthBackend.dto.parks;

import com.example.CRMAuthBackend.dto.entities.Park;
import lombok.Data;

@Data
public class ParksDto extends Park {
    private PlaceDto placeBusy;


}
