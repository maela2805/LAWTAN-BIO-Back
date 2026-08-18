package com.lawtan.entity;

import com.lawtan.model.AnimalCategory;
import com.lawtan.model.AnimalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String internalId; // e.g. FL-001

    @Column(nullable = false, length = 100)
    private String name; // e.g. NDIRA

    @Column(nullable = false, unique = true, length = 50)
    private String earTagNumber; // e.g. SN-DK-1423

    @Column(length = 50)
    private String rfidCode; // e.g. RFID-9820-001

    @Column(nullable = false, length = 100)
    private String breed; // e.g. Holstein Pure

    private LocalDate birthDate;

    @Column(length = 20)
    private String gender; // FEMALE / MALE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AnimalCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AnimalStatus status;

    private Double weight; // in kg

    private Double temperature; // in °C

    private Double dailyMilkYield; // in Liters/day

    private Integer lactationNumber; // e.g. 3

    private Integer daysInMilk; // DIM e.g. 154

    private Double totalLactationMilk; // in Liters

    @Column(length = 255)
    private String reproStatus; // e.g. "Gestation à confirmer (IA Mai 2026)"

    @Column(length = 10)
    private String avatarEmoji; // e.g. 🐄, 🐮, 🐂

    @Column(columnDefinition = "TEXT")
    private String imageUrl; // Photo Base64 / Cloudinary / URL

    @Column(length = 255)
    private String origin;

    @Column(length = 1000)
    private String notes;

    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Pedigree pedigree;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<HealthRecord> healthRecords = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReproductionEvent> reproductionEvents = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MilkProduction> milkProductions = new ArrayList<>();
}
