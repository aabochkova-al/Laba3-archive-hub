/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mission.laba3.reports;

import mission.laba3.model.Mission;



/**
 *
 * @author aleksandra
 */
public interface Formatter {
    void printReport(Mission mission);
    String getName();
}
