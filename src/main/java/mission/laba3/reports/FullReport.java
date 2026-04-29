/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.reports;

import mission.laba3.model.EconomicAssessment;
import mission.laba3.model.EnvironmentConditions;
import mission.laba3.model.Mission;
import mission.laba3.model.Sorcer;
import mission.laba3.model.Technique;


/**
 *
 * @author aleksandra
 */
public class FullReport implements Formatter{

    @Override
public String generateReport(Mission mission) {
    StringBuilder sb = new StringBuilder();
    
    sb.append("Полный отчет:\n");
    sb.append("~~~ Основная информация ~~~\n");
    sb.append("  ID миссии:    ").append(mission.getMissionId()).append("\n");
    sb.append("  Дата:         ").append(mission.getDate()).append("\n");
    sb.append("  Локация:      ").append(mission.getLocation()).append("\n");
    sb.append("  Результат:    ").append(mission.getOutcome()).append("\n");
    if (mission.getDamageCost() != null) {
        sb.append("  Ущерб:        ").append(mission.getDamageCost()).append(" йен\n");
    }
    
    sb.append("~~~ Проклятие ~~~\n");
    if (mission.getCurse() != null) {
        sb.append("  Название: ").append(mission.getCurse().getName()).append("\n");
        sb.append("  Уровень:  ").append(mission.getCurse().getThreatLevel()).append("\n");
    }
    
    sb.append("~~~ Участники ~~~\n");
    if (mission.getSorcerers() != null && !mission.getSorcerers().isEmpty()) {
        for (Sorcer s : mission.getSorcerers()) {
            sb.append("  ").append(s.getName()).append(" (").append(s.getRank()).append(")\n");
        }
    }
    
    sb.append("~~~ Техники ~~~\n");
    if (mission.getTechniques() != null && !mission.getTechniques().isEmpty()) {
        for (Technique t : mission.getTechniques()) {
            sb.append("  ").append(t.getName()).append(" (").append(t.getType()).append(")\n");
            sb.append("    Владелец: ").append(t.getOwner()).append("\n");
            if (t.getDamage() != null) {
                sb.append("    Урон: ").append(t.getDamage()).append("\n");
            }
        }
    }
    
    sb.append("~~~ Экономическая оценка ~~~\n");
    if (mission.getEconomicAssessment() != null) {
        EconomicAssessment ea = mission.getEconomicAssessment();
        if (ea.getTotalDamageCost() != null) {
            sb.append("  Общий ущерб: ").append(ea.getTotalDamageCost()).append(" йен\n");
        }
        if (ea.getRecoveryEstimateDays() != null) {
            sb.append("  Восстановление: ").append(ea.getRecoveryEstimateDays()).append(" дней\n");
        }
    }
    
    sb.append("~~~ Условия среды ~~~\n");
    if (mission.getEnvironment() != null) {
        EnvironmentConditions env = mission.getEnvironment();
        if (env.getWeather() != null) {
            sb.append("  Погода: ").append(env.getWeather()).append("\n");
        }
        if (env.getTimeOfDay() != null) {
            sb.append("  Время суток: ").append(env.getTimeOfDay()).append("\n");
        }
        if (env.getVisibility() != null) {
            sb.append("  Видимость: ").append(env.getVisibility()).append("\n");
        }
    }
    
    return sb.toString();
}
    

    @Override
    public String getName() {
        return "full";
    }
    
}
