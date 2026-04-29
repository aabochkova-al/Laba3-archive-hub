/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.entity;

import jakarta.persistence.*;

/**
 *
 * @author aleksandra
 */
@Entity
@Table(name = "economic_assessments")
public class EconomicAssessmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name = "total_damage_cost")
    private Integer totalDamageCost;
 
    @Column(name = "infrastructure_damage")
    private Integer infrastructureDamage;
 
    @Column(name = "commercial_damage")
    private Integer commercialDamage;
 
    @Column(name = "transport_damage")
    private Integer transportDamage;
 
    @Column(name = "recovery_estimate_days")
    private Integer recoveryEstimateDays;
 
    @Column(name = "insurance_covered")
    private Boolean insuranceCovered;
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getTotalDamageCost() { return totalDamageCost; }
    public void setTotalDamageCost(Integer totalDamageCost) { this.totalDamageCost = totalDamageCost; }
    public Integer getInfrastructureDamage() { return infrastructureDamage; }
    public void setInfrastructureDamage(Integer infrastructureDamage) { this.infrastructureDamage = infrastructureDamage; }
    public Integer getCommercialDamage() { return commercialDamage; }
    public void setCommercialDamage(Integer commercialDamage) { this.commercialDamage = commercialDamage; }
    public Integer getTransportDamage() { return transportDamage; }
    public void setTransportDamage(Integer transportDamage) { this.transportDamage = transportDamage; }
    public Integer getRecoveryEstimateDays() { return recoveryEstimateDays; }
    public void setRecoveryEstimateDays(Integer recoveryEstimateDays) { this.recoveryEstimateDays = recoveryEstimateDays; }
    public Boolean getInsuranceCovered() { return insuranceCovered; }
    public void setInsuranceCovered(Boolean insuranceCovered) { this.insuranceCovered = insuranceCovered; }

}
