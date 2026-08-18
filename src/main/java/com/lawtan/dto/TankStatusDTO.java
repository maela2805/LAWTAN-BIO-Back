package com.lawtan.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TankStatusDTO {
    private String tankName;
    private Double currentVolume;
    private Double maxCapacity;
    private Double fillPercentage;
    private Double temperature;
    private Double phLevel;
    private String qualityStatus;
    private String targetBatch;
    private Double morningVolume;
    private Double eveningVolume;
    private LocalDate collectionDate;
}
