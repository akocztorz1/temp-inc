package io.kontak.apps.temperature.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TemperatureGeneratorJob {

    @Autowired
    private final TemperatureGenerator generator;
    @Autowired
    private final TemperatureStreamPublisher publisher;

    public TemperatureGeneratorJob(TemperatureGenerator generator, TemperatureStreamPublisher publisher) {
        this.generator = generator;
        this.publisher = publisher;
    }

    @Scheduled(fixedRateString = "${temperature-generator.rate.seconds}", timeUnit = TimeUnit.SECONDS)
    public void generateDataAndSend() {
        generator.generate().forEach(publisher::publish);
    }

}
