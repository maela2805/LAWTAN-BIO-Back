package com.lawtan.service.impl;

import com.lawtan.dto.SolarEnergyMetricDTO;
import com.lawtan.entity.SolarEnergyMetric;
import com.lawtan.repository.SolarEnergyMetricRepository;
import com.lawtan.service.SolarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SolarServiceImpl implements SolarService {

    @Autowired
    private SolarEnergyMetricRepository solarRepository;

    @Override
    public SolarEnergyMetricDTO getLatestSolarTelemetry() {
        return solarRepository.findTopByOrderByTimestampDesc()
                .map(this::convertToDTO)
                .orElseGet(this::createDefaultSolarMetric);
    }

    @Override
    public SolarEnergyMetricDTO recordSolarMetric(SolarEnergyMetricDTO dto) {
        SolarEnergyMetric entity = new SolarEnergyMetric(
                dto.getCurrentSolarPowerKw(),
                dto.getBatterySocPercent(),
                dto.getDailySolarYieldKwh(),
                dto.getTotalSolarYieldMwh(),
                dto.getGridStatus(),
                dto.getColdRoomTempCelsius(),
                dto.getSecondColdRoomTempCelsius(),
                dto.getWaterPumpFlowM3h(),
                dto.getWaterTankLevelPercent(),
                dto.getCo2SavedKg()
        );
        SolarEnergyMetric saved = solarRepository.save(entity);
        return convertToDTO(saved);
    }

    private SolarEnergyMetricDTO createDefaultSolarMetric() {
        SolarEnergyMetric metric = new SolarEnergyMetric(
                38.4, 94.0, 215.0, 68.4, "SOLAR_OPTIMAL", 3.8, 4.1, 14.5, 92.0, 182.5
        );
        return convertToDTO(solarRepository.save(metric));
    }

    private SolarEnergyMetricDTO convertToDTO(SolarEnergyMetric entity) {
        SolarEnergyMetricDTO dto = new SolarEnergyMetricDTO();
        dto.setId(entity.getId());
        dto.setTimestamp(entity.getTimestamp());
        dto.setCurrentSolarPowerKw(entity.getCurrentSolarPowerKw());
        dto.setBatterySocPercent(entity.getBatterySocPercent());
        dto.setDailySolarYieldKwh(entity.getDailySolarYieldKwh());
        dto.setTotalSolarYieldMwh(entity.getTotalSolarYieldMwh());
        dto.setGridStatus(entity.getGridStatus());
        dto.setColdRoomTempCelsius(entity.getColdRoomTempCelsius());
        dto.setSecondColdRoomTempCelsius(entity.getSecondColdRoomTempCelsius());
        dto.setWaterPumpFlowM3h(entity.getWaterPumpFlowM3h());
        dto.setWaterTankLevelPercent(entity.getWaterTankLevelPercent());
        dto.setCo2SavedKg(entity.getCo2SavedKg());
        return dto;
    }
}
