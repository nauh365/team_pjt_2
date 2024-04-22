package com.example.safe_ride.locationInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentLocationDto {
    private Double lat;
    private Double lng;

    public String toQueryValue() {
        return String.format("%f,%f", lng, lat);
    }
}
