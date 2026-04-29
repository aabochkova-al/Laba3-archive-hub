/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.service;


import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import mission.laba3.entity.*;
import mission.laba3.model.*;
import mission.laba3.parsers.*;
import mission.laba3.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author aleksandra
 */
@Service
public class MissionService {
    @Autowired
    private MissionRepository missionRepository;
    
    @Transactional
    public MissionEntity uploadMission(MultipartFile file) throws IOException {
        // Сохраняем загруженный файл во временную папку
        Path tempDir = Files.createTempDirectory("missions");
        String originalFilename = file.getOriginalFilename();
        File tempFile = tempDir.resolve(originalFilename != null ? originalFilename : "mission").toFile();
        file.transferTo(tempFile);
 
        try {
            ParserFactory.setup();
            MissionParser parser = ParserFactory.getParser(tempFile.getAbsolutePath());
            MissionBuilder builder = new MissionBuilder();
            Mission mission = parser.parse(builder, tempFile.getAbsolutePath());
 
            new MissionValidator().validate(mission);
 
            return saveMission(mission);
        } finally {
            tempFile.delete();
        }
    }
    
        @Transactional
    public MissionEntity saveMission(Mission mission) {
        // Если миссия с таким ID уже существует, обновляем её
        Optional<MissionEntity> existing = missionRepository.findByMissionId(mission.getMissionId());
        MissionEntity entity = existing.orElse(new MissionEntity());
 
        // Oсновные поля
        entity.setMissionId(mission.getMissionId());
        entity.setDate(mission.getDate());
        entity.setLocation(mission.getLocation());
        entity.setNotes(mission.getNotes());
 
        if (mission.getDamageCost() != null) {
            entity.setDamageCost(mission.getDamageCost());
        }
 

        if (mission.getOutcome() != null) {
            entity.setOutcome(mission.getOutcome());
        }

        if (mission.getCurse() != null) {
            CurseEntity curseEntity = entity.getCurse() != null ? entity.getCurse() : new CurseEntity();
            curseEntity.setName(mission.getCurse().getName());
            curseEntity.setThreatLevel(mission.getCurse().getThreatLevel());
            entity.setCurse(curseEntity);
        }
 
        entity.getSorcerers().clear();
        if (mission.getSorcerers() != null) {
            for (Sorcer s : mission.getSorcerers()) {
                SorcererEntity se = new SorcererEntity();
                se.setName(s.getName());
                se.setRank(s.getRank());
                se.setMission(entity);
                entity.getSorcerers().add(se);
            }
        }

        entity.getTechniques().clear();
        if (mission.getTechniques() != null) {
            for (Technique t : mission.getTechniques()) {
                TechniqueEntity te = new TechniqueEntity();
                te.setName(t.getName());
                te.setType(t.getType());
                te.setOwner(t.getOwner());
                te.setDamage(t.getDamage());
                te.setMission(entity);
                entity.getTechniques().add(te);
            }
        }
 
        if (mission.getEconomicAssessment() != null) {
            EconomicAssessmentEntity ea = entity.getEconomicAssessment() != null
                    ? entity.getEconomicAssessment()
                    : new EconomicAssessmentEntity();
            EconomicAssessment src = mission.getEconomicAssessment();
            ea.setTotalDamageCost(src.getTotalDamageCost());
            ea.setInfrastructureDamage(src.getInfrastructureDamage());
            ea.setCommercialDamage(src.getCommercialDamage());
            ea.setTransportDamage(src.getTransportDamage());
            ea.setRecoveryEstimateDays(src.getRecoveryEstimateDays());
            ea.setInsuranceCovered(src.getInsuranceCovered());
            entity.setEconomicAssessment(ea);
        }

        if (mission.getEnvironment() != null) {
            EnvironmentEntity env = entity.getEnvironment() != null
                    ? entity.getEnvironment()
                    : new EnvironmentEntity();
            EnvironmentConditions src = mission.getEnvironment();
            if (src.getWeather() != null)
                env.setWeather(src.getWeather());
            if (src.getTimeOfDay() != null)
                env.setTimeOfDay(src.getTimeOfDay());
            if (src.getVisibility() != null)
                env.setVisibility(src.getVisibility());
            env.setCursedEnergyDensity(src.getCursedEnergyDensity());
            entity.setEnvironment(env);
        }
 
        // Сохраняем в базу данных
        return missionRepository.save(entity);
    }
 
    // Получить все миссии из БД
    public List<MissionEntity> getAllMissions() {
        return missionRepository.findAllByOrderByDateDesc();
    }
 
    // Получить миссию по внутреннему ID
    public Optional<MissionEntity> getMissionById(Long id) {
        return missionRepository.findById(id);
    }
 
    // Удалить миссию
    @Transactional
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }
    
}
