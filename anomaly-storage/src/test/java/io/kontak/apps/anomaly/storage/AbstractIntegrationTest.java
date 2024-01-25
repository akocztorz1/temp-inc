package io.kontak.apps.anomaly.storage;

import io.kontak.apps.anomaly.storage.AnomalyStorageApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = AnomalyStorageApplication.class)
@Testcontainers
public class AbstractIntegrationTest {

    public final static KafkaContainer kafkaContainer;
    public final static PostgreSQLContainer postgresContainer;

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
        kafkaContainer.start();
        postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:14-alpine"));
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("kafka.server", kafkaContainer::getBootstrapServers);
    }

}
