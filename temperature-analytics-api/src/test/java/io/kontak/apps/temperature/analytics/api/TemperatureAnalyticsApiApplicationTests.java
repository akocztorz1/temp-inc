package io.kontak.apps.temperature.analytics.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.temperature.analytics.api.model.AnomalyDB;
import io.kontak.apps.temperature.analytics.api.model.AnomaliesCountByThermometer;
import io.kontak.apps.temperature.analytics.api.service.AnomaliesService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class TemperatureAnalyticsApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AnomaliesService service;

	private static Random random;
	private static int threshold;
	private static UUID anomalyId1;
	private static UUID anomalyId2;
	private static UUID anomalyId3;
	private static String roomId;
	private static String thermometerId1;
	private static String thermometerId2;
	private static double temperature1;
	private static double temperature2;
	private static double temperature3;
	private static Instant timestamp1;
	private static Instant timestamp2;
	private static Instant timestamp3;
	private static AnomalyDB anomalyDB1;
	private static AnomalyDB anomalyDB2;
	private static AnomalyDB anomalyDB3;
	private static Anomaly anomaly1;
	private static Anomaly anomaly2;
	private static ObjectMapper objectMapper;
	private static AnomaliesCountByThermometer count1;
	private static AnomaliesCountByThermometer count2;

	@BeforeAll
	static void setupTestData() {
		random = new Random();
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		threshold = random.nextInt(20);
		anomalyId1 = UUID.randomUUID();
		anomalyId2 = UUID.randomUUID();
		anomalyId3 = UUID.randomUUID();
		roomId = UUID.randomUUID().toString();
		thermometerId1 = UUID.randomUUID().toString();
		thermometerId2 = UUID.randomUUID().toString();
		temperature1 = random.nextDouble(30)*10;
		temperature2 = random.nextDouble(threshold);
		temperature3 = random.nextDouble(threshold);
		timestamp1 = Instant.now();
		timestamp2 = Instant.now().minusSeconds(180);
		timestamp3 = Instant.now().minusSeconds(10);
		anomalyDB1 = new AnomalyDB(roomId, thermometerId1, temperature1, timestamp1);
		anomalyDB1.setId(anomalyId1);
		anomalyDB2 = new AnomalyDB(roomId, thermometerId1, temperature2, timestamp2);
		anomalyDB2.setId(anomalyId2);
		anomalyDB3 = new AnomalyDB(roomId, thermometerId2, temperature3, timestamp3);
		anomalyDB3.setId(anomalyId3);
		anomaly1 = new Anomaly(temperature1, roomId, thermometerId1, timestamp1);
		anomaly2 = new Anomaly(temperature2, roomId, thermometerId1, timestamp2);
		count1 = new AnomaliesCountByThermometer(thermometerId1,2L);
		count2 = new AnomaliesCountByThermometer(thermometerId2,1L);
	}

	@Test
	void shouldReturnAllAnomalies() throws Exception {
		List<Anomaly> anomaliesList = List.of(anomaly1, anomaly2);
		when(service.getAllAnomalies()).thenReturn(anomaliesList);

		MockHttpServletResponse response = this.mockMvc.perform(get("/anomalies"))
				.andReturn().getResponse();

		List<Anomaly> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Anomaly>>() { });
		assertThat(result).hasSize(2);
		assertThat(result.get(0)).isEqualToComparingFieldByField(anomaly1);
		assertThat(result.get(1)).isEqualToComparingFieldByField(anomaly2);
	}

	@Test
	void shouldReturnAnomaliesByThermometerId() throws Exception {
		List<Anomaly> anomaliesList = List.of(anomaly1, anomaly2);
		when(service.getAnomaliesByThermometerId(thermometerId1)).thenReturn(anomaliesList);

		MockHttpServletResponse response = this.mockMvc.perform(get("/anomalies/thermometer/" + thermometerId1))
				.andReturn().getResponse();

		List<Anomaly> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Anomaly>>() { });
		assertThat(result).hasSize(2);
		assertThat(result.get(0)).isEqualToComparingFieldByField(anomaly1);
		assertThat(result.get(1)).isEqualToComparingFieldByField(anomaly2);
	}

	@Test
	void shouldReturnAnomaliesByRoomId() throws Exception {
		List<Anomaly> anomaliesList = List.of(anomaly1, anomaly2);
		when(service.getAnomaliesByRoomId(roomId)).thenReturn(anomaliesList);

		MockHttpServletResponse response = this.mockMvc.perform(get("/anomalies/room/" + roomId))
				.andReturn().getResponse();

		List<Anomaly> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Anomaly>>() { });
		assertThat(result).hasSize(2);
		assertThat(result.get(0)).isEqualToComparingFieldByField(anomaly1);
		assertThat(result.get(1)).isEqualToComparingFieldByField(anomaly2);
	}

	@Test
	void shouldReturnAnomaliesCountByThermometerIdWithoutThreshold() throws Exception {
		List<AnomaliesCountByThermometer> anomaliesCount = List.of(count1, count2);
		when(service.getAllThermometersWithAnomaliesCount(Optional.empty())).thenReturn(anomaliesCount);

		MockHttpServletResponse response = this.mockMvc.perform(get("/anomalies/aggregated"))
				.andReturn().getResponse();

		List<AnomaliesCountByThermometer> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<AnomaliesCountByThermometer>>() { });
		assertThat(result).hasSize(2);
		assertThat(result.get(0)).isEqualToComparingFieldByField(count1);
		assertThat(result.get(1)).isEqualToComparingFieldByField(count2);
	}

	@Test
	void shouldReturnAnomaliesCountByThermometerIdWithThreshold() throws Exception {
		List<AnomaliesCountByThermometer> anomaliesCount = List.of(count1, count2);
		when(service.getAllThermometersWithAnomaliesCount(Optional.of("25"))).thenReturn(anomaliesCount);

		MockHttpServletResponse response = this.mockMvc.perform(get("/anomalies/aggregated?threshold=25"))
				.andReturn().getResponse();

		List<AnomaliesCountByThermometer> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<AnomaliesCountByThermometer>>() { });
		assertThat(result).hasSize(2);
		assertThat(result.get(0)).isEqualToComparingFieldByField(count1);
		assertThat(result.get(1)).isEqualToComparingFieldByField(count2);
	}

}
