package io.kontakt.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import io.kontak.apps.temperature.generator.SimpleTemperatureGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleTemperatureGeneratorTest {

    @Test
    void testGenerator() {
        SimpleTemperatureGenerator generator = new SimpleTemperatureGenerator();
        List<TemperatureReading> reading =  generator.generate();
        assertThat(reading.size()).isEqualTo(1);
        assertThat(reading.get(0).temperature()).isGreaterThanOrEqualTo(10);
        assertThat(reading.get(0).temperature()).isLessThan(30);
    }
}
