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
        // Читаем файл
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filepath))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }
 
        // Определяем формат файла
        boolean hasSections = lines.stream().anyMatch(l -> l.trim().startsWith("[") && l.trim().endsWith("]"));
        boolean hasColonFormat = lines.stream().anyMatch(l -> l.contains(":") && !l.trim().startsWith("["));
        boolean hasEqualsFormat = lines.stream().anyMatch(l -> l.contains("=") && !l.trim().startsWith("#"));
 
        if (hasSections && hasEqualsFormat) {
 
            return convertSectionFormat(parseSectionFormat(lines));
        } else if (hasColonFormat && !hasSections) {

            return parseColonFormat(lines);
        } else if (hasEqualsFormat) {

            return convertSectionFormat(parseSectionFormat(lines));
        }
 
        throw new IOException("Неизвестный формат TXT файла");
    }
 

    private Map<String, String> parseSectionFormat(List<String> lines) {
        Map<String, String> data = new HashMap<>();
        String currentSection = "";
        Map<String, Integer> sectionCounters = new HashMap<>();
 
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
 
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1).toLowerCase();
                sectionCounters.put(currentSection, sectionCounters.getOrDefault(currentSection, -1) + 1);
                continue;
            }
 
            int eqIndex = line.indexOf('=');
            if (eqIndex > 0) {
                String key = line.substring(0, eqIndex).trim();
                String value = line.substring(eqIndex + 1).trim();
 
                if (!currentSection.isEmpty() && !currentSection.equals("mission")) {
                    int counter = sectionCounters.getOrDefault(currentSection, -1);
                    key = currentSection + "[" + counter + "]." + key;
                }
                data.put(key, value);
            }
        }
        return data;
    }
       private Map<String, Object> parseColonFormat(List<String> lines) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> flatData = new HashMap<>();
 
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
 
            // Ищем первое двоеточие
            int colonIndex = line.indexOf(':');
            if (colonIndex <= 0) continue;
 
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();
 
            if (!value.isEmpty()) {
                flatData.put(key, value);
            }
        }
 
        // Конвертируем плоскую карту в структурированную
        // Основные поля
        if (flatData.containsKey("missionId")) result.put("missionId", flatData.get("missionId"));
        if (flatData.containsKey("date")) result.put("date", flatData.get("date"));
        if (flatData.containsKey("location")) result.put("location", flatData.get("location"));
        if (flatData.containsKey("outcome")) result.put("outcome", flatData.get("outcome"));
        if (flatData.containsKey("damageCost")) {
            try { result.put("damageCost", Integer.parseInt(flatData.get("damageCost"))); }
            catch (NumberFormatException ignored) {}
        }
 
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
                catch (NumberFormatException ignored) {}
            }
            techniques.add(t);
            j++;
        }
        if (!techniques.isEmpty()) result.put("techniques", techniques);
 
        return result;
    }

    private Map<String, Object> convertSectionFormat(Map<String, String> firstData) {
        Map<String, Object> result = new HashMap<>();
 
        if (firstData.containsKey("missionId")) result.put("missionId", firstData.get("missionId"));
        if (firstData.containsKey("date")) result.put("date", firstData.get("date"));
        if (firstData.containsKey("location")) result.put("location", firstData.get("location"));
        if (firstData.containsKey("outcome")) result.put("outcome", firstData.get("outcome"));
        if (firstData.containsKey("damageCost")) {
            try { result.put("damageCost", Integer.parseInt(firstData.get("damageCost"))); }
            catch (NumberFormatException ignored) {}
        }
 
        if (firstData.containsKey("curse[0].name") || firstData.containsKey("curse[0].threatLevel")) {
            Map<String, String> curse = new HashMap<>();
            if (firstData.containsKey("curse[0].name")) curse.put("name", firstData.get("curse[0].name"));
            if (firstData.containsKey("curse[0].threatLevel")) curse.put("threatLevel", firstData.get("curse[0].threatLevel"));
            result.put("curse", curse);
        }
 
        List<Map<String, String>> sorcerers = new ArrayList<>();
        int si = 0;
        while (firstData.containsKey("sorcerer[" + si + "].name")) {
            Map<String, String> s = new HashMap<>();
            s.put("name", firstData.get("sorcerer[" + si + "].name"));
            if (firstData.containsKey("sorcerer[" + si + "].rank"))
                s.put("rank", firstData.get("sorcerer[" + si + "].rank"));
            sorcerers.add(s);
            si++;
        }
        if (!sorcerers.isEmpty()) result.put("sorcerers", sorcerers);
 
        List<Map<String, Object>> techniques = new ArrayList<>();
        int ti = 0;
        while (firstData.containsKey("technique[" + ti + "].name")) {
            Map<String, Object> t = new HashMap<>();
            t.put("name", firstData.get("technique[" + ti + "].name"));
            if (firstData.containsKey("technique[" + ti + "].type"))
                t.put("type", firstData.get("technique[" + ti + "].type"));
            if (firstData.containsKey("technique[" + ti + "].owner"))
                t.put("owner", firstData.get("technique[" + ti + "].owner"));
            if (firstData.containsKey("technique[" + ti + "].damage")) {
                try { t.put("damage", Integer.parseInt(firstData.get("technique[" + ti + "].damage"))); }
                catch (NumberFormatException ignored) {}
            }
            techniques.add(t);
            ti++;
        }
        if (!techniques.isEmpty()) result.put("techniques", techniques);
 
        if (firstData.containsKey("environment[0].weather") || firstData.containsKey("environment[0].timeOfDay")) {
            Map<String, Object> env = new HashMap<>();
            if (firstData.containsKey("environment[0].weather")) env.put("weather", firstData.get("environment[0].weather"));
            if (firstData.containsKey("environment[0].timeOfDay")) env.put("timeOfDay", firstData.get("environment[0].timeOfDay"));
            if (firstData.containsKey("environment[0].visibility")) env.put("visibility", firstData.get("environment[0].visibility"));
            if (firstData.containsKey("environment[0].cursedEnergyDensity"))
                env.put("cursedEnergyDensity", firstData.get("environment[0].cursedEnergyDensity"));
            result.put("environment", env);
        }
 
        return result;
    }
 
    @Override
    public String getExtension() {
        return "txt";
    }
    
}
