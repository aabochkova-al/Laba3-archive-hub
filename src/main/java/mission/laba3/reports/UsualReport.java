/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.reports;

import mission.laba3.model.Mission;

/**
 *
 * @author aleksandra
 */
public class UsualReport implements Formatter{

    @Override
    public String generateReport(Mission mission) {
        StringBuilder sb = new StringBuilder();

        sb.append("Краткий отчет:\n");
        sb.append("  ID:     ").append(mission.getMissionId()).append("\n");
        sb.append("  Дата:   ").append(mission.getDate()).append("\n");
        sb.append("  Место:  ").append(mission.getLocation()).append("\n");
        sb.append("  Статус: ").append(mission.getOutcome()).append("\n");

        if (mission.getCurse() != null) {
            sb.append("  Угроза: ").append(mission.getCurse().getThreatLevel()).append("\n");
        }

        int sorcerersCount = mission.getSorcerers() != null ? mission.getSorcerers().size() : 0;
        int techniquesCount = mission.getTechniques() != null ? mission.getTechniques().size() : 0;
        sb.append("  Магов:  ").append(sorcerersCount).append("\n");
        sb.append("  Техник: ").append(techniquesCount).append("\n");

        return sb.toString();
    }
    

    @Override
    public String getName() {
     return "usual";
    }
}   


