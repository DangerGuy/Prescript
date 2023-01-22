package com.example.prescript;
import java.util.*;

public class MedicationsDatabase {

    public static class NoMedicationFoundException extends Exception {}

    public final Set<MainActivity.Medication> ALL_MEDS;

    public MedicationsDatabase() {
        ALL_MEDS = new HashSet<>();
        ALL_MEDS.add(new MainActivity.Medication("Hydrophil", "9:00 a.m. daily", true,
                "a truly competent drug that will make you fly", Arrays.asList("Vitamin C", "Stubbilone")));
        ALL_MEDS.add(new MainActivity.Medication("Polybene", "12:00 p.m. once every 2 days", false,
                "a medicine that makes you grow 4 legs", Arrays.asList("Banetane")));
        ALL_MEDS.add(new MainActivity.Medication("Banetane", "once every week at 4:00 p.m.", false,
                "this medication makes you invincible!", Arrays.asList("Polybene")));
        ALL_MEDS.add(new MainActivity.Medication("Heptaforis", "every day at 9 a.m. and 9 p.m.", true,
                "makes you live forever", Arrays.asList("Stubbilone")));
        ALL_MEDS.add(new MainActivity.Medication("Lethlisen", "twice a day, at any time", false,
                "makes you invisible", Arrays.asList()));
        ALL_MEDS.add(new MainActivity.Medication("Vitamin C", "once daily at 12 p.m.", false,
                "makes you healthier", Arrays.asList("Hydrophil")));
        ALL_MEDS.add(new MainActivity.Medication("Stubbilone", "once a year", true,
                "makes you dead", Arrays.asList("Heptaforis", "Hydrophil")));
        ALL_MEDS.add(new MainActivity.Medication("Pill", "any time", false,
                "it's a random pill filled with water", Arrays.asList()));
    }

    public MainActivity.Medication findMedication(String medName) throws NoMedicationFoundException {
        return MainActivity.getMedication(ALL_MEDS, medName);
    }

}
