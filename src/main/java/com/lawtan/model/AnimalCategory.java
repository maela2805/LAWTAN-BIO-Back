package com.lawtan.model;

public enum AnimalCategory {
    MILKING_COW("Vache Laitière en Production"),
    HEIFER_YOUNG("Génisse / Jeune"),
    MALE_BULL("Mâle / Taureau Reproducteur");

    private final String label;

    AnimalCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
