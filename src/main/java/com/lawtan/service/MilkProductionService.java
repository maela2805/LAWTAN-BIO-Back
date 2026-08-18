package com.lawtan.service;

import com.lawtan.dto.MilkProductionDTO;

import java.time.LocalDate;
import java.util.List;

public interface MilkProductionService {
    List<MilkProductionDTO> getProductionsByDate(LocalDate date);
    List<MilkProductionDTO> getProductionsByAnimal(Long animalId);
    MilkProductionDTO recordMilkProduction(MilkProductionDTO dto);
}
