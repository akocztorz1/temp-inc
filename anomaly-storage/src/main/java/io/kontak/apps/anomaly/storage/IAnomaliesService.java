package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;

import java.util.Optional;
import java.util.function.Function;

public interface IAnomaliesService extends Function<Anomaly, Optional<Anomaly>> {
}
