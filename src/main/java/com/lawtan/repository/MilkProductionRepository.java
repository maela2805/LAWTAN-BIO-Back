package com.lawtan.repository;

import com.lawtan.entity.MilkProduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MilkProductionRepository extends JpaRepository<MilkProduction, Long> {

    List<MilkProduction> findByProductionDateOrderBySessionAsc(LocalDate date);

    List<MilkProduction> findByProductionDateBetweenOrderByProductionDateAsc(LocalDate startDate, LocalDate endDate);

    List<MilkProduction> findByAnimalIdOrderByProductionDateDesc(Long animalId);

    @Query("SELECT SUM(m.volumeLiters) FROM MilkProduction m WHERE m.productionDate = :date")
    Double sumVolumeByProductionDate(LocalDate date);
}
