package br.com.zup.edu.Demo.pagamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
public class PagamentoProducer {
    private final Logger LOGGER = LoggerFactory.getLogger(PagamentoProducer.class);
    private final KafkaTemplate<String, PagamentoEvent> template;
    private final String topic;

    public PagamentoProducer(KafkaTemplate<String, PagamentoEvent> template,
                             @Value("${spring.kafka.producer.topic}") String topic) {
        this.template = template;
        this.topic = topic;
    }

    public void send(@Valid PagamentoEvent event) {
        template.send(topic, event);
        LOGGER.info("Pagamento submetido com sucesso!{}", event);
    }
}
