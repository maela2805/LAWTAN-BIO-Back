package com.lawtan.service.impl;

import com.lawtan.dto.SupplierDTO;
import com.lawtan.entity.Supplier;
import com.lawtan.repository.SupplierRepository;
import com.lawtan.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'ID: " + id));
        return mapToDTO(supplier);
    }

    @Override
    @Transactional
    public SupplierDTO createSupplier(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        mapToEntity(dto, supplier);
        supplier.setActive(true);
        supplier.setCreatedAt(LocalDateTime.now());
        if (supplier.getTotalOrdersCount() == null) supplier.setTotalOrdersCount(0);
        if (supplier.getTotalSpentFcfa() == null) supplier.setTotalSpentFcfa(0.0);
        if (supplier.getBioCertified() == null) supplier.setBioCertified(true);

        Supplier saved = supplierRepository.save(supplier);
        log.info("Nouveau fournisseur créé avec succès: ID={}, Nom={}", saved.getId(), saved.getName());
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public SupplierDTO updateSupplier(Long id, SupplierDTO dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'ID: " + id));
        mapToEntity(dto, supplier);
        Supplier updated = supplierRepository.save(supplier);
        log.info("Fournisseur mis à jour: ID={}, Nom={}", updated.getId(), updated.getName());
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'ID: " + id));
        supplier.setActive(false);
        supplierRepository.save(supplier);
        log.info("Fournisseur désactivé: ID={}", id);
    }

    @Override
    public List<SupplierDTO> searchSuppliers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllSuppliers();
        }
        return supplierRepository.findByNameContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrContactPersonContainingIgnoreCase(
                        query, query, query)
                .stream()
                .filter(s -> Boolean.TRUE.equals(s.getActive()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SupplierDTO mapToDTO(Supplier s) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setCompanyName(s.getCompanyName());
        dto.setContactPerson(s.getContactPerson());
        dto.setPhone(s.getPhone());
        dto.setEmail(s.getEmail());
        dto.setAddress(s.getAddress());
        dto.setCity(s.getCity());
        dto.setCategory(s.getCategory());
        dto.setNineaNumber(s.getNineaNumber());
        dto.setPaymentTerms(s.getPaymentTerms());
        dto.setTotalOrdersCount(s.getTotalOrdersCount() != null ? s.getTotalOrdersCount() : 0);
        dto.setTotalSpentFcfa(s.getTotalSpentFcfa() != null ? s.getTotalSpentFcfa() : 0.0);
        dto.setBioCertified(s.getBioCertified() != null ? s.getBioCertified() : true);
        dto.setActive(s.getActive() != null ? s.getActive() : true);
        dto.setNotes(s.getNotes());
        dto.setCreatedAt(s.getCreatedAt());
        return dto;
    }

    private void mapToEntity(SupplierDTO dto, Supplier entity) {
        entity.setName(dto.getName());
        entity.setCompanyName(dto.getCompanyName());
        entity.setContactPerson(dto.getContactPerson());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setCategory(dto.getCategory() != null ? dto.getCategory() : "FOURRAGE_ALIMENT");
        entity.setNineaNumber(dto.getNineaNumber());
        entity.setPaymentTerms(dto.getPaymentTerms());
        if (dto.getTotalOrdersCount() != null) entity.setTotalOrdersCount(dto.getTotalOrdersCount());
        if (dto.getTotalSpentFcfa() != null) entity.setTotalSpentFcfa(dto.getTotalSpentFcfa());
        if (dto.getBioCertified() != null) entity.setBioCertified(dto.getBioCertified());
        entity.setNotes(dto.getNotes());
    }
}
