/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import mission.laba3.enums.Outcome;

/**
 *
 * @author aleksandra
 */
@Entity
@Table(name = "missions")
public class MissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "mission_id", unique = true)
    private String missionId;
 
    @Column(name = "date")
    private String date;
 
    @Column(name = "location")
    private String location;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "outcome")
    private Outcome outcome;
 
    @Column(name = "damage_cost")
    private Integer damageCost;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curse_id")
    private CurseEntity curse;
    
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SorcererEntity> sorcerers = new ArrayList<>();
 
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechniqueEntity> techniques = new ArrayList<>();
 
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "economic_assessment_id")
    private EconomicAssessmentEntity economicAssessment;
 
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "environment_id")
    private EnvironmentEntity environment;
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }
 
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
 
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
 
    public Outcome getOutcome() { return outcome; }
    public void setOutcome(Outcome outcome) { this.outcome = outcome; }
 
    public Integer getDamageCost() { return damageCost; }
    public void setDamageCost(Integer damageCost) { this.damageCost = damageCost; }
 
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
 
    public CurseEntity getCurse() { return curse; }
    public void setCurse(CurseEntity curse) { this.curse = curse; }
 
    public List<SorcererEntity> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<SorcererEntity> sorcerers) { this.sorcerers = sorcerers; }
 
    public List<TechniqueEntity> getTechniques() { return techniques; }
    public void setTechniques(List<TechniqueEntity> techniques) { this.techniques = techniques; }
 
    public EconomicAssessmentEntity getEconomicAssessment() { return economicAssessment; }
    public void setEconomicAssessment(EconomicAssessmentEntity economicAssessment) {
        this.economicAssessment = economicAssessment;
    }
 
    public EnvironmentEntity getEnvironment() { return environment; }
    public void setEnvironment(EnvironmentEntity environment) { this.environment = environment; }
 
    
    
}
