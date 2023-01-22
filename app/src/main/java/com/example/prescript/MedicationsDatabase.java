package com.example.prescript;
import java.util.*;
import java.util.jar.Manifest;

public class MedicationsDatabase {

    public static class NoMedicationFoundException extends Exception {}

    public final Set<MainActivity.Medication> ALL_MEDS;

    public MedicationsDatabase() {
        ALL_MEDS = new HashSet<>();
        ALL_MEDS.add(new MainActivity.Medication("Ventolin", "9:00 a.m., 2 puffs per hour", true,
                "used to prevent wheezing and shortness of breath", Arrays.asList()));
        ALL_MEDS.add(new MainActivity.Medication("Valium", "9:30 a.m., 2 milligrams", true,
                "used to treat anxiety, alcohol withdrawal, and seizures", Arrays.asList("Alcohol")));
        ALL_MEDS.add(new MainActivity.Medication("Creatine", "2x a day, any time", false,
                "supplement used by athletes to improve their performance", Arrays.asList("Caffeine")));
        ALL_MEDS.add(new MainActivity.Medication("Vitamin C", "once daily at 12 p.m.", false,
                "strengthens your immune system", Arrays.asList("")));
        ALL_MEDS.add(new MainActivity.Medication("Pill", "whenever you feel crazy", false,
                "it's a random pill filled with water", Arrays.asList()));
        ALL_MEDS.add(new MainActivity.Medication("Alcohol", "any time, any day", false,
                "makes you feel good", Arrays.asList("Valium")));
        ALL_MEDS.add(new MainActivity.Medication("Caffeine", "any time, any day", false,
                "makes you awake", Arrays.asList("Creatine")));
    }

    public MainActivity.Medication findMedication(String medName) throws NoMedicationFoundException {
        return MainActivity.getMedication(ALL_MEDS, medName);
    }

}
