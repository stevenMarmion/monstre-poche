package com.esiea.monstre.poche.models.combats;

import java.util.ArrayList;
import java.util.List;

/**
 * Logger pour capturer tous les événements du combat.
 */
public class CombatLogger {
    private static final List<String> logs = new ArrayList<>();
    
    public static void log(String message) {
        logs.add(message);
        System.out.println(message);
    }
    
    public static List<String> getLogs() {
        return new ArrayList<>(logs);
    }
    
    public static void clear() {
        logs.clear();
    }
    
    public static String getFormattedLogs() {
        return String.join("\n", logs);
    }
}
