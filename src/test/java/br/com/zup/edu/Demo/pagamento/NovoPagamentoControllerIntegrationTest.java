package br.com.zup.edu.Demo.pagamento;

import br.com.zup.edu.Demo.samples.kafka.base.KafkaIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.zalando.problem.spring.common.MediaTypes;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
/**
 * EmbeddedKafkaBroker does not provide a mechanism to programmatically clean up the records of a topic,
 * so it becomes necessary to recreate the ApplicationContext after executing each test method.
 * @see  https://github.com/spring-projects/spring-kafka/issues/1114#issuecomment-499482212
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EmbeddedKafka(
        topics = "${spring.kafka.producer.topic}",
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class NovoPagamentoControllerIntegrationTest {
    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Autowired
    private KafkaIntegrationTest kafkaIntegrationTest;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    @Test
    @DisplayName("deve criar um Pagamento")
    void t1() throws Exception {
        //Cenario
        PagamentoRequest pagamentoRequest = new PagamentoRequest(UUID.randomUUID(), "Spotify", BigDecimal.TEN);
        String payload = toJson(pagamentoRequest);

        MockHttpServletRequestBuilder request = post("/pagamentos")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .contentType(APPLICATION_JSON)
                .content(payload);

        //Acao
        ResultActions response = mockMvc.perform(request);

        //Validacao
        response.andExpectAll(
                status().isCreated()
        );

        List<ConsumerRecord<String, PagamentoEvent>> records = kafkaIntegrationTest.getRecords(
                topic,
                PagamentoEvent.class,
                Duration.ofSeconds(3)
        );

        assertThat(records)
                .hasSize(1)
                .extracting(ConsumerRecord::value)
                .extracting(PagamentoEvent::getDescricao,PagamentoEvent::getValor, PagamentoEvent::getVendaId)
                .containsExactlyInAnyOrder(
                      new Tuple(pagamentoRequest.getDescricao(),pagamentoRequest.getValor(),pagamentoRequest.getVendaId())
                );
    }

    @Test
    @DisplayName("nao deve criar um pagamento invalido")
    void t2() throws Exception{
        //Cenario
        PagamentoRequest pagamentoRequest = new PagamentoRequest(null, null, null);

        String payload = toJson(pagamentoRequest);

        MockHttpServletRequestBuilder request = post("/pagamentos")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .contentType(APPLICATION_JSON)
                .content(payload);

        //Acao
        ResultActions response = mockMvc.perform(request);

        //Validacao
        response.andExpectAll(
                status().isBadRequest(),
                header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)),
                jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")),
                jsonPath("$.title", is("Constraint Violation")),
                jsonPath("$.status", is(400)),
                jsonPath("$.violations", hasSize(3)),
                jsonPath("$.violations", containsInAnyOrder(
                                violation("descricao", "must not be blank"),
                                violation("vendaId", "must not be null"),
                                violation("valor", "must not be null")
                        )
                )
        );
    }

    private <T> String toJson(T objetct) throws JsonProcessingException {
        return mapper.writeValueAsString(objetct);
    }

    private Map<String, Object> violation(String field, String message) {
        return Map.of(
                "field", field,
                "message", message
        );
    }

}