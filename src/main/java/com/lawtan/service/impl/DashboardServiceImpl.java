package com.lawtan.service.impl;

import com.lawtan.dto.AnimalDTO;
import com.lawtan.dto.DashboardStatsDTO;
import com.lawtan.model.AnimalCategory;
import com.lawtan.model.AnimalStatus;
import com.lawtan.repository.AnimalRepository;
import com.lawtan.repository.HealthRecordRepository;
import com.lawtan.service.AnimalService;
import com.lawtan.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AnimalRepository animalRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final AnimalService animalService;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        List<AnimalDTO> allAnimals = animalService.getAllAnimals();
        List<AnimalDTO> milkingCows = animalService.getAnimalsByCategory(AnimalCategory.MILKING_COW);
        long femalesCount = allAnimals.stream()
                .filter(a -> a.getCategory() == AnimalCategory.MILKING_COW || a.getCategory() == AnimalCategory.HEIFER_YOUNG)
                .count();
        long bullsCount = allAnimals.stream()
                .filter(a -> a.getCategory() == AnimalCategory.MALE_BULL)
                .count();

        long healthAlerts = allAnimals.stream()
                .filter(a -> a.getStatus() == AnimalStatus.FEVER_TREATMENT)
                .count();

        double dailyTotal = milkingCows.stream()
                .mapToDouble(a -> a.getDailyMilkYield() != null ? a.getDailyMilkYield() : 0.0)
                .sum();

        double avgPerCow = milkingCows.isEmpty() ? 0.0 : Math.round((dailyTotal / milkingCows.size()) * 10.0) / 10.0;

        // Mock 7-day chart data
        List<Map<String, Object>> weeklyData = new ArrayList<>();
        String[] days = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        double[] morning = {65.0, 66.5, 68.0, 67.0, 69.5, 68.0, 68.0};
        double[] evening = {52.0, 54.0, 55.0, 56.0, 56.5, 55.0, 56.0};
        for (int i = 0; i < days.length; i++) {
            Map<String, Object> dayMap = new HashMap<>();
            dayMap.put("day", days[i]);
            dayMap.put("morning", morning[i]);
            dayMap.put("evening", evening[i]);
            dayMap.put("total", morning[i] + evening[i]);
            weeklyData.add(dayMap);
        }

        // Revenue distribution
        List<Map<String, Object>> revenueData = new ArrayList<>();
        revenueData.add(Map.of("name", "Lait Frais Bio Pasteurisé", "percent", 45, "color", "#16a34a"));
        revenueData.add(Map.of("name", "Lait Caillé Artisanal (Sow)", "percent", 30, "color", "#38bdf8"));
        revenueData.add(Map.of("name", "Fromage Fermier Bio", "percent", 15, "color", "#f59e0b"));
        revenueData.add(Map.of("name", "Yaourts Bio Brassés", "percent", 10, "color", "#9333ea"));

        return DashboardStatsDTO.builder()
                .dailyMilkTotal(dailyTotal > 0 ? dailyTotal : 124.0)
                .morningMilkTotal(68.0)
                .eveningMilkTotal(56.0)
                .milkingCowsCount(milkingCows.size())
                .totalFemalesCount((int) femalesCount)
                .totalBullsCount((int) bullsCount)
                .totalHerdCount(allAnimals.size())
                .averageMilkPerCow(avgPerCow > 0 ? avgPerCow : 17.7)
                .healthAlertsCount((int) (healthAlerts > 0 ? healthAlerts : 1))
                .monthlyRevenueFcfa(1240000.0)
                .feedConversionRatio(1.42)
                .solarAutonomyPercentage(94.0)
                .milkingCowsToday(milkingCows)
                .weeklyProductionChart(weeklyData)
                .revenueDistributionChart(revenueData)
                .build();
    }
}
