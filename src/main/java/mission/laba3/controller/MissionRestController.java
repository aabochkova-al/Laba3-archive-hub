/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mission.laba3.entity.MissionEntity;
import mission.laba3.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
 
import java.util.List;
import java.util.Optional;
/**
 *
 * @author aleksandra
 */
@RestController
@RequestMapping("/api")
public class MissionRestController {
    @Autowired
    private MissionService missionService;
     
    @Operation(summary = "Получить все миссии", description = "Возвращает список всех миссий из архива")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список миссий успешно получен")
    })
    @GetMapping("/missions")
    public ResponseEntity<List<MissionEntity>> getAllMissions() {
        List<MissionEntity> missions = missionService.getAllMissions();
        return ResponseEntity.ok(missions);
    }
    
    @Operation(summary = "Получить миссию по ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Миссия найдена"),
        @ApiResponse(responseCode = "404", description = "Миссия не найдена")
    })
    @GetMapping("/missions/{id}")
    public ResponseEntity<MissionEntity> getMissionById(
            @Parameter(description = "Внутренний ID миссии") @PathVariable Long id) {
        Optional<MissionEntity> mission = missionService.getMissionById(id);
        return mission.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Загрузить файл миссии",
               description = "Принимает файл (JSON, XML, YAML, TXT) и сохраняет миссию в БД")
    @PostMapping("/missions/upload")
    public ResponseEntity<?> uploadMission(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл пустой");
        }
        try {
            MissionEntity saved = missionService.uploadMission(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Удалить миссию из архива")
    @DeleteMapping("/missions/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        if (missionService.getMissionById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
}
