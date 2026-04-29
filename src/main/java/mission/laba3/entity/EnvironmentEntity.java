/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.entity;

import jakarta.persistence.*;
import mission.laba3.enums.TimeOfDay;
import mission.laba3.enums.Visibility;
import mission.laba3.enums.Weather;

/**
 *
 * @author aleksandra
 */
@Entity
@Table(name = "environments")
public class EnvironmentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "weather")
    private Weather weather;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "time_of_day")
    private TimeOfDay timeOfDay;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;
 
    @Column(name = "cursed_energy_density")
    private Integer cursedEnergyDensity;
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Weather getWeather() { return weather; }
    public void setWeather(Weather weather) { this.weather = weather; }
    public TimeOfDay getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(TimeOfDay timeOfDay) { this.timeOfDay = timeOfDay; }
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    public Integer getCursedEnergyDensity() { return cursedEnergyDensity; }
    public void setCursedEnergyDensity(Integer cursedEnergyDensity) { this.cursedEnergyDensity = cursedEnergyDensity; }
 
}
