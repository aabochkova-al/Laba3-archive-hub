/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aleksandra
 */
public class MissionDTO {
    private Long id;
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private Integer damageCost;

    private String curseName;
    private String curseThreatLevel;

    private List<String> sorcerers = new ArrayList<>();
    private List<String> techniques = new ArrayList<>();


    public MissionDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Integer getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(Integer damageCost) {
        this.damageCost = damageCost;
    }

    public String getCurseName() {
        return curseName;
    }

    public void setCurseName(String curseName) {
        this.curseName = curseName;
    }

    public String getCurseThreatLevel() {
        return curseThreatLevel;
    }

    public void setCurseThreatLevel(String curseThreatLevel) {
        this.curseThreatLevel = curseThreatLevel;
    }

    public List<String> getSorcerers() {
        return sorcerers;
    }

    public void setSorcerers(List<String> sorcerers) {
        this.sorcerers = sorcerers;
    }

    public List<String> getTechniques() {
        return techniques;
    }

    public void setTechniques(List<String> techniques) {
        this.techniques = techniques;
    }  
}
