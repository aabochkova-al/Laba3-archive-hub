/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 *
 * @author aleksandra
 */
public class TxtParser extends BasicParser{
     @Override
     protected Map<String, Object> parseToMap(String filepath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filepath))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }
       
        boolean hasSectionBrackets = lines.stream().anyMatch(l -> l.trim().startsWith("[") && l.trim().endsWith("]"));
        
        if (hasSectionBrackets) {
            return parseSectionFormatImproved(lines);
        }
        
        boolean hasColonFormat = lines.stream().anyMatch(l -> l.contains(":") && !l.trim().startsWith("["));
        if (hasColonFormat) {
            return parseColonFormat(lines);
        }
        return parseColonFormat(lines);
    }
    
    private Map<String, Object> parseSectionFormatImproved(List<String> lines) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, String>> sorcerersList = new ArrayList<>();
        List<Map<String, Object>> techniquesList = new ArrayList<>();
        
        String currentSection = null;
        Map<String, String> currentSorcerer = null;
        Map<String, Object> currentTechnique = null;
        Map<String, String> currentCurse = null;
        Map<String, Object> currentEnvironment = null;
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            
            if (line.startsWith("[") && line.endsWith("]")) {
                String sectionName = line.substring(1, line.length() - 1).toUpperCase();
                currentSection = sectionName;
                
                if ("SORCERER".equals(sectionName)) {
                    if (currentSorcerer != null && !currentSorcerer.isEmpty()) {
                        sorcerersList.add(currentSorcerer);
                    }
                    currentSorcerer = new HashMap<>();
                } else if ("TECHNIQUE".equals(sectionName)) {
                    if (currentTechnique != null && !currentTechnique.isEmpty()) {
                        techniquesList.add(currentTechnique);
                    }
                    currentTechnique = new HashMap<>();
                } else if ("CURSE".equals(sectionName)) {
                    currentCurse = new HashMap<>();
                } else if ("ENVIRONMENT".equals(sectionName)) {
                    currentEnvironment = new HashMap<>();
                }
                continue;
            }
            
            int eqIndex = line.indexOf('=');
            if (eqIndex <= 0) continue;
            
            String key = line.substring(0, eqIndex).trim();
            String value = line.substring(eqIndex + 1).trim();
            
            if ("MISSION".equals(currentSection)) {
                switch (key) {
                    case "missionId": result.put("missionId", value); break;
                    case "date": result.put("date", value); break;
                    case "location": result.put("location", value); break;
                    case "outcome": result.put("outcome", value); break;
                    case "damageCost":
                        try { result.put("damageCost", Integer.parseInt(value)); }
                        catch (NumberFormatException e) { result.put("damageCost", value); }
                        break;
                    case "notes": result.put("notes", value); break;
                }
            } else if ("CURSE".equals(currentSection) && currentCurse != null) {
                currentCurse.put(key, value);
            } else if ("SORCERER".equals(currentSection) && currentSorcerer != null) {
                currentSorcerer.put(key, value);
            } else if ("TECHNIQUE".equals(currentSection) && currentTechnique != null) {
                if ("damage".equals(key)) {
                    try { currentTechnique.put("damage", Integer.parseInt(value)); }
                    catch (NumberFormatException e) { currentTechnique.put("damage", value); }
                } else {
                    currentTechnique.put(key, value);
                }
            } else if ("ENVIRONMENT".equals(currentSection) && currentEnvironment != null) {
                if ("cursedEnergyDensity".equals(key)) {
                    try { currentEnvironment.put("cursedEnergyDensity", Integer.parseInt(value)); }
                    catch (NumberFormatException e) { currentEnvironment.put("cursedEnergyDensity", value); }
                } else {
                    currentEnvironment.put(key, value);
                }
            }
        }

        if (currentSorcerer != null && !currentSorcerer.isEmpty()) {
            sorcerersList.add(currentSorcerer);
        }
        if (currentTechnique != null && !currentTechnique.isEmpty()) {
            techniquesList.add(currentTechnique);
        }

        if (currentCurse != null && !currentCurse.isEmpty()) {
            result.put("curse", currentCurse);
        }
        
        if (!sorcerersList.isEmpty()) {
            result.put("sorcerers", sorcerersList);
        }
        
        if (!techniquesList.isEmpty()) {
            result.put("techniques", techniquesList);
        }
        
        if (currentEnvironment != null && !currentEnvironment.isEmpty()) {
            result.put("environment", currentEnvironment);
        }
        
        return result;
    }

    private Map<String, Object> parseColonFormat(List<String> lines) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> flatData = new HashMap<>();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            
            int colonIndex = line.indexOf(':');
            if (colonIndex <= 0) continue;
            
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();
            if (!value.isEmpty()) {
                flatData.put(key, value);
            }
        }
        
        // Основные поля
        if (flatData.containsKey("missionId")) result.put("missionId", flatData.get("missionId"));
        if (flatData.containsKey("date")) result.put("date", flatData.get("date"));
        if (flatData.containsKey("location")) result.put("location", flatData.get("location"));
        if (flatData.containsKey("outcome")) result.put("outcome", flatData.get("outcome"));
        if (flatData.containsKey("damageCost")) {
            try { result.put("damageCost", Integer.parseInt(flatData.get("damageCost"))); }
            catch (NumberFormatException e) {}
        }
        if (flatData.containsKey("comment")) result.put("notes", flatData.get("comment"));
        if (flatData.containsKey("note")) result.put("notes", flatData.get("note"));
        
        // Проклятие
        if (flatData.containsKey("curse.name") || flatData.containsKey("curse.threatLevel")) {
            Map<String, String> curse = new HashMap<>();
            if (flatData.containsKey("curse.name")) curse.put("name", flatData.get("curse.name"));
            if (flatData.containsKey("curse.threatLevel")) curse.put("threatLevel", flatData.get("curse.threatLevel"));
            result.put("curse", curse);
        }
        
        // Маги
        List<Map<String, String>> sorcerers = new ArrayList<>();
        int i = 0;
        while (flatData.containsKey("sorcerer[" + i + "].name")) {
            Map<String, String> s = new HashMap<>();
            s.put("name", flatData.get("sorcerer[" + i + "].name"));
            if (flatData.containsKey("sorcerer[" + i + "].rank"))
                s.put("rank", flatData.get("sorcerer[" + i + "].rank"));
            sorcerers.add(s);
            i++;
        }
        if (!sorcerers.isEmpty()) result.put("sorcerers", sorcerers);
        
        // Техники
        List<Map<String, Object>> techniques = new ArrayList<>();
        int j = 0;
        while (flatData.containsKey("technique[" + j + "].name")) {
            Map<String, Object> t = new HashMap<>();
            t.put("name", flatData.get("technique[" + j + "].name"));
            if (flatData.containsKey("technique[" + j + "].type"))
                t.put("type", flatData.get("technique[" + j + "].type"));
            if (flatData.containsKey("technique[" + j + "].owner"))
                t.put("owner", flatData.get("technique[" + j + "].owner"));
            if (flatData.containsKey("technique[" + j + "].damage")) {
                try { t.put("damage", Integer.parseInt(flatData.get("technique[" + j + "].damage"))); }
                catch (NumberFormatException e) {}
            }
            techniques.add(t);
            j++;
        }
        if (!techniques.isEmpty()) result.put("techniques", techniques);
        
        return result;
    }
 
    @Override
    public String getExtension() {
        return "txt";
    }
    
}
