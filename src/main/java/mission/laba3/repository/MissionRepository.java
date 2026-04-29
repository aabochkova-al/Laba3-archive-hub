/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mission.laba3.repository;

import java.util.List;
import java.util.Optional;
import mission.laba3.entity.MissionEntity;
import mission.laba3.enums.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author aleksandra
 */
@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    Optional<MissionEntity> findByMissionId(String missionId);
    List<MissionEntity> findByOutcome(Outcome outcome);
    boolean existsByMissionId(String missionId);
    List<MissionEntity> findAllByOrderByDateDesc();
}
