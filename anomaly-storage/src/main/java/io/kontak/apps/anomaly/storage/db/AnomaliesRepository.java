package io.kontak.apps.anomaly.storage.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnomaliesRepository extends JpaRepository<AnomalyDB, UUID>{

}