package com.lawtan.model;

public enum ReproEventType {
    HEAT_DETECTION("Détection de Chaleurs"),
    ARTIFICIAL_INSEMINATION("Insémination Artificielle (IA)"),
    NATURAL_MATING("Saillie Naturelle"),
    PREGNANCY_DIAGNOSIS("Diagnostic Gestation (Échographie)"),
    CALVING("Enregistrement Vêlage"),
    DRY_OFF("Mise au Tarissement");

    private final String label;

    ReproEventType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
