package com.example.CRMAuthBackend.dto.parks;


import com.example.CRMAuthBackend.dto.entities.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {
    private int countInvalid;
    private int countNormal;

    public static PlaceDto transformToPlaceDtoFromPlace(Place place) {
        return new PlaceDto(place.getCountInvalid(), place.getCountNormal());
    }

    public PlaceDto subtract(PlaceDto other) {
        int resultCountInvalid = this.countInvalid - other.countInvalid;
        int resultCountNormal = this.countNormal - other.countNormal;

        return new PlaceDto(resultCountInvalid, resultCountNormal);
    }
}
