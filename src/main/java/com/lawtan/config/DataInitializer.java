package com.lawtan.config;

import com.lawtan.entity.Animal;
import com.lawtan.entity.HealthRecord;
import com.lawtan.entity.MilkProduction;
import com.lawtan.entity.Pedigree;
import com.lawtan.entity.ReproductionEvent;
import com.lawtan.entity.VaccineSchedule;
import com.lawtan.model.AnimalCategory;
import com.lawtan.model.AnimalStatus;
import com.lawtan.model.MilkSession;
import com.lawtan.model.ReproEventType;
import com.lawtan.repository.AnimalRepository;
import com.lawtan.repository.HealthRecordRepository;
import com.lawtan.repository.MilkProductionRepository;
import com.lawtan.repository.ReproductionEventRepository;
import com.lawtan.repository.VaccineScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AnimalRepository animalRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;
    private final ReproductionEventRepository reproductionEventRepository;
    private final MilkProductionRepository milkProductionRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("ALTER TABLE animals ALTER COLUMN image_url TYPE TEXT");
            log.info("Colonne image_url convertie en type TEXT avec succès.");
        } catch (Exception e) {
            log.debug("Vérification colonne image_url: " + e.getMessage());
        }

        if (animalRepository.count() == 0) {
            initAnimalsAndHealth();
        }

        initReproductionData();
        initMilkData();
    }

    private void initAnimalsAndHealth() {
        log.info("Initialisation des animaux de démo pour la Ferme LAWTAN Agro Industries...");

        // 1. NDIRA (FL-001)
        Animal ndira = Animal.builder()
                .internalId("FL-001")
                .name("NDIRA")
                .earTagNumber("SN-DK-1423")
                .rfidCode("RFID-9820-001")
                .breed("Holstein x N'Dama")
                .birthDate(LocalDate.of(2022, 3, 14))
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.EXCELLENT)
                .weight(520.0)
                .temperature(38.6)
                .dailyMilkYield(21.5)
                .lactationNumber(3)
                .daysInMilk(154)
                .totalLactationMilk(1840.0)
                .reproStatus("Gestation à confirmer (IA Mai 2026)")
                .avatarEmoji("🐄")
                .origin("Ferme LAWTAN")
                .notes("Vache laitière d'exception, très rustique et productive.")
                .build();
        ndira.setPedigree(Pedigree.builder()
                .animal(ndira)
                .subjectNote("Holstein Pure — Ferme LAWTAN")
                .fatherName("SULTAN (USA-42891)")
                .fatherBreed("Holstein Champion USA")
                .fatherNote("Semence Importée A+")
                .motherName("NAFI (SN-0129)")
                .motherBreed("Montbéliarde x Gobra")
                .motherNote("Record : 24 L/j")
                .grandFatherPaternal("KING (CAN-8821)")
                .grandMotherPaternal("BELLA (USA-3341)")
                .grandFatherMaternal("MOUSSA (SN-0045)")
                .grandMotherMaternal("DIOUMA (SN-0048)")
                .build());

        // 2. MARIAMA (FL-002)
        Animal mariama = Animal.builder()
                .internalId("FL-002")
                .name("MARIAMA")
                .earTagNumber("SN-DK-1424")
                .rfidCode("RFID-9820-002")
                .breed("Montbéliarde")
                .birthDate(LocalDate.of(2021, 11, 20))
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.EXCELLENT)
                .weight(490.0)
                .temperature(38.5)
                .dailyMilkYield(19.0)
                .lactationNumber(4)
                .daysInMilk(190)
                .totalLactationMilk(2340.0)
                .reproStatus("Cycle régulier")
                .avatarEmoji("🐄")
                .origin("France (Import)")
                .notes("Excellente aptitude fromagère (lait riche en protéines).")
                .build();
        mariama.setPedigree(Pedigree.builder()
                .animal(mariama)
                .subjectNote("Montbéliarde Pure Lignée France")
                .fatherName("VALENTIN (FR-88910)")
                .fatherBreed("Montbéliard France")
                .fatherNote("Index fromager élevé")
                .motherName("CERISE (FR-1120)")
                .motherBreed("Montbéliarde")
                .motherNote("18 L/j")
                .grandFatherPaternal("JURA (FR-3301)")
                .grandMotherPaternal("ALPES (FR-2290)")
                .grandFatherMaternal("RHONE (FR-7711)")
                .grandMotherMaternal("DOUBS (FR-8822)")
                .build());

        // 3. DIOUMA (FL-003) - Alert Case
        Animal diouma = Animal.builder()
                .internalId("FL-003")
                .name("DIOUMA")
                .earTagNumber("SN-DK-1425")
                .rfidCode("RFID-9820-003")
                .breed("Holstein")
                .birthDate(LocalDate.of(2022, 6, 10))
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.FEVER_TREATMENT)
                .weight(510.0)
                .temperature(39.8)
                .dailyMilkYield(12.0)
                .lactationNumber(2)
                .daysInMilk(95)
                .totalLactationMilk(1120.0)
                .reproStatus("En traitement - Repos saillie")
                .avatarEmoji("🐄")
                .origin("Ferme LAWTAN")
                .notes("Sous traitement antibiotique Dr. Fall (fièvre 39.8°C). Délai d'attente lait 3j.")
                .build();
        diouma.setPedigree(Pedigree.builder()
                .animal(diouma)
                .subjectNote("Holstein Lignée Dakar")
                .fatherName("KADER (FL-010)")
                .fatherBreed("Holstein USA")
                .fatherNote("Taureau Ferme")
                .motherName("AMIE (SN-0098)")
                .motherBreed("Holstein Métisse")
                .motherNote("Bonne laitière")
                .grandFatherPaternal("SULTAN (USA-42891)")
                .grandMotherPaternal("NAFI (SN-0129)")
                .grandFatherMaternal("IBOU (SN-0021)")
                .grandMotherMaternal("FAMA (SN-0033)")
                .build());

        // 4. COUMBA (FL-004) - Pregnant
        Animal coumba = Animal.builder()
                .internalId("FL-004")
                .name("COUMBA")
                .earTagNumber("SN-DK-1426")
                .rfidCode("RFID-9820-004")
                .breed("Gir Laitier")
                .birthDate(LocalDate.of(2022, 1, 5))
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.PREGNANT)
                .weight(455.0)
                .temperature(38.4)
                .dailyMilkYield(18.0)
                .lactationNumber(2)
                .daysInMilk(120)
                .totalLactationMilk(1450.0)
                .reproStatus("Gestante (4 mois) - Vêlage Décembre 2026")
                .avatarEmoji("🐄")
                .origin("Brésil (Gir)")
                .notes("Gestation confirmée par échographie le 10/08.")
                .build();
        coumba.setPedigree(Pedigree.builder()
                .animal(coumba)
                .subjectNote("Gir Laitier Brésil Adapté")
                .fatherName("BRAHMA (IND-7712)")
                .fatherBreed("Gir Pur")
                .fatherNote("Lignée tropicale")
                .motherName("SAMBA (BR-9901)")
                .motherBreed("Gir Laitière")
                .motherNote("Haute résistance thermique")
                .grandFatherPaternal("KRISHNA (IND-1120)")
                .grandMotherPaternal("GANGA (IND-9912)")
                .grandFatherMaternal("RIO (BR-5512)")
                .grandMotherMaternal("BAHIA (BR-4411)")
                .build());

        // 5. FATOU (FL-005) - In Heat
        Animal fatou = Animal.builder()
                .internalId("FL-005")
                .name("FATOU")
                .earTagNumber("SN-DK-1427")
                .rfidCode("RFID-9820-005")
                .breed("Montbéliarde")
                .birthDate(LocalDate.of(2023, 2, 18))
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.IN_HEAT)
                .weight(430.0)
                .temperature(38.7)
                .dailyMilkYield(17.0)
                .lactationNumber(1)
                .daysInMilk(60)
                .totalLactationMilk(780.0)
                .reproStatus("Chaleurs dans 3j - IA programmée avec KADER")
                .avatarEmoji("🐄")
                .origin("Ferme LAWTAN")
                .notes("Fille de NDIRA. Première lactation prometteuse.")
                .build();
        fatou.setPedigree(Pedigree.builder()
                .animal(fatou)
                .subjectNote("Fille de NDIRA x Montbéliard")
                .fatherName("VALENTIN (FR-88910)")
                .fatherBreed("Montbéliard France")
                .fatherNote("Index laitier")
                .motherName("NDIRA (FL-001)")
                .motherBreed("Holstein Pure")
                .motherNote("21.5 L/j")
                .grandFatherPaternal("JURA (FR-3301)")
                .grandMotherPaternal("ALPES (FR-2290)")
                .grandFatherMaternal("SULTAN (USA-42891)")
                .grandMotherMaternal("NAFI (SN-0129)")
                .build());

        // 6. SOKHNA (FL-006)
        Animal sokhna = Animal.builder()
                .internalId("FL-006")
                .name("SOKHNA")
                .earTagNumber("SN-DK-1428")
                .rfidCode("RFID-9820-006")
                .breed("Gir Métisse")
                .birthDate(LocalDate.of(2022, 8, 22))
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.HEALTHY)
                .weight(440.0)
                .temperature(38.5)
                .dailyMilkYield(16.5)
                .lactationNumber(2)
                .daysInMilk(110)
                .totalLactationMilk(1290.0)
                .reproStatus("Vêlage Mars 2026")
                .avatarEmoji("🐄")
                .origin("Ferme LAWTAN")
                .notes("Excellente rusticité, adaptée au climat.")
                .build();
        sokhna.setPedigree(Pedigree.builder()
                .animal(sokhna)
                .subjectNote("Métisse Gir Rustique")
                .fatherName("BRAHMA (IND-7712)")
                .fatherBreed("Gir Pur")
                .fatherNote("Brésil")
                .motherName("BINETA (SN-0077)")
                .motherBreed("Gobra Sélectionnée")
                .motherNote("Locale")
                .grandFatherPaternal("KRISHNA (IND-1120)")
                .grandMotherPaternal("GANGA (IND-9912)")
                .grandFatherMaternal("ALPHA (SN-0010)")
                .grandMotherMaternal("ASTOU (SN-0015)")
                .build());

        // 7. ROKHAYA (FL-007)
        Animal rokhaya = Animal.builder()
                .internalId("FL-007")
                .name("ROKHAYA")
                .earTagNumber("SN-DK-1429")
                .rfidCode("RFID-9820-007")
                .breed("Holstein Pure")
                .birthDate(LocalDate.of(2021, 9, 12))
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.EXCELLENT)
                .weight(530.0)
                .temperature(38.6)
                .dailyMilkYield(20.0)
                .lactationNumber(3)
                .daysInMilk(140)
                .totalLactationMilk(1980.0)
                .reproStatus("Vêlage Avril 2026")
                .avatarEmoji("🐄")
                .origin("Ferme LAWTAN")
                .notes("Haute productrice, lait de qualité supérieure.")
                .build();
        rokhaya.setPedigree(Pedigree.builder()
                .animal(rokhaya)
                .subjectNote("Holstein Championne")
                .fatherName("SULTAN (USA-42891)")
                .fatherBreed("Holstein USA")
                .fatherNote("USA A+")
                .motherName("MAREME (SN-0055)")
                .motherBreed("Holstein Pure")
                .motherNote("22 L/j")
                .grandFatherPaternal("KING (CAN-8821)")
                .grandMotherPaternal("BELLA (USA-3341)")
                .grandFatherMaternal("PAPA (SN-0008)")
                .grandMotherMaternal("MAMA (SN-0009)")
                .build());

        // 8. AWA (FL-008) - Young Heifer
        Animal awa = Animal.builder()
                .internalId("FL-008")
                .name("AWA")
                .earTagNumber("SN-DK-1430")
                .rfidCode("RFID-9820-008")
                .breed("Montbéliarde")
                .birthDate(LocalDate.of(2025, 9, 15))
                .category(AnimalCategory.HEIFER_YOUNG)
                .status(AnimalStatus.GROWTH)
                .weight(180.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .lactationNumber(0)
                .daysInMilk(0)
                .totalLactationMilk(0.0)
                .reproStatus("Génisse en croissance (11 mois)")
                .avatarEmoji("🐮")
                .origin("Ferme LAWTAN")
                .notes("Fille de Mariama. Croissance +650g/jour.")
                .build();
        awa.setPedigree(Pedigree.builder()
                .animal(awa)
                .subjectNote("Génisse Montbéliarde")
                .fatherName("SAMBA (FL-011)")
                .fatherBreed("Montbéliard")
                .fatherNote("Taureau Ferme")
                .motherName("MARIAMA (FL-002)")
                .motherBreed("Montbéliarde")
                .motherNote("19 L/j")
                .grandFatherPaternal("VALENTIN (FR-88910)")
                .grandMotherPaternal("CERISE (FR-1120)")
                .grandFatherMaternal("VALENTIN (FR-88910)")
                .grandMotherMaternal("CERISE (FR-1120)")
                .build());

        // 9. AMINATA (FL-009) - Young Heifer
        Animal aminata = Animal.builder()
                .internalId("FL-009")
                .name("AMINATA")
                .earTagNumber("SN-DK-1431")
                .rfidCode("RFID-9820-009")
                .breed("Holstein Pure")
                .birthDate(LocalDate.of(2025, 6, 1))
                .category(AnimalCategory.HEIFER_YOUNG)
                .status(AnimalStatus.GROWTH)
                .weight(215.0)
                .temperature(38.6)
                .dailyMilkYield(0.0)
                .lactationNumber(0)
                .daysInMilk(0)
                .totalLactationMilk(0.0)
                .reproStatus("Génisse (14 mois) - Prête pour 1ère IA")
                .avatarEmoji("🐮")
                .origin("Ferme LAWTAN")
                .notes("Fille de NDIRA x KADER. Objectif 1ère saillie à 15 mois.")
                .build();
        aminata.setPedigree(Pedigree.builder()
                .animal(aminata)
                .subjectNote("Future Génitrice Laitière")
                .fatherName("KADER (FL-010)")
                .fatherBreed("Holstein Pure")
                .fatherNote("Reproducteur A+")
                .motherName("NDIRA (FL-001)")
                .motherBreed("Holstein Pure")
                .motherNote("21.5 L/j")
                .grandFatherPaternal("SULTAN (USA-42891)")
                .grandMotherPaternal("NAFI (SN-0129)")
                .grandFatherMaternal("SULTAN (USA-42891)")
                .grandMotherMaternal("NAFI (SN-0129)")
                .build());

        // 10. KADER (FL-010) - Breeder Bull
        Animal kader = Animal.builder()
                .internalId("FL-010")
                .name("KADER")
                .earTagNumber("SN-DK-1432")
                .rfidCode("RFID-9820-010")
                .breed("Holstein Pure")
                .birthDate(LocalDate.of(2022, 2, 10))
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.BREEDER_BULL)
                .weight(780.0)
                .temperature(38.4)
                .dailyMilkYield(0.0)
                .lactationNumber(0)
                .daysInMilk(0)
                .totalLactationMilk(0.0)
                .reproStatus("Taureau Reproducteur Principal - Semence A+")
                .avatarEmoji("🐂")
                .origin("USA (Lignée Championne)")
                .notes("Taureau d'élite pour insémination artificielle. 12 doses de semence congelées.")
                .build();
        kader.setPedigree(Pedigree.builder()
                .animal(kader)
                .subjectNote("Taureau Reproducteur Lignée Championne")
                .fatherName("TITAN (USA-99881)")
                .fatherBreed("Holstein USA")
                .fatherNote("Père d'élites")
                .motherName("QUEEN (USA-11442)")
                .motherBreed("Holstein USA")
                .motherNote("Championne 30 L/j")
                .grandFatherPaternal("APOLLO (USA-5511)")
                .grandMotherPaternal("VENUS (USA-3399)")
                .grandFatherMaternal("MAX (USA-7722)")
                .grandMotherMaternal("DIAMOND (USA-8811)")
                .semenMobilityPercentage(85.0)
                .semenConcentration(1.2)
                .semenMorphologyOkPercentage(90.0)
                .semenDosesAvailable(12)
                .build());

        // 11. SAMBA (FL-011) - Bull 2
        Animal samba = Animal.builder()
                .internalId("FL-011")
                .name("SAMBA")
                .earTagNumber("SN-DK-1433")
                .rfidCode("RFID-9820-011")
                .breed("Montbéliard")
                .birthDate(LocalDate.of(2023, 4, 15))
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.BREEDER_BULL)
                .weight(720.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .lactationNumber(0)
                .daysInMilk(0)
                .totalLactationMilk(0.0)
                .reproStatus("Taureau Montbéliard - 8 Doses")
                .avatarEmoji("🐂")
                .origin("France")
                .notes("Taureau pour amélioration fromagère.")
                .build();
        samba.setPedigree(Pedigree.builder()
                .animal(samba)
                .subjectNote("Montbéliard Race Pure")
                .fatherName("VALENTIN (FR-88910)")
                .fatherBreed("Montbéliard")
                .fatherNote("France")
                .motherName("DOUCE (FR-4412)")
                .motherBreed("Montbéliarde")
                .motherNote("Laitière A+")
                .grandFatherPaternal("JURA (FR-3301)")
                .grandMotherPaternal("ALPES (FR-2290)")
                .grandFatherMaternal("LION (FR-9901)")
                .grandMotherMaternal("BELLA (FR-3388)")
                .semenMobilityPercentage(80.0)
                .semenConcentration(1.1)
                .semenMorphologyOkPercentage(88.0)
                .semenDosesAvailable(8)
                .build());

        // 12. BADOU (FL-012) - Young Bull
        Animal badou = Animal.builder()
                .internalId("FL-012")
                .name("BADOU")
                .earTagNumber("SN-DK-1434")
                .rfidCode("RFID-9820-012")
                .breed("Gir Pur")
                .birthDate(LocalDate.of(2025, 2, 10))
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.GROWTH)
                .weight(380.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .lactationNumber(0)
                .daysInMilk(0)
                .totalLactationMilk(0.0)
                .reproStatus("Jeune Mâle (18 mois) - Futur Reproducteur")
                .avatarEmoji("🐂")
                .origin("Ferme LAWTAN")
                .notes("Excellente musculature et ossature.")
                .build();
        badou.setPedigree(Pedigree.builder()
                .animal(badou)
                .subjectNote("Jeune Taureau Gir")
                .fatherName("BRAHMA (IND-7712)")
                .fatherBreed("Gir")
                .fatherNote("Brésil")
                .motherName("COUMBA (FL-004)")
                .motherBreed("Gir Laitier")
                .motherNote("18 L/j")
                .grandFatherPaternal("KRISHNA (IND-1120)")
                .grandMotherPaternal("GANGA (IND-9912)")
                .grandFatherMaternal("RIO (BR-5512)")
                .grandMotherMaternal("BAHIA (BR-4411)")
                .build());

        // 13. MODOU (FL-013) - Young Bull
        Animal modou = Animal.builder()
                .internalId("FL-013")
                .name("MODOU")
                .earTagNumber("SN-DK-1435")
                .rfidCode("RFID-9820-013")
                .breed("Métis Gobra x Holstein")
                .birthDate(LocalDate.of(2025, 3, 20))
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.GROWTH)
                .weight(350.0)
                .temperature(38.6)
                .dailyMilkYield(0.0)
                .lactationNumber(0)
                .daysInMilk(0)
                .totalLactationMilk(0.0)
                .reproStatus("Jeune Mâle (17 mois)")
                .avatarEmoji("🐂")
                .origin("Ferme LAWTAN")
                .notes("En phase d'engraissement et croissance.")
                .build();
        modou.setPedigree(Pedigree.builder()
                .animal(modou)
                .subjectNote("Croisement Rustique")
                .fatherName("KADER (FL-010)")
                .fatherBreed("Holstein Pure")
                .fatherNote("Taureau Ferme")
                .motherName("SOKHNA (FL-006)")
                .motherBreed("Gir Métisse")
                .motherNote("16.5 L/j")
                .grandFatherPaternal("TITAN (USA-99881)")
                .grandMotherPaternal("QUEEN (USA-11442)")
                .grandFatherMaternal("BRAHMA (IND-7712)")
                .grandMotherMaternal("BINETA (SN-0077)")
                .build());

        animalRepository.saveAll(List.of(
                ndira, mariama, diouma, coumba, fatou, sokhna, rokhaya,
                awa, aminata, kader, samba, badou, modou
        ));

        // Health Records
        HealthRecord hr1 = HealthRecord.builder()
                .animal(diouma)
                .recordDate(LocalDate.now().minusDays(1))
                .actType("Traitement Pathologie")
                .diagnosis("Fièvre 39.8°C relevée lors du contrôle matinal")
                .treatmentPrescription("Protocole antipyrétique + antibiotique injectable (3 jours). Délai d'attente lait.")
                .practitionerName("Dr. Fall (Vétérinaire Ferme)")
                .costFcfa(12500.0)
                .status("En cours")
                .milkWithdrawalDays(3)
                .build();

        HealthRecord hr2 = HealthRecord.builder()
                .animal(mariama)
                .recordDate(LocalDate.now().minusDays(4))
                .actType("Vaccination")
                .diagnosis("Vaccination annuelle IBR & BVD")
                .treatmentPrescription("Injection 2ml sous-cutanée")
                .practitionerName("Dr. Fall")
                .costFcfa(8000.0)
                .status("Terminé")
                .milkWithdrawalDays(0)
                .build();

        HealthRecord hr3 = HealthRecord.builder()
                .animal(fatou)
                .recordDate(LocalDate.now().minusDays(9))
                .actType("Bilan pré-insémination")
                .diagnosis("Contrôle œstrus et pesée (430 kg)")
                .treatmentPrescription("Vache apte pour insémination artificielle")
                .practitionerName("Technicien Élevage")
                .costFcfa(0.0)
                .status("Apte IA")
                .milkWithdrawalDays(0)
                .build();

        HealthRecord hr4 = HealthRecord.builder()
                .animal(kader)
                .recordDate(LocalDate.now().minusDays(13))
                .actType("Analyse Spermatique")
                .diagnosis("Spermogramme & contrôle fertilité semence")
                .treatmentPrescription("85% mobilité, 1.2e9/mL concentration. Qualité A+ confirmée.")
                .practitionerName("Dr. Fall")
                .costFcfa(25000.0)
                .status("Qualité A+")
                .milkWithdrawalDays(0)
                .build();

        healthRecordRepository.saveAll(List.of(hr1, hr2, hr3, hr4));

        // Vaccine Schedules
        VaccineSchedule vs1 = VaccineSchedule.builder()
                .vaccineType("Rappel Fièvre Aphteuse")
                .targetHerd("Tout le troupeau (13 animaux)")
                .scheduledDate(LocalDate.now().plusDays(2))
                .practitioner("Dr. Fall (Vétérinaire Ferme)")
                .estimatedCost(20000.0)
                .status("Dans 2 jours")
                .notes("Intervention matinale à 08h00 à l'étable.")
                .build();

        VaccineSchedule vs2 = VaccineSchedule.builder()
                .vaccineType("Péripneumonie Contagieuse Bovine (PPCB)")
                .targetHerd("Vaches en lactation + 2 jeunes génisses")
                .scheduledDate(LocalDate.now().plusDays(15))
                .practitioner("Dr. Fall")
                .estimatedCost(15000.0)
                .status("Planifié")
                .notes("Rappel semestriel réglementaire.")
                .build();

        VaccineSchedule vs3 = VaccineSchedule.builder()
                .vaccineType("Déparasitage Global (Ivermectine)")
                .targetHerd("Troupeau complet (13 têtes)")
                .scheduledDate(LocalDate.now().plusDays(30))
                .practitioner("Technicien Élevage")
                .estimatedCost(10000.0)
                .status("Planifié")
                .notes("Administration antiparasitaire interne & externe.")
                .build();

        vaccineScheduleRepository.saveAll(List.of(vs1, vs2, vs3));
        log.info("Initialisation terminée avec succès : 13 animaux, pedigrees, carnet de santé et calendrier vaccinal prêts !");
    }

    private void initReproductionData() {
        if (reproductionEventRepository.count() > 0) {
            return;
        }

        log.info("Initialisation des événements de reproduction...");

        animalRepository.findByInternalId("FL-005").ifPresent(fatou -> {
            ReproductionEvent re1 = ReproductionEvent.builder()
                    .animal(fatou)
                    .eventType(ReproEventType.HEAT_DETECTION)
                    .eventDate(LocalDate.now().minusDays(3))
                    .operatorName("Bouvier Responsable")
                    .observations("Signes œstrus francs, agitation et chevauchement.")
                    .isConfirmed(true)
                    .build();

            ReproductionEvent re2 = ReproductionEvent.builder()
                    .animal(fatou)
                    .eventType(ReproEventType.ARTIFICIAL_INSEMINATION)
                    .eventDate(LocalDate.now().minusDays(1))
                    .bullOrSemenUsed("KADER (FL-010) — Semence A+")
                    .operatorName("Dr. Fall (Vétérinaire Ferme)")
                    .expectedDryOffDate(LocalDate.now().minusDays(1).plusDays(222))
                    .expectedCalvingDate(LocalDate.now().minusDays(1).plusDays(282))
                    .observations("Insémination réussie avec paille congelée KADER A+.")
                    .isConfirmed(true)
                    .build();

            reproductionEventRepository.saveAll(List.of(re1, re2));
        });

        animalRepository.findByInternalId("FL-004").ifPresent(coumba -> {
            ReproductionEvent re = ReproductionEvent.builder()
                    .animal(coumba)
                    .eventType(ReproEventType.PREGNANCY_DIAGNOSIS)
                    .eventDate(LocalDate.now().minusDays(30))
                    .bullOrSemenUsed("BRAHMA (IND-7712)")
                    .operatorName("Dr. Fall")
                    .expectedDryOffDate(LocalDate.now().plusDays(55))
                    .expectedCalvingDate(LocalDate.now().plusDays(115))
                    .observations("Échographie positive (gestation 4 mois). Vœu femelle détecté.")
                    .isConfirmed(true)
                    .build();
            reproductionEventRepository.save(re);
        });

        animalRepository.findByInternalId("FL-007").ifPresent(rokhaya -> {
            ReproductionEvent re = ReproductionEvent.builder()
                    .animal(rokhaya)
                    .eventType(ReproEventType.PREGNANCY_DIAGNOSIS)
                    .eventDate(LocalDate.now().minusMonths(8))
                    .bullOrSemenUsed("SULTAN (USA-42891)")
                    .operatorName("Dr. Fall")
                    .expectedDryOffDate(LocalDate.now().minusDays(48))
                    .expectedCalvingDate(LocalDate.now().plusDays(12))
                    .observations("Vêlage imminent sous 12 jours. Surveillance rapprochée au box de mise bas.")
                    .isConfirmed(true)
                    .build();
            reproductionEventRepository.save(re);
        });

        animalRepository.findByInternalId("FL-002").ifPresent(mariama -> {
            ReproductionEvent re = ReproductionEvent.builder()
                    .animal(mariama)
                    .eventType(ReproEventType.DRY_OFF)
                    .eventDate(LocalDate.now().plusDays(7))
                    .bullOrSemenUsed("VALENTIN (FR-88910)")
                    .operatorName("Dr. Fall")
                    .expectedDryOffDate(LocalDate.now().plusDays(7))
                    .expectedCalvingDate(LocalDate.now().plusDays(67))
                    .observations("Tarissement programmé sous 7 jours avant prochain vêlage.")
                    .isConfirmed(true)
                    .build();
            reproductionEventRepository.save(re);
        });

        log.info("Événements de reproduction initialisés.");
    }

    private void initMilkData() {
        if (milkProductionRepository.count() > 0) {
            return;
        }

        log.info("Initialisation des collectes de traite...");
        LocalDate today = LocalDate.now();

        List<String> milkingIds = List.of("FL-001", "FL-002", "FL-004", "FL-005", "FL-006", "FL-007");

        for (String id : milkingIds) {
            animalRepository.findByInternalId(id).ifPresent(cow -> {
                double base = (cow.getDailyMilkYield() != null && cow.getDailyMilkYield() > 0) ? cow.getDailyMilkYield() : 18.0;
                double mYield = Math.round((base * 0.58) * 10.0) / 10.0;
                double eYield = Math.round((base * 0.42) * 10.0) / 10.0;

                MilkProduction mpMorn = MilkProduction.builder()
                        .animal(cow)
                        .productionDate(today)
                        .session(MilkSession.MORNING)
                        .volumeLiters(mYield)
                        .milkTemperature(34.2)
                        .fatPercentage(4.1)
                        .destinationTank("Cuve Réfrigérée N°1 (Bio)")
                        .isOrganicCompliant(true)
                        .build();

                MilkProduction mpEve = MilkProduction.builder()
                        .animal(cow)
                        .productionDate(today)
                        .session(MilkSession.EVENING)
                        .volumeLiters(eYield)
                        .milkTemperature(34.0)
                        .fatPercentage(4.2)
                        .destinationTank("Cuve Réfrigérée N°1 (Bio)")
                        .isOrganicCompliant(true)
                        .build();

                milkProductionRepository.saveAll(List.of(mpMorn, mpEve));
            });
        }

        log.info("Collectes de traite initialisées.");
    }
}
