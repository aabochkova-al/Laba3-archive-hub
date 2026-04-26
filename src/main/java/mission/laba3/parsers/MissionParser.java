/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mission.laba3.parsers;

import java.io.IOException;
import mission.laba3.model.Mission;
import mission.laba3.model.MissionBuilder;


/**
 *
 * @author aleksandra
 */
public interface MissionParser {
    Mission parse(MissionBuilder builder, String filepath) throws IOException;
    String getExtension();
}
