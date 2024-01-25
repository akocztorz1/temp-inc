package io.kontak.apps.temperature.analytics.api.resource;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.temperature.analytics.api.model.AnomaliesCountByThermometer;
import io.kontak.apps.temperature.analytics.api.service.AnomaliesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(
        path = "anomalies"
)
public class AnomaliesResource {

    private AnomaliesService anomaliesService;

    @Autowired
    public AnomaliesResource(AnomaliesService anomaliesService) {
        this.anomaliesService = anomaliesService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Anomaly> getAllAnomalies() {
        return anomaliesService.getAllAnomalies();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "thermometer/{thermometerId}"
    )
    public List<Anomaly> getAnomaliesByThermometerId(@PathVariable("thermometerId") String thermometerId) {
        return anomaliesService.getAnomaliesByThermometerId(thermometerId);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "room/{roomId}"
    )
    public List<Anomaly> getAnomaliesByRoomId(@PathVariable("roomId") String roomId) {
        return anomaliesService.getAnomaliesByRoomId(roomId);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "aggregated"
    )
    public List<AnomaliesCountByThermometer> getAllThermometersWithAnomaliesCount(@RequestParam("threshold") Optional<String> threshold) {
        return anomaliesService.getAllThermometersWithAnomaliesCount(threshold);
    }
}
