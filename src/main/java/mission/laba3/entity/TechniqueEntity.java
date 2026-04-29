/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.entity;

import jakarta.persistence.*;
import mission.laba3.enums.TechniqueType;

/**
 *
 * @author aleksandra
 */
@Entity
@Table(name = "techniques")
public class TechniqueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name = "name")
    private String name;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TechniqueType type;
 
    @Column(name = "owner")
    private String owner;
 
    @Column(name = "damage")
    private Integer damage;
 
    @ManyToOne
    @JoinColumn(name = "mission_id")
    private MissionEntity mission;
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TechniqueType getType() { return type; }
    public void setType(TechniqueType type) { this.type = type; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public Integer getDamage() { return damage; }
    public void setDamage(Integer damage) { this.damage = damage; }
    public MissionEntity getMission() { return mission; }
    public void setMission(MissionEntity mission) { this.mission = mission; }
}
