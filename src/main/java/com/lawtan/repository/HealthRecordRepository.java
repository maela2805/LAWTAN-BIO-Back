package com.lawtan.repository;

import com.lawtan.entity.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByAnimalIdOrderByRecordDateDesc(Long animalId);
    List<HealthRecord> findByAnimalInternalIdOrderByRecordDateDesc(String internalId);
    List<HealthRecord> findAllByOrderByRecordDateDesc();
}
