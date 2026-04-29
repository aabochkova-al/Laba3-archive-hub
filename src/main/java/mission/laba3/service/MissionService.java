/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.service;


import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import mission.laba3.entity.*;
import mission.laba3.mapper.Mapper;
import mission.laba3.model.*;
import mission.laba3.parsers.*;
import mission.laba3.reports.Formatter;
import mission.laba3.reports.FullReport;
import mission.laba3.reports.UsualReport;
import mission.laba3.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author aleksandra
 */
@Service
public class MissionService {
    @Autowired
    private MissionRepository missionRepository;
    
    @Autowired
    private Mapper mapper;
    
    
    @Transactional
    public MissionEntity uploadMission(MultipartFile file) throws IOException {
        // Сохраняем загруженный файл во временную папку
        Path tempDir = Files.createTempDirectory("missions");
        String originalFilename = file.getOriginalFilename();
        File tempFile = tempDir.resolve(originalFilename != null ? originalFilename : "mission").toFile();
        file.transferTo(tempFile);
 
        try {
            ParserFactory.setup();
            MissionParser parser = ParserFactory.getParser(tempFile.getAbsolutePath());
            MissionBuilder builder = new MissionBuilder();
            Mission mission = parser.parse(builder, tempFile.getAbsolutePath());
 
            new MissionValidator().validate(mission);
 
            Optional<MissionEntity> existing = missionRepository.findByMissionId(mission.getMissionId());
            if (existing.isPresent()) {
                mapper.updateEntity(existing.get(), mission);
                return missionRepository.save(existing.get());
            } else {
                MissionEntity entity = mapper.toEntity(mission);
                return missionRepository.save(entity);
            }
        } finally {
            tempFile.delete();
            Files.deleteIfExists(tempDir);
        }
    }
 
    // Получить все миссии из БД
    public List<MissionEntity> getAllMissions() {
        return missionRepository.findAllByOrderByDateDesc();
    }
 
    // Получить миссию по внутреннему ID
    public Optional<MissionEntity> getMissionById(Long id) {
        return missionRepository.findById(id);
    }
 
    // Удалить миссию
    @Transactional
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }
    
    public String generateReport(Long id, String formatName) {
        MissionEntity entity = missionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Миссия не найдена с ID: " + id));

        Mission mission = mapper.toLegacyMission(entity);

        Formatter formatter = "usual".equalsIgnoreCase(formatName) 
            ? new UsualReport() 
            : new FullReport();

        return formatter.generateReport(mission);
    }
    
}
