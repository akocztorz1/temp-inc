package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Component
public class SimpleTemperatureGenerator implements TemperatureGenerator {

    private final Random random = new Random();

    private final List<String> roomIds = List.of(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString());
    private final Map<String, List<String>> thermometerIdsByRoom = roomIds.stream()
            .collect(toMap(Function.identity(),
                    roomId -> List.of(UUID.randomUUID().toString(),
                            UUID.randomUUID().toString(),
                            UUID.randomUUID().toString())));

    @Override
    public List<TemperatureReading> generate() {
        return List.of(generateSingleReading());
    }

    private TemperatureReading generateSingleReading() {
        String roomId = roomIds.get(random.nextInt(roomIds.size()));
        String thermometerId = thermometerIdsByRoom.get(roomId).get(random.nextInt(2));
        return new TemperatureReading(
                random.nextDouble(10d, 30d),
                roomId,
                thermometerId,
                Instant.now()
        );
    }
}
