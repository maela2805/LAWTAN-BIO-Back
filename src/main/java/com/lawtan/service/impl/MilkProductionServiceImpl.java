package com.lawtan.service.impl;

import com.lawtan.dto.MilkProductionDTO;
import com.lawtan.entity.Animal;
import com.lawtan.entity.MilkProduction;
import com.lawtan.model.MilkSession;
import com.lawtan.repository.AnimalRepository;
import com.lawtan.repository.MilkProductionRepository;
import com.lawtan.service.MilkProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MilkProductionServiceImpl implements MilkProductionService {

    private final MilkProductionRepository milkProductionRepository;
    private final AnimalRepository animalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MilkProductionDTO> getProductionsByDate(LocalDate date) {
        return milkProductionRepository.findByProductionDateOrderBySessionAsc(date).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MilkProductionDTO> getProductionsByAnimal(Long animalId) {
        return milkProductionRepository.findByAnimalIdOrderByProductionDateDesc(animalId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MilkProductionDTO recordMilkProduction(MilkProductionDTO dto) {
        Animal animal = animalRepository.findByInternalId(dto.getAnimalInternalId())
                .orElseThrow(() -> new RuntimeException("Animal non trouvé: " + dto.getAnimalInternalId()));

        MilkProduction prod = MilkProduction.builder()
                .animal(animal)
                .productionDate(dto.getProductionDate() != null ? dto.getProductionDate() : LocalDate.now())
                .session(dto.getSession() != null ? dto.getSession() : MilkSession.MORNING)
                .volumeLiters(dto.getVolumeLiters())
                .milkTemperature(dto.getMilkTemperature() != null ? dto.getMilkTemperature() : 34.0)
                .fatPercentage(dto.getFatPercentage() != null ? dto.getFatPercentage() : 4.0)
                .destinationTank(dto.getDestinationTank() != null ? dto.getDestinationTank() : "Cuve Réfrigérée N°1 (Bio)")
                .isOrganicCompliant(dto.getIsOrganicCompliant() != null ? dto.getIsOrganicCompliant() : true)
                .build();

        // Update animal daily milk yield and total lactation
        if (animal.getTotalLactationMilk() == null) animal.setTotalLactationMilk(0.0);
        animal.setTotalLactationMilk(animal.getTotalLactationMilk() + dto.getVolumeLiters());
        animal.setDailyMilkYield(dto.getVolumeLiters());
        animalRepository.save(animal);

        MilkProduction saved = milkProductionRepository.save(prod);
        return mapToDTO(saved);
    }

    private MilkProductionDTO mapToDTO(MilkProduction m) {
        return MilkProductionDTO.builder()
                .id(m.getId())
                .animalId(m.getAnimal() != null ? m.getAnimal().getId() : null)
                .animalInternalId(m.getAnimal() != null ? m.getAnimal().getInternalId() : null)
                .animalName(m.getAnimal() != null ? m.getAnimal().getName() : null)
                .animalBreed(m.getAnimal() != null ? m.getAnimal().getBreed() : null)
                .productionDate(m.getProductionDate())
                .session(m.getSession())
                .sessionLabel(m.getSession() != null ? m.getSession().getLabel() : null)
                .volumeLiters(m.getVolumeLiters())
                .milkTemperature(m.getMilkTemperature())
                .fatPercentage(m.getFatPercentage())
                .destinationTank(m.getDestinationTank())
                .isOrganicCompliant(m.getIsOrganicCompliant())
                .build();
    }
}
