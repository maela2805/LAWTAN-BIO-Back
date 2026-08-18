package com.lawtan.service;

import com.lawtan.dto.MilkHistoryDTO;
import com.lawtan.dto.MilkProductionDTO;
import com.lawtan.dto.TankStatusDTO;

import java.time.LocalDate;
import java.util.List;

public interface MilkProductionService {
    List<MilkProductionDTO> getProductionsByDate(LocalDate date);
    List<MilkProductionDTO> getProductionsByAnimal(Long animalId);
    MilkProductionDTO recordMilkProduction(MilkProductionDTO dto);
    TankStatusDTO getTankStatus();
    List<MilkHistoryDTO> getMilkHistory(int days);
}
