/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.mapper;

import mission.laba3.dto.MissionDTO;
import mission.laba3.entity.CurseEntity;
import mission.laba3.entity.EconomicAssessmentEntity;
import mission.laba3.entity.EnvironmentEntity;
import mission.laba3.entity.MissionEntity;
import mission.laba3.entity.SorcererEntity;
import mission.laba3.entity.TechniqueEntity;
import mission.laba3.model.Curse;
import mission.laba3.model.EconomicAssessment;
import mission.laba3.model.EnvironmentConditions;
import mission.laba3.model.Mission;
import mission.laba3.model.Sorcer;
import mission.laba3.model.Technique;
import org.springframework.stereotype.Component;

/**
 *
 * @author aleksandra
 */
@Component
public class Mapper {
    //Mission в MissionEntity
    public MissionEntity toEntity(Mission mission) {
        if (mission == null) return null;
        MissionEntity entity = new MissionEntity();
        copyToEntity(mission, entity);
        return entity;
    }
    
    public void updateEntity(MissionEntity entity, Mission mission) {
        if (entity == null || mission == null) return;
        copyToEntity(mission, entity);
    }
    
    private void copyToEntity(Mission mission, MissionEntity entity) {
        entity.setMissionId(mission.getMissionId());
        entity.setDate(mission.getDate());
        entity.setLocation(mission.getLocation());
        entity.setOutcome(mission.getOutcome());
        entity.setDamageCost(mission.getDamageCost());
        entity.setNotes(mission.getNotes());

        // Curse
        if (mission.getCurse() != null) {
            CurseEntity curseEntity = entity.getCurse() != null ? entity.getCurse() : new CurseEntity();
            curseEntity.setName(mission.getCurse().getName());
            curseEntity.setThreatLevel(mission.getCurse().getThreatLevel());
            entity.setCurse(curseEntity);
        }

        // Sorcerers
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

        // Techniques
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

        // EconomicAssessment
        if (mission.getEconomicAssessment() != null) {
            EconomicAssessmentEntity ea = entity.getEconomicAssessment() != null
                ? entity.getEconomicAssessment() : new EconomicAssessmentEntity();
            EconomicAssessment src = mission.getEconomicAssessment();
            ea.setTotalDamageCost(src.getTotalDamageCost());
            ea.setInfrastructureDamage(src.getInfrastructureDamage());
            ea.setCommercialDamage(src.getCommercialDamage());
            ea.setTransportDamage(src.getTransportDamage());
            ea.setRecoveryEstimateDays(src.getRecoveryEstimateDays());
            ea.setInsuranceCovered(src.getInsuranceCovered());
            entity.setEconomicAssessment(ea);
        }

        // Environment
        if (mission.getEnvironment() != null) {
            EnvironmentEntity env = entity.getEnvironment() != null
                ? entity.getEnvironment() : new EnvironmentEntity();
            EnvironmentConditions src = mission.getEnvironment();
            env.setWeather(src.getWeather());
            env.setTimeOfDay(src.getTimeOfDay());
            env.setVisibility(src.getVisibility());
            env.setCursedEnergyDensity(src.getCursedEnergyDensity());
            entity.setEnvironment(env);
        }
    }
    
    //MissionEntity в Mission
        public Mission toLegacyMission(MissionEntity entity) {
        if (entity == null) return null;
        
        Mission mission = new Mission();
        mission.setMissionId(entity.getMissionId());
        mission.setDate(entity.getDate());
        mission.setLocation(entity.getLocation());
        mission.setOutcome(entity.getOutcome());
        mission.setDamageCost(entity.getDamageCost());
        mission.setNotes(entity.getNotes());
        
        // Curse
        if (entity.getCurse() != null) {
            Curse curse = new Curse();
            curse.setName(entity.getCurse().getName());
            curse.setThreatLevel(entity.getCurse().getThreatLevel());
            mission.setCurse(curse);
        }
        
        // Sorcerers
        if (entity.getSorcerers() != null) {
            for (SorcererEntity se : entity.getSorcerers()) {
                Sorcer sorcer = new Sorcer();
                sorcer.setName(se.getName());
                sorcer.setRank(se.getRank());
                mission.getSorcerers().add(sorcer);
            }
        }
        
        // Techniques
        if (entity.getTechniques() != null) {
            for (TechniqueEntity te : entity.getTechniques()) {
                Technique technique = new Technique();
                technique.setName(te.getName());
                technique.setType(te.getType());
                technique.setOwner(te.getOwner());
                technique.setDamage(te.getDamage());
                mission.getTechniques().add(technique);
            }
        }
        
        // EconomicAssessment
        if (entity.getEconomicAssessment() != null) {
            EconomicAssessment ea = new EconomicAssessment();
            ea.setTotalDamageCost(entity.getEconomicAssessment().getTotalDamageCost());
            ea.setInfrastructureDamage(entity.getEconomicAssessment().getInfrastructureDamage());
            ea.setCommercialDamage(entity.getEconomicAssessment().getCommercialDamage());
            ea.setTransportDamage(entity.getEconomicAssessment().getTransportDamage());
            ea.setRecoveryEstimateDays(entity.getEconomicAssessment().getRecoveryEstimateDays());
            ea.setInsuranceCovered(entity.getEconomicAssessment().getInsuranceCovered());
            mission.setEconomicAssessment(ea);
        }
        
        // Environment
        if (entity.getEnvironment() != null) {
            EnvironmentConditions env = new EnvironmentConditions();
            env.setWeather(entity.getEnvironment().getWeather());
            env.setTimeOfDay(entity.getEnvironment().getTimeOfDay());
            env.setVisibility(entity.getEnvironment().getVisibility());
            env.setCursedEnergyDensity(entity.getEnvironment().getCursedEnergyDensity());
            mission.setEnvironment(env);
        }
        
        return mission;
    }
    public MissionDTO toMissionDTO(MissionEntity entity) {
        if (entity == null) return null;

        MissionDTO dto = new MissionDTO();
        dto.setId(entity.getId());
        dto.setMissionId(entity.getMissionId());
        dto.setDate(entity.getDate());
        dto.setLocation(entity.getLocation());
        dto.setOutcome(entity.getOutcome() != null ? entity.getOutcome().name() : null);
        dto.setDamageCost(entity.getDamageCost());

        // Curse
        if (entity.getCurse() != null) {
            dto.setCurseName(entity.getCurse().getName());
            dto.setCurseThreatLevel(
                entity.getCurse().getThreatLevel() != null 
                    ? entity.getCurse().getThreatLevel().name() 
                    : null
            );
        }

        // Sorcerers (только имена)
        if (entity.getSorcerers() != null) {
            for (SorcererEntity se : entity.getSorcerers()) {
                if (se.getName() != null) {
                    dto.getSorcerers().add(se.getName());
                }
            }
        }

        // Techniques (только названия)
        if (entity.getTechniques() != null) {
            for (TechniqueEntity te : entity.getTechniques()) {
                if (te.getName() != null) {
                    dto.getTechniques().add(te.getName());
                }
            }
        }

        return dto;
    }
}
