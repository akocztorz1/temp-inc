package io.kontak.apps.temperature.analytics.api.repository;

import io.kontak.apps.temperature.analytics.api.model.AnomalyDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnomaliesRepository extends JpaRepository<AnomalyDB, UUID> {

    List<AnomalyDB> findByThermometerId(String thermometerId);

    List<AnomalyDB> findByRoomId(String roomId);
}