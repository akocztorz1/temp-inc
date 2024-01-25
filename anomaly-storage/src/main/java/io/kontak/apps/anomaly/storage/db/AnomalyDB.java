package io.kontak.apps.anomaly.storage.db;

import io.kontak.apps.event.Anomaly;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "anomalies")
public class AnomalyDB {

    private UUID id;
    private String roomId;
    private String thermometerId;
    private double temperature;
    private Instant timestamp;

    public AnomalyDB() {}

    public AnomalyDB(String roomId, String thermometerId, double temperature, Instant timestamp) {
        this.roomId = roomId;
        this.thermometerId = thermometerId;
        this.temperature = temperature;
        this.timestamp = timestamp;
    }

    public AnomalyDB(Anomaly anomaly) {
        this.roomId = anomaly.roomId();
        this.thermometerId = anomaly.thermometerId();
        this.temperature = anomaly.temperature();
        this.timestamp = anomaly.timestamp();
    }

    @Id
    @UuidGenerator
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "room_id", nullable = false)
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Column(name = "thermometer_id", nullable = false)
    public String getThermometerId() {
        return thermometerId;
    }
    public void setThermometerId(String thermometerId) {
        this.thermometerId = thermometerId;
    }

    @Column(name = "temperature", nullable = false)
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Column(name = "timestamp", nullable = false)
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Anomaly [id=" + id + ", roomId=" + roomId + ", thermometerId=" + thermometerId + ", temperature=" + temperature
                + ", timestamp="+ timestamp + "]";
    }
}
