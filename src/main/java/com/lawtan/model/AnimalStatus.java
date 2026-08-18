package com.lawtan.model;

public enum AnimalStatus {
    EXCELLENT("Excellente"),
    HEALTHY("Sain"),
    FEVER_TREATMENT("Fièvre / En Soin"),
    PREGNANT("Gestante"),
    IN_HEAT("Chaleurs"),
    DRY_OFF("Tarie"),
    GROWTH("En Croissance"),
    BREEDER_BULL("Reproducteur A+");

    private final String label;

    AnimalStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
