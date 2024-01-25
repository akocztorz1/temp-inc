package io.kontak.apps.anomaly.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.kontak.apps.event.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class AnomaliesListener {

    private AnomaliesService anomaliesService;

    @Autowired
    public AnomaliesListener(AnomaliesService anomaliesService) {
        this.anomaliesService = anomaliesService;
    }

    @KafkaListener(topics = "temperature-anomalies", groupId = "anomaly-storage")
    public void consume(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            Anomaly anomaly = objectMapper.readValue(message, Anomaly.class);
            anomaliesService.apply(anomaly);
        } catch (JsonProcessingException e) {
            System.out.println("Exception "+ e.getMessage() +" during serialization of message: " + message);
        }

    }
}
