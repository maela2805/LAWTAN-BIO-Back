package com.lawtan.config;

import com.lawtan.entity.*;
import com.lawtan.model.*;
import com.lawtan.repository.*;
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
    private final RecipeRepository recipeRepository;
    private final TransformationBatchRepository transformationBatchRepository;
    private final ProductStockRepository productStockRepository;
    private final CustomerRepository customerRepository;
    private final SaleInvoiceRepository saleInvoiceRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final FeedStockRepository feedStockRepository;
    private final FeedRationRepository feedRationRepository;
    private final SolarEnergyMetricRepository solarEnergyMetricRepository;
    private final SupplierRepository supplierRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("ALTER TABLE animals ALTER COLUMN image_url TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE animals ALTER COLUMN ear_tag_number DROP NOT NULL");
            jdbcTemplate.execute("ALTER TABLE animals DROP CONSTRAINT IF EXISTS animals_ear_tag_number_key");
            log.info("Colonnes image_url et ear_tag_number adaptées avec succès.");
        } catch (Exception e) {
            log.debug("Vérification colonne image_url/ear_tag_number: " + e.getMessage());
        }

        if (animalRepository.count() == 0) {
            log.info("Initialisation du cheptel réel de la Ferme LAWTAN (18 têtes selon le registre officiel CowList)...");
            initRealHerd();
            initRealReproductionData();
            initRealMilkData();
            initRealHealthData();
            initRealTransformationData();
            initRealCommercialData();
            initRealFeedAndSolarData();
            initRealSuppliersData();
            log.info("✅ Données réelles de la Ferme LAWTAN insérées avec succès dans PostgreSQL !");
        } else {
            log.info("Cheptel déjà présent en base ({} animaux enregistrés).", animalRepository.count());
        }
    }

    private void initRealHerd() {
        // 1. H - 1043
        Animal h1043 = Animal.builder()
                .internalId("H-1043")
                .name("Holstein 1043")
                .earTagNumber("H-1043")
                .rfidCode("RFID-1043-001")
                .breed("Holstein")
                .birthDate(LocalDate.of(2021, 1, 22))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.EXCELLENT)
                .weight(560.0)
                .temperature(38.5)
                .dailyMilkYield(22.5)
                .lactationNumber(3)
                .daysInMilk(140)
                .totalLactationMilk(2450.0)
                .reproStatus("En lactation active")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Vache Holstein Pure - Lignée haute productrice")
                        .fatherName("Géniteur Importé USA")
                        .motherName("Lignée Holstein A+")
                        .build())
                .build();

        // 2. H - 4835
        Animal h4835 = Animal.builder()
                .internalId("H-4835")
                .name("Holstein 4835")
                .earTagNumber("H-4835")
                .rfidCode("RFID-4835-002")
                .breed("Holstein")
                .birthDate(LocalDate.of(2021, 1, 22))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.HEALTHY)
                .weight(545.0)
                .temperature(38.6)
                .dailyMilkYield(20.0)
                .lactationNumber(3)
                .daysInMilk(148)
                .totalLactationMilk(2180.0)
                .reproStatus("À inséminer dans les 100j post-vêlage (148 jours)")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Vache Holstein Pure")
                        .build())
                .build();

        // 3. M - 2713
        Animal m2713 = Animal.builder()
                .internalId("M-2713")
                .name("Montbeliarde 2713")
                .earTagNumber("M-2713")
                .rfidCode("RFID-2713-003")
                .breed("Montbeliarde")
                .birthDate(LocalDate.of(2019, 1, 22))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.PREGNANT)
                .weight(590.0)
                .temperature(38.6)
                .dailyMilkYield(19.0)
                .lactationNumber(4)
                .daysInMilk(121)
                .totalLactationMilk(3100.0)
                .reproStatus("Gestante (Jour 121) - Vêlage estimé dans 166 jours")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Matriarche Montbéliarde de la ferme (Mère de M-0022, MH-0028, MH-0031)")
                        .build())
                .build();

        // 4. M - 8326
        Animal m8326 = Animal.builder()
                .internalId("M-8326")
                .name("Montbeliarde 8326")
                .earTagNumber("M-8326")
                .rfidCode("RFID-8326-004")
                .breed("Montbeliarde")
                .birthDate(LocalDate.of(2021, 1, 22))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.HEALTHY)
                .weight(565.0)
                .temperature(38.4)
                .dailyMilkYield(16.5)
                .lactationNumber(3)
                .daysInMilk(396)
                .totalLactationMilk(2600.0)
                .reproStatus("Days Open: 396j - Intervalle vêlage élevé (Mère de M-0030)")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Vache Montbéliarde (Mère de M-0030)")
                        .build())
                .build();

        // 5. J - 9163
        Animal j9163 = Animal.builder()
                .internalId("J-9163")
                .name("Jersiaise 9163")
                .earTagNumber("J-9163")
                .rfidCode("RFID-9163-005")
                .breed("Jersiaise")
                .birthDate(LocalDate.of(2019, 1, 22))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.PREGNANT)
                .weight(430.0)
                .temperature(38.5)
                .dailyMilkYield(15.5)
                .lactationNumber(4)
                .daysInMilk(121)
                .totalLactationMilk(2800.0)
                .reproStatus("Gestante (Jour 121) - Vêlage estimé dans 159 jours")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Matriarche Jersiaise Pure (Mère de J-0766)")
                        .build())
                .build();

        // 6. J - 0766
        Animal j0766 = Animal.builder()
                .internalId("J-0766")
                .name("Jersiaise 0766 - Ex 02")
                .earTagNumber("J-0766")
                .rfidCode("RFID-0766-006")
                .breed("Jersiaise")
                .birthDate(LocalDate.of(2022, 3, 12))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.HEALTHY)
                .weight(415.0)
                .temperature(38.6)
                .dailyMilkYield(16.0)
                .lactationNumber(2)
                .daysInMilk(155)
                .totalLactationMilk(1900.0)
                .reproStatus("À inséminer dans les 100j post-vêlage (155 jours)")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Fille de J-9163 (Mère du veau JM-0033)")
                        .motherName("Jersiaise 9163")
                        .motherEarTag("J-9163")
                        .motherBreed("Jersiaise")
                        .build())
                .build();

        // 7. JH - 0027
        Animal jh0027 = Animal.builder()
                .internalId("JH-0027")
                .name("Jersiaise-Holstein 027")
                .earTagNumber("JH-0027")
                .rfidCode("RFID-0027-007")
                .breed("Croisé Jersiaise/Holstein")
                .birthDate(LocalDate.of(2023, 12, 5))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.HEALTHY)
                .weight(465.0)
                .temperature(38.7)
                .dailyMilkYield(18.0)
                .lactationNumber(1)
                .daysInMilk(216)
                .totalLactationMilk(1600.0)
                .reproStatus("Days Open: 216j - En attente d'insémination (Mère de JHM-0032)")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Croisement F1 Jersiaise x Holstein")
                        .motherName("J-0021")
                        .motherEarTag("J-0021")
                        .fatherName("Holstein blanc (Né de 1647)")
                        .fatherBreed("Holstein")
                        .build())
                .build();

        // 8. M - 0022
        Animal m0022 = Animal.builder()
                .internalId("M-0022")
                .name("Montbeliard 022")
                .earTagNumber("M-0022")
                .rfidCode("RFID-0022-008")
                .breed("Montbéliard")
                .birthDate(LocalDate.of(2022, 3, 14))
                .gender("MALE")
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.BREEDER_BULL)
                .weight(790.0)
                .temperature(38.4)
                .dailyMilkYield(0.0)
                .reproStatus("Taureau Reproducteur Montbéliard Actif (Père de JHM-0032, JM-0033)")
                .avatarEmoji("🐂")
                .pedigree(Pedigree.builder()
                        .subjectNote("Taureau géniteur Montbéliard")
                        .motherName("Montbeliarde 2713")
                        .motherEarTag("M-2713")
                        .semenMobilityPercentage(88.0)
                        .semenConcentration(1.3)
                        .semenDosesAvailable(24)
                        .build())
                .build();

        // 9. H - 0023
        Animal h0023 = Animal.builder()
                .internalId("H-0023")
                .name("Holstein 023")
                .earTagNumber("H-0023")
                .rfidCode("RFID-0023-009")
                .breed("Holstein")
                .birthDate(LocalDate.of(2023, 3, 23))
                .gender("MALE")
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.BREEDER_BULL)
                .weight(730.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .reproStatus("Taureau Reproducteur Holstein Actif (Père de MH-0031)")
                .avatarEmoji("🐂")
                .pedigree(Pedigree.builder()
                        .subjectNote("Taureau Holstein Pure")
                        .motherName("Holstein 1043")
                        .motherEarTag("H-1043")
                        .semenMobilityPercentage(90.0)
                        .semenConcentration(1.4)
                        .semenDosesAvailable(30)
                        .build())
                .build();

        // 10. MH - 0028
        Animal mh0028 = Animal.builder()
                .internalId("MH-0028")
                .name("Montbéliard-Holstein 028")
                .earTagNumber("MH-0028")
                .rfidCode("RFID-0028-010")
                .breed("Mont-Holstein")
                .birthDate(LocalDate.of(2024, 5, 25))
                .gender("MALE")
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.GROWTH)
                .weight(530.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .reproStatus("Jeune taureau croisé Montbéliard-Holstein")
                .avatarEmoji("🐂")
                .pedigree(Pedigree.builder()
                        .subjectNote("Croisement F1 Montbéliarde x Holstein")
                        .motherName("Montbeliarde 2713")
                        .motherEarTag("M-2713")
                        .fatherName("Holstein blanc né de 1647_ANIPL 02")
                        .build())
                .build();

        // 11. G-001
        Animal g001 = Animal.builder()
                .internalId("G-001")
                .name("Gobra 001")
                .earTagNumber("G-001")
                .rfidCode("RFID-G001-011")
                .breed("Gobra")
                .birthDate(LocalDate.of(2021, 11, 17))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.PREGNANT)
                .weight(390.0)
                .temperature(38.4)
                .dailyMilkYield(8.5)
                .lactationNumber(2)
                .daysInMilk(95)
                .totalLactationMilk(950.0)
                .reproStatus("Inséminée (Jour 25) — Contrôle gestation prévu (Mère de G-002, G-003)")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Vache Gobra locale rustique et adaptée")
                        .build())
                .build();

        // 12. G-002
        Animal g002 = Animal.builder()
                .internalId("G-002")
                .name("Gobra 002")
                .earTagNumber("G-002")
                .rfidCode("RFID-G002-012")
                .breed("Gobra")
                .birthDate(LocalDate.of(2023, 9, 15))
                .gender("FEMALE")
                .category(AnimalCategory.HEIFER_YOUNG)
                .status(AnimalStatus.PREGNANT)
                .weight(320.0)
                .temperature(38.6)
                .dailyMilkYield(0.0)
                .reproStatus("Génisse Inséminée (Jour 27) — Première saillie")
                .avatarEmoji("🐮")
                .pedigree(Pedigree.builder()
                        .subjectNote("Génisse Gobra issue de G-001")
                        .motherName("Gobra 001")
                        .motherEarTag("G-001")
                        .build())
                .build();

        // 13. G-003
        Animal g003 = Animal.builder()
                .internalId("G-003")
                .name("Gobra 003")
                .earTagNumber("G-003")
                .rfidCode("RFID-G003-013")
                .breed("Gobra")
                .birthDate(LocalDate.of(2025, 8, 5))
                .gender("MALE")
                .category(AnimalCategory.MALE_BULL)
                .status(AnimalStatus.GROWTH)
                .weight(190.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .reproStatus("Jeune taillon Gobra en élevage")
                .avatarEmoji("🐂")
                .pedigree(Pedigree.builder()
                        .subjectNote("Jeune mâle Gobra issu de G-001")
                        .motherName("Gobra 001")
                        .motherEarTag("G-001")
                        .build())
                .build();

        // 14. MH-0031
        Animal mh0031 = Animal.builder()
                .internalId("MH-0031")
                .name("Montbéliard/Holstein 031")
                .earTagNumber("MH-0031")
                .rfidCode("RFID-0031-014")
                .breed("Croisé Montbéliard/Holstein")
                .birthDate(LocalDate.of(2025, 12, 9))
                .gender("MALE")
                .category(AnimalCategory.HEIFER_YOUNG)
                .status(AnimalStatus.GROWTH)
                .weight(160.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .reproStatus("Jeune bovin mâle F1")
                .avatarEmoji("🐂")
                .pedigree(Pedigree.builder()
                        .subjectNote("Croisé Montbéliarde x Holstein")
                        .motherName("Montbeliarde 2713")
                        .motherEarTag("M-2713")
                        .fatherName("Holstein 023 (H-0023)")
                        .fatherEarTag("H-0023")
                        .build())
                .build();

        // 15. JHM - 0032
        Animal jhm0032 = Animal.builder()
                .internalId("JHM-0032")
                .name("JHMontbeliarde JHM")
                .earTagNumber("JHM-0032")
                .rfidCode("RFID-0032-015")
                .breed("Croisé JH et Montbéliarde")
                .birthDate(LocalDate.of(2026, 1, 10))
                .gender("FEMALE")
                .category(AnimalCategory.HEIFER_YOUNG)
                .status(AnimalStatus.GROWTH)
                .weight(130.0)
                .temperature(38.6)
                .dailyMilkYield(0.0)
                .reproStatus("Génisse croisée 3 voies (Jersiaise x Holstein x Montbéliarde)")
                .avatarEmoji("🐮")
                .pedigree(Pedigree.builder()
                        .subjectNote("Croisement 3 races à haute valeur laitière")
                        .motherName("Jersiaise-Holstein 027")
                        .motherEarTag("JH-0027")
                        .fatherName("Montbeliard 022 (M-0022)")
                        .fatherEarTag("M-0022")
                        .build())
                .build();

        // 16. JM-0033
        Animal jm0033 = Animal.builder()
                .internalId("JM-0033")
                .name("Jersiaise Montbéliard 033")
                .earTagNumber("JM-0033")
                .rfidCode("RFID-0033-016")
                .breed("Croisé Jersiaise/Montbéliard")
                .birthDate(LocalDate.of(2026, 3, 11))
                .gender("FEMALE")
                .category(AnimalCategory.HEIFER_YOUNG)
                .status(AnimalStatus.GROWTH)
                .weight(95.0)
                .temperature(38.6)
                .dailyMilkYield(0.0)
                .reproStatus("Veau femelle sous surveillance nutritionnelle")
                .avatarEmoji("🐮")
                .pedigree(Pedigree.builder()
                        .subjectNote("Veau F1 Jersiaise x Montbéliarde")
                        .motherName("Jersiaise 0766 - Ex 02")
                        .motherEarTag("J-0766")
                        .fatherName("Montbeliard 022 (M-0022)")
                        .fatherEarTag("M-0022")
                        .build())
                .build();

        // 17. M-0030
        Animal m0030 = Animal.builder()
                .internalId("M-0030")
                .name("Montbéliarde 030")
                .earTagNumber("M-0030")
                .rfidCode("RFID-0030-017")
                .breed("Montbéliard")
                .birthDate(LocalDate.of(2025, 7, 13))
                .gender("FEMALE")
                .category(AnimalCategory.HEIFER_YOUNG)
                .status(AnimalStatus.GROWTH)
                .weight(270.0)
                .temperature(38.5)
                .dailyMilkYield(0.0)
                .reproStatus("Génisse Montbéliarde issue d'IA (Fallou)")
                .avatarEmoji("🐮")
                .pedigree(Pedigree.builder()
                        .subjectNote("Génisse issue d'insémination artificielle")
                        .motherName("Montbeliarde 8326")
                        .motherEarTag("M-8326")
                        .fatherName("Fallou Inséminateur (Semence Montbéliarde)")
                        .build())
                .build();

        // 18. G-004
        Animal g004 = Animal.builder()
                .internalId("G-004")
                .name("Gobra 004")
                .earTagNumber("G-004")
                .rfidCode("RFID-G004-018")
                .breed("Gobra")
                .birthDate(LocalDate.of(2023, 5, 15))
                .gender("FEMALE")
                .category(AnimalCategory.MILKING_COW)
                .status(AnimalStatus.HEALTHY)
                .weight(380.0)
                .temperature(38.5)
                .dailyMilkYield(7.5)
                .lactationNumber(1)
                .daysInMilk(110)
                .totalLactationMilk(820.0)
                .reproStatus("Événements manquants : ajouter enregistrements d'IA ou de saillie !")
                .avatarEmoji("🐄")
                .pedigree(Pedigree.builder()
                        .subjectNote("Vache Gobra locale")
                        .build())
                .build();

        List<Animal> realHerd = List.of(
                h1043, h4835, m2713, m8326, j9163, j0766, jh0027,
                m0022, h0023, mh0028, g001, g002, g003, mh0031,
                jhm0032, jm0033, m0030, g004
        );

        for (Animal a : realHerd) {
            if (a.getPedigree() != null) {
                a.getPedigree().setAnimal(a);
            }
        }

        animalRepository.saveAll(realHerd);
        log.info("18 animaux réels enregistrés avec succès.");
    }

    private void initRealReproductionData() {
        LocalDate today = LocalDate.now();
        Animal m2713 = animalRepository.findByInternalId("M-2713").orElse(null);
        Animal j9163 = animalRepository.findByInternalId("J-9163").orElse(null);
        Animal g001 = animalRepository.findByInternalId("G-001").orElse(null);
        Animal g002 = animalRepository.findByInternalId("G-002").orElse(null);

        ReproductionEvent reproM2713 = ReproductionEvent.builder()
                .animal(m2713)
                .eventType(ReproEventType.PREGNANCY_DIAGNOSIS)
                .eventDate(today.minusDays(121))
                .bullOrSemenUsed("Montbeliard 022 (M-0022)")
                .operatorName("Dr. Fall")
                .expectedDryOffDate(today.plusDays(106))
                .expectedCalvingDate(today.plusDays(166))
                .observations("Gestation confirmée par échographie à 45 jours. Vêlage estimé dans 166 jours.")
                .isConfirmed(true)
                .build();

        ReproductionEvent reproJ9163 = ReproductionEvent.builder()
                .animal(j9163)
                .eventType(ReproEventType.PREGNANCY_DIAGNOSIS)
                .eventDate(today.minusDays(121))
                .bullOrSemenUsed("Holstein 023 (H-0023)")
                .operatorName("Dr. Fall")
                .expectedDryOffDate(today.plusDays(99))
                .expectedCalvingDate(today.plusDays(159))
                .observations("Gestation confirmée. Vêlage estimé dans 159 jours.")
                .isConfirmed(true)
                .build();

        ReproductionEvent reproG001 = ReproductionEvent.builder()
                .animal(g001)
                .eventType(ReproEventType.ARTIFICIAL_INSEMINATION)
                .eventDate(today.minusDays(25))
                .bullOrSemenUsed("Montbeliard 022 (M-0022)")
                .operatorName("Fallou Inséminateur")
                .expectedCalvingDate(today.plusDays(255))
                .observations("Insémination artificielle il y a 25 jours. Palpation de contrôle prévue à J+45.")
                .isConfirmed(false)
                .build();

        ReproductionEvent reproG002 = ReproductionEvent.builder()
                .animal(g002)
                .eventType(ReproEventType.ARTIFICIAL_INSEMINATION)
                .eventDate(today.minusDays(27))
                .bullOrSemenUsed("Holstein 023 (H-0023)")
                .operatorName("Fallou Inséminateur")
                .expectedCalvingDate(today.plusDays(253))
                .observations("Première insémination génisse réussie.")
                .isConfirmed(false)
                .build();

        reproductionEventRepository.saveAll(List.of(reproM2713, reproJ9163, reproG001, reproG002));
    }

    private void initRealMilkData() {
        LocalDate today = LocalDate.now();
        Animal h1043 = animalRepository.findByInternalId("H-1043").orElse(null);
        Animal h4835 = animalRepository.findByInternalId("H-4835").orElse(null);
        Animal m2713 = animalRepository.findByInternalId("M-2713").orElse(null);
        Animal m8326 = animalRepository.findByInternalId("M-8326").orElse(null);
        Animal j9163 = animalRepository.findByInternalId("J-9163").orElse(null);
        Animal j0766 = animalRepository.findByInternalId("J-0766").orElse(null);
        Animal jh0027 = animalRepository.findByInternalId("JH-0027").orElse(null);
        Animal g001 = animalRepository.findByInternalId("G-001").orElse(null);
        Animal g004 = animalRepository.findByInternalId("G-004").orElse(null);

        List<MilkProduction> productions = List.of(
                MilkProduction.builder().animal(h1043).session(MilkSession.MORNING).productionDate(today).volumeLiters(13.0).milkTemperature(34.1).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(h1043).session(MilkSession.EVENING).productionDate(today).volumeLiters(9.5).milkTemperature(34.2).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(h4835).session(MilkSession.MORNING).productionDate(today).volumeLiters(11.5).milkTemperature(34.0).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(h4835).session(MilkSession.EVENING).productionDate(today).volumeLiters(8.5).milkTemperature(34.3).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(m2713).session(MilkSession.MORNING).productionDate(today).volumeLiters(11.0).milkTemperature(34.2).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(m2713).session(MilkSession.EVENING).productionDate(today).volumeLiters(8.0).milkTemperature(34.1).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(m8326).session(MilkSession.MORNING).productionDate(today).volumeLiters(9.5).milkTemperature(34.2).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(m8326).session(MilkSession.EVENING).productionDate(today).volumeLiters(7.0).milkTemperature(34.0).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(j9163).session(MilkSession.MORNING).productionDate(today).volumeLiters(9.0).milkTemperature(34.4).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(j9163).session(MilkSession.EVENING).productionDate(today).volumeLiters(6.5).milkTemperature(34.1).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(j0766).session(MilkSession.MORNING).productionDate(today).volumeLiters(9.5).milkTemperature(34.1).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(j0766).session(MilkSession.EVENING).productionDate(today).volumeLiters(6.5).milkTemperature(34.3).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(jh0027).session(MilkSession.MORNING).productionDate(today).volumeLiters(10.5).milkTemperature(34.2).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(jh0027).session(MilkSession.EVENING).productionDate(today).volumeLiters(7.5).milkTemperature(34.1).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(g001).session(MilkSession.MORNING).productionDate(today).volumeLiters(5.0).milkTemperature(34.0).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(g001).session(MilkSession.EVENING).productionDate(today).volumeLiters(3.5).milkTemperature(34.2).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(g004).session(MilkSession.MORNING).productionDate(today).volumeLiters(4.5).milkTemperature(34.2).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build(),
                MilkProduction.builder().animal(g004).session(MilkSession.EVENING).productionDate(today).volumeLiters(3.0).milkTemperature(34.1).destinationTank("Cuve Réfrigérée N°1 (Bio)").isOrganicCompliant(true).build()
        );
        milkProductionRepository.saveAll(productions);
    }

    private void initRealHealthData() {
        LocalDate today = LocalDate.now();
        Animal h1043 = animalRepository.findByInternalId("H-1043").orElse(null);
        Animal jm0033 = animalRepository.findByInternalId("JM-0033").orElse(null);

        List<HealthRecord> records = List.of(
                HealthRecord.builder()
                        .animal(h1043)
                        .recordDate(today.minusDays(14))
                        .actType("Visite Sanitaire Routine")
                        .diagnosis("Contrôle mamelle et aplombs conforme")
                        .treatmentPrescription("Aucun traitement nécessaire")
                        .practitionerName("Dr. Fall")
                        .costFcfa(10000.0)
                        .status("Clôturé")
                        .milkWithdrawalDays(0)
                        .build(),
                HealthRecord.builder()
                        .animal(jm0033)
                        .recordDate(today.minusDays(5))
                        .actType("Suivi Pédiatrique Veau")
                        .diagnosis("Pesée et contrôle ombilical normal")
                        .treatmentPrescription("Vitamines AD3E orales")
                        .practitionerName("Dr. Fall")
                        .costFcfa(7500.0)
                        .status("Clôturé")
                        .milkWithdrawalDays(0)
                        .build()
        );
        healthRecordRepository.saveAll(records);

        List<VaccineSchedule> vaccines = List.of(
                VaccineSchedule.builder()
                        .vaccineType("Rappel Fièvre Aphteuse")
                        .targetHerd("Tout le troupeau (18 têtes)")
                        .scheduledDate(today.plusDays(15))
                        .practitioner("Dr. Fall (Vétérinaire Ferme)")
                        .estimatedCost(36000.0)
                        .status("Planifié")
                        .notes("Campagne de vaccination annuelle préventive")
                        .build(),
                VaccineSchedule.builder()
                        .vaccineType("Péricardite Exsudative & Charbon")
                        .targetHerd("Génisses & Veaux (6 têtes)")
                        .scheduledDate(today.plusDays(30))
                        .practitioner("Dr. Fall")
                        .estimatedCost(18000.0)
                        .status("Planifié")
                        .notes("Immunisation jeunes bovins")
                        .build()
        );
        vaccineScheduleRepository.saveAll(vaccines);
    }

    private void initRealTransformationData() {
        Recipe r1 = Recipe.builder()
                .code("REC-01")
                .name("Lait Frais Entier Pasteurisé Bio 1L")
                .productType(ProductType.PASTEURIZED_MILK)
                .targetUnit("Bouteille 1L")
                .milkLitersPerUnit(1.0)
                .ingredientsList("100% Lait cru entier bio de la ferme")
                .shelfLifeDays(7)
                .processInstructions("Pasteurisation douce 72°C pendant 15 sec, refroidissement immédiat à 4°C.")
                .emoji("🥛")
                .standardSellingPriceFcfa(900.0)
                .build();

        Recipe r2 = Recipe.builder()
                .code("REC-02")
                .name("Yaourt Nature Brassé Bio 500g")
                .productType(ProductType.YOGURT)
                .targetUnit("Pot 500g")
                .milkLitersPerUnit(0.6)
                .ingredientsList("Lait pasteurisé bio, ferments lactiques vivants (L. bulgaricus, S. thermophilus)")
                .shelfLifeDays(21)
                .processInstructions("Ensemencement à 43°C, étuvage 6h, maturation à froid.")
                .emoji("🥣")
                .standardSellingPriceFcfa(1200.0)
                .build();

        Recipe r3 = Recipe.builder()
                .code("REC-03")
                .name("Fromage Blanc Fermier Bio 250g")
                .productType(ProductType.CHEESE)
                .targetUnit("Pot 250g")
                .milkLitersPerUnit(1.2)
                .ingredientsList("Lait cru bio, présure naturelle végétale, sel marin de Saloum")
                .shelfLifeDays(15)
                .processInstructions("Caillage lent, égouttage sur toile, salage léger.")
                .emoji("🧀")
                .standardSellingPriceFcfa(1800.0)
                .build();

        Recipe r4 = Recipe.builder()
                .code("REC-04")
                .name("Beurre Fermier Traditionnel 200g")
                .productType(ProductType.BUTTER)
                .targetUnit("Plaquette 200g")
                .milkLitersPerUnit(4.5)
                .ingredientsList("Crème de lait bio maturée, sel fin")
                .shelfLifeDays(30)
                .processInstructions("Écrémage, barattage traditionnel, lavage eau glacée et moulage.")
                .emoji("🧈")
                .standardSellingPriceFcfa(2200.0)
                .build();

        recipeRepository.saveAll(List.of(r1, r2, r3, r4));

        LocalDate today = LocalDate.now();
        List<ProductStock> stocks = List.of(
                ProductStock.builder().recipe(r1).productName(r1.getName()).quantityAvailable(80.0).unit("Bouteille 1L").unitPriceFcfa(900.0).totalValueFcfa(72000.0).mfgDate(today).dlcExpiryDate(today.plusDays(7)).storageLocation("Chambre Froide Laiterie (+4°C)").isOrganicCertified(true).build(),
                ProductStock.builder().recipe(r2).productName(r2.getName()).quantityAvailable(45.0).unit("Pot 500g").unitPriceFcfa(1200.0).totalValueFcfa(54000.0).mfgDate(today).dlcExpiryDate(today.plusDays(21)).storageLocation("Chambre Froide (+4°C)").isOrganicCertified(true).build(),
                ProductStock.builder().recipe(r3).productName(r3.getName()).quantityAvailable(30.0).unit("Pot 250g").unitPriceFcfa(1800.0).totalValueFcfa(54000.0).mfgDate(today).dlcExpiryDate(today.plusDays(15)).storageLocation("Chambre Froide (+4°C)").isOrganicCertified(true).build()
        );
        productStockRepository.saveAll(stocks);
    }

    private void initRealCommercialData() {
        Customer c1 = new Customer("Supermarché Exclusive Dakar", "Exclusive Market SAS", CustomerType.SUPERMARKET, "+221 77 540 12 34", "achats@exclusive-dakar.sn", "Almadies, Route de Ngor", "Dakar", "00489123-2B2");
        c1.setTotalOrdersCount(1);
        c1.setTotalSpentFcfa(125000.0);
        c1.setBalanceDueFcfa(0.0);

        Customer c2 = new Customer("Hôtel Teranga & Spa", "Teranga Hospitality", CustomerType.HOTEL_RESTAURANT, "+221 33 821 00 90", "chef@teranga-hotel.sn", "Plateau, Place de l'Indépendance", "Dakar", "00981244-1A3");
        c2.setTotalOrdersCount(1);
        c2.setTotalSpentFcfa(85000.0);
        c2.setBalanceDueFcfa(85000.0);

        customerRepository.saveAll(List.of(c1, c2));

        LocalDate today = LocalDate.now();
        SaleInvoice inv1 = new SaleInvoice();
        inv1.setInvoiceNumber("FAC-2026-0001");
        inv1.setCustomer(c1);
        inv1.setIssueDate(today.minusDays(3));
        inv1.setDueDate(today.plusDays(27));
        inv1.setSubTotalFcfa(125000.0);
        inv1.setDiscountFcfa(0.0);
        inv1.setTaxFcfa(0.0);
        inv1.setTotalAmountFcfa(125000.0);
        inv1.setPaidAmountFcfa(125000.0);
        inv1.setRemainingAmountFcfa(0.0);
        inv1.setStatus(InvoiceStatus.PAID);
        inv1.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        inv1.setPaymentReference("VIR-BOA-882190");
        inv1.setNotes("Livraison hebdomadaire produits bio");

        InvoiceItem item1 = new InvoiceItem();
        item1.setInvoice(inv1);
        item1.setProductName("Lait Frais Entier Pasteurisé Bio 1L");
        item1.setProductType(ProductType.PASTEURIZED_MILK);
        item1.setQuantity(100.0);
        item1.setUnit("Bouteille 1L");
        item1.setUnitPriceFcfa(900.0);
        item1.setLineTotalFcfa(90000.0);

        InvoiceItem item2 = new InvoiceItem();
        item2.setInvoice(inv1);
        item2.setProductName("Yaourt Nature Brassé Bio 500g");
        item2.setProductType(ProductType.YOGURT);
        item2.setQuantity(25.0);
        item2.setUnit("Pot 500g");
        item2.setUnitPriceFcfa(1400.0);
        item2.setLineTotalFcfa(35000.0);

        inv1.setItems(List.of(item1, item2));
        saleInvoiceRepository.save(inv1);
    }

    private void initRealFeedAndSolarData() {
        List<FeedStock> feeds = List.of(
                new FeedStock("Fourrage Vert (Sorgho & Luzerne)", "FORAGE_GREEN", 1800.0, 500.0, 60.0, "GIE Agro-Sénégal", "Silo Fourrage N°1", "Riche en matière verte"),
                new FeedStock("Ensilage de Maïs Bio", "FORAGE_GREEN", 3200.0, 800.0, 85.0, "Production Propre Ferme", "Silo N°2", "Énergie et appétence"),
                new FeedStock("Tourteau de Coton & Arachide", "CONCENTRATE", 950.0, 300.0, 240.0, "Huilerie Suneor Diourbel", "Hangar Concentrés", "Protéines 42%"),
                new FeedStock("Foin Sec d'Arachide & Herbe", "FORAGE_DRY", 1400.0, 400.0, 90.0, "Producteurs Thiès", "Hangar Paille", "Fibres longues digestibles"),
                new FeedStock("Minéraux, Sel & CMV Lactation", "MINERALS_VITAMINS", 280.0, 80.0, 550.0, "VetAgro Dakar", "Pharmacie Vétérinaire", "Complément Oligo-éléments")
        );
        feedStockRepository.saveAll(feeds);

        List<FeedRation> rations = List.of(
                new FeedRation("Ration Vaches Haute Lactation (Holstein/Montbéliarde)", "Vaches en Production", 16.5, "8kg Fourrage Vert + 5kg Ensilage + 3kg Tourteau + 200g CMV", 2850.0, 14.2, 1350.0),
                new FeedRation("Ration Vaches Gestantes & Gobra", "Gestantes / Gobra", 12.0, "6kg Fourrage Vert + 4kg Foin + 1.5kg Tourteau + 100g Minéraux", 1800.0, 10.5, 950.0),
                new FeedRation("Ration Jeunes Bovins & Croissance", "Génisses & Taillons", 7.5, "4kg Fourrage Tendre + 2kg Foin + 1kg Concentré Croissance", 1250.0, 8.0, 750.0)
        );
        feedRationRepository.saveAll(rations);
    }

    private void initRealSuppliersData() {
        List<Supplier> suppliers = List.of(
                new Supplier("GIE Agro-Sénégal", "GIE Agro-Sénégal Niayes", "Mamadou Diallo", "+221 77 632 11 00", "contact@agro-senegal.sn", "Zone Maraîchère des Niayes", "Thiès", "FOURRAGE_ALIMENT", "Comptant / Wave", true),
                new Supplier("Cabinet Vétérinaire Dr. Fall", "Cabinet VetFall Dakar", "Dr. Ousmane Fall", "+221 77 450 88 99", "dr.fall@vetfall.sn", "VDN Cité Keur Gorgui", "Dakar", "VETERINAIRE_SANTE", "Fin de mois / Virement", true),
                new Supplier("Packaging & Bouteilles Bio SN", "Emballages du Sénégal", "Awa Ndiaye", "+221 33 832 44 55", "commercial@emballages-sn.com", "Zone Industrielle Dakar", "Dakar", "EMBALLAGE_PACKAGING", "30 jours", true)
        );
        supplierRepository.saveAll(suppliers);
    }
}
