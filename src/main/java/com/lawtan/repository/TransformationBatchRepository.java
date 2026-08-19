package com.lawtan.repository;

import com.lawtan.entity.TransformationBatch;
import com.lawtan.model.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransformationBatchRepository extends JpaRepository<TransformationBatch, Long> {
    Optional<TransformationBatch> findByBatchNumber(String batchNumber);
    List<TransformationBatch> findByStatus(BatchStatus status);
    List<TransformationBatch> findAllByOrderByProductionDateDesc();

    @Query("SELECT SUM(b.milkLitersConsumed) FROM TransformationBatch b WHERE b.status = 'COMPLETED'")
    Double sumTotalMilkTransformed();

    @Query("SELECT AVG(b.yieldEfficiencyPercentage) FROM TransformationBatch b WHERE b.status = 'COMPLETED' AND b.yieldEfficiencyPercentage IS NOT NULL")
    Double avgYieldEfficiency();

    Long countByStatus(BatchStatus status);
}
