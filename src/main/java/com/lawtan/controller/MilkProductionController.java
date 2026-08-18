package com.lawtan.controller;

import com.lawtan.dto.MilkProductionDTO;
import com.lawtan.service.MilkProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/milk")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MilkProductionController {

    private final MilkProductionService milkProductionService;

    @GetMapping("/by-date")
    public ResponseEntity<List<MilkProductionDTO>> getProductionsByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(milkProductionService.getProductionsByDate(targetDate));
    }

    @GetMapping("/by-animal/{animalId}")
    public ResponseEntity<List<MilkProductionDTO>> getProductionsByAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(milkProductionService.getProductionsByAnimal(animalId));
    }

    @PostMapping("/record")
    public ResponseEntity<MilkProductionDTO> recordMilkProduction(@RequestBody MilkProductionDTO dto) {
        return new ResponseEntity<>(milkProductionService.recordMilkProduction(dto), HttpStatus.CREATED);
    }
}
