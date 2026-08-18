package com.lawtan.service.impl;

import com.lawtan.dto.ReproductionAlertDTO;
import com.lawtan.dto.ReproductionEventDTO;
import com.lawtan.entity.Animal;
import com.lawtan.entity.ReproductionEvent;
import com.lawtan.model.AnimalCategory;
import com.lawtan.model.AnimalStatus;
import com.lawtan.model.ReproEventType;
import com.lawtan.repository.AnimalRepository;
import com.lawtan.repository.ReproductionEventRepository;
import com.lawtan.service.ReproductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReproductionServiceImpl implements ReproductionService {

    private final ReproductionEventRepository reproductionEventRepository;
    private final AnimalRepository animalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReproductionEventDTO> getAllEvents() {
        return reproductionEventRepository.findAllByOrderByEventDateDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReproductionEventDTO> getEventsByAnimal(Long animalId) {
        return reproductionEventRepository.findByAnimalIdOrderByEventDateDesc(animalId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReproductionEventDTO recordEvent(ReproductionEventDTO dto) {
        Animal animal;
        if (dto.getAnimalId() != null) {
            animal = animalRepository.findById(dto.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("Animal non trouvé avec ID: " + dto.getAnimalId()));
        } else if (dto.getAnimalInternalId() != null) {
            animal = animalRepository.findByInternalId(dto.getAnimalInternalId())
                    .orElseThrow(() -> new RuntimeException("Animal non trouvé avec matricule: " + dto.getAnimalInternalId()));
        } else {
            throw new IllegalArgumentException("Identifiant animal requis.");
        }

        LocalDate eventDate = dto.getEventDate() != null ? dto.getEventDate() : LocalDate.now();
        LocalDate expectedCalving = dto.getExpectedCalvingDate();
        LocalDate expectedDryOff = dto.getExpectedDryOffDate();

        // Calculs automatiques selon le type d'acte (Standards CowMaster)
        if (dto.getEventType() == ReproEventType.ARTIFICIAL_INSEMINATION || dto.getEventType() == ReproEventType.NATURAL_MATING) {
            if (expectedCalving == null) {
                expectedCalving = eventDate.plusDays(282); // Gestation moyenne bovine
            }
            if (expectedDryOff == null) {
                expectedDryOff = expectedCalving.minusDays(60); // Tarissement 60j avant vêlage
            }
            animal.setReproStatus("Inséminée le " + eventDate + " (" + (dto.getBullOrSemenUsed() != null ? dto.getBullOrSemenUsed() : "Semence") + ")");
        } else if (dto.getEventType() == ReproEventType.PREGNANCY_DIAGNOSIS) {
            boolean isPositive = dto.getIsConfirmed() != null && dto.getIsConfirmed();
            if (isPositive) {
                animal.setStatus(AnimalStatus.PREGNANT);
                animal.setReproStatus("Gestation confirmée — Vêlage prévu: " + (expectedCalving != null ? expectedCalving : "en cours"));
            } else {
                animal.setReproStatus("Diagnostic négatif (Non gestante)");
            }
        } else if (dto.getEventType() == ReproEventType.DRY_OFF) {
            animal.setReproStatus("Tarie — Repos pré-vêlage");
        } else if (dto.getEventType() == ReproEventType.CALVING) {
            animal.setStatus(AnimalStatus.EXCELLENT);
            animal.setCategory(AnimalCategory.MILKING_COW);
            animal.setLactationNumber((animal.getLactationNumber() != null ? animal.getLactationNumber() : 0) + 1);
            animal.setDaysInMilk(0);
            animal.setReproStatus("Vêlage réussi le " + eventDate + " — Nouvelle lactation (" + animal.getLactationNumber() + "e)");
        } else if (dto.getEventType() == ReproEventType.HEAT_DETECTION) {
            animal.setStatus(AnimalStatus.IN_HEAT);
            animal.setReproStatus("Chaleurs observées le " + eventDate + " — IA recommandée sous 12-18h");
        }

        animalRepository.save(animal);

        ReproductionEvent event = ReproductionEvent.builder()
                .animal(animal)
                .eventType(dto.getEventType())
                .eventDate(eventDate)
                .bullOrSemenUsed(dto.getBullOrSemenUsed())
                .operatorName(dto.getOperatorName() != null ? dto.getOperatorName() : "Dr. Fall")
                .expectedDryOffDate(expectedDryOff)
                .expectedCalvingDate(expectedCalving)
                .observations(dto.getObservations())
                .isConfirmed(dto.getIsConfirmed() != null ? dto.getIsConfirmed() : true)
                .build();

        ReproductionEvent saved = reproductionEventRepository.save(event);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ReproductionEventDTO updateEvent(Long id, ReproductionEventDTO dto) {
        ReproductionEvent event = reproductionEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement reproduction non trouvé: " + id));

        if (dto.getEventType() != null) event.setEventType(dto.getEventType());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getBullOrSemenUsed() != null) event.setBullOrSemenUsed(dto.getBullOrSemenUsed());
        if (dto.getOperatorName() != null) event.setOperatorName(dto.getOperatorName());
        if (dto.getExpectedDryOffDate() != null) event.setExpectedDryOffDate(dto.getExpectedDryOffDate());
        if (dto.getExpectedCalvingDate() != null) event.setExpectedCalvingDate(dto.getExpectedCalvingDate());
        if (dto.getObservations() != null) event.setObservations(dto.getObservations());
        if (dto.getIsConfirmed() != null) event.setIsConfirmed(dto.getIsConfirmed());

        ReproductionEvent updated = reproductionEventRepository.save(event);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        reproductionEventRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReproductionAlertDTO> getReproductionAlerts() {
        List<ReproductionAlertDTO> alerts = new ArrayList<>();
        LocalDate today = LocalDate.now();

        List<Animal> animals = animalRepository.findAll();

        for (Animal a : animals) {
            List<ReproductionEvent> animalEvents = reproductionEventRepository.findByAnimalIdOrderByEventDateDesc(a.getId());
            if (animalEvents.isEmpty()) continue;

            ReproductionEvent latestEvent = animalEvents.get(0);
            boolean hasRecentCalving = animalEvents.stream()
                    .anyMatch(e -> e.getEventType() == ReproEventType.CALVING && e.getEventDate() != null && !e.getEventDate().isBefore(today.minusDays(60)));
            boolean hasRecentDryOff = animalEvents.stream()
                    .anyMatch(e -> e.getEventType() == ReproEventType.DRY_OFF && e.getEventDate() != null && !e.getEventDate().isBefore(today.minusDays(30)));
            boolean hasRecentIA = animalEvents.stream()
                    .anyMatch(e -> (e.getEventType() == ReproEventType.ARTIFICIAL_INSEMINATION || e.getEventType() == ReproEventType.NATURAL_MATING) 
                            && e.getEventDate() != null && !e.getEventDate().isBefore(today.minusDays(7)));

            for (ReproductionEvent e : animalEvents) {
                // 1. Alerte Vêlage Imminent (uniquement si l'animal n'a pas encore vêlé et est gestante)
                if (e.getExpectedCalvingDate() != null && !hasRecentCalving && a.getStatus() == AnimalStatus.PREGNANT) {
                    long daysToCalving = ChronoUnit.DAYS.between(today, e.getExpectedCalvingDate());
                    if (daysToCalving >= -5 && daysToCalving <= 30) {
                        String severity = daysToCalving <= 7 ? "DANGER" : (daysToCalving <= 15 ? "WARNING" : "INFO");
                        alerts.add(ReproductionAlertDTO.builder()
                                .alertType("CALVING_IMMINENT")
                                .animalInternalId(a.getInternalId())
                                .animalName(a.getName())
                                .targetDate(e.getExpectedCalvingDate())
                                .daysRemaining(daysToCalving)
                                .message("Vêlage prévu pour " + a.getName() + " (" + a.getInternalId() + ") dans " + daysToCalving + " jours (" + e.getExpectedCalvingDate() + ")")
                                .severity(severity)
                                .build());
                        break; // Une seule alerte de vêlage par vache
                    }
                }

                // 2. Alerte Tarissement à effectuer (uniquement si pas encore tarie)
                if (e.getExpectedDryOffDate() != null && !hasRecentDryOff && !hasRecentCalving) {
                    long daysToDryOff = ChronoUnit.DAYS.between(today, e.getExpectedDryOffDate());
                    if (daysToDryOff >= -5 && daysToDryOff <= 20) {
                        String severity = daysToDryOff <= 5 ? "WARNING" : "INFO";
                        alerts.add(ReproductionAlertDTO.builder()
                                .alertType("DRY_OFF_DUE")
                                .animalInternalId(a.getInternalId())
                                .animalName(a.getName())
                                .targetDate(e.getExpectedDryOffDate())
                                .daysRemaining(daysToDryOff)
                                .message("Tarissement à programmer pour " + a.getName() + " (" + a.getInternalId() + ") - J-" + daysToDryOff)
                                .severity(severity)
                                .build());
                        break; // Une seule alerte de tarissement par vache
                    }
                }
            }

            // 3. Alerte Chaleurs pour animaux IN_HEAT (si pas d'IA récente)
            if (a.getStatus() == AnimalStatus.IN_HEAT && !hasRecentIA) {
                alerts.add(ReproductionAlertDTO.builder()
                        .alertType("HEAT_ACTIVE")
                        .animalInternalId(a.getInternalId())
                        .animalName(a.getName())
                        .targetDate(today)
                        .daysRemaining(0L)
                        .message("Chaleurs actives pour " + a.getName() + " (" + a.getInternalId() + ") — IA recommandée aujourd'hui")
                        .severity("DANGER")
                        .build());
            }
        }

        return alerts;
    }

    private ReproductionEventDTO mapToDTO(ReproductionEvent e) {
        return ReproductionEventDTO.builder()
                .id(e.getId())
                .animalId(e.getAnimal() != null ? e.getAnimal().getId() : null)
                .animalInternalId(e.getAnimal() != null ? e.getAnimal().getInternalId() : null)
                .animalName(e.getAnimal() != null ? e.getAnimal().getName() : null)
                .eventType(e.getEventType())
                .eventTypeLabel(e.getEventType() != null ? e.getEventType().getLabel() : null)
                .eventDate(e.getEventDate())
                .bullOrSemenUsed(e.getBullOrSemenUsed())
                .operatorName(e.getOperatorName())
                .expectedDryOffDate(e.getExpectedDryOffDate())
                .expectedCalvingDate(e.getExpectedCalvingDate())
                .observations(e.getObservations())
                .isConfirmed(e.getIsConfirmed())
                .build();
    }
}
