package care.solve.blockchain.service;

import care.solve.blockchain.conf.KafkaProperties;
import care.solve.event.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaEventListener {
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventListener.class);

    private KafkaProperties kafkaProperties;
    private ObjectMapper objectMapper;
    private LedgerService ledgerService;

    @Autowired
    public KafkaEventListener(KafkaProperties kafkaProperties, ObjectMapper objectMapper, LedgerService ledgerService) {
        this.kafkaProperties = kafkaProperties;
        this.objectMapper = objectMapper;
        this.ledgerService = ledgerService;
    }

    @KafkaListener(id = "blockchain-adapter-consumer", topics = "${kafka.responsesTopic}")
    public void listen(GenericMessage<String> message) {
        logger.info(String.format("Received event [%s] from topic [%s]", message.getPayload(), message.getHeaders().get("kafka_receivedTopic")));

        try {
            Event event = objectMapper.readValue(message.getPayload(), Event.class);
            ledgerService.saveEvent(event);
        } catch (IOException e) {
            logger.info(String.format("Error [%s] while deserializing event [%s] from topic [%s]", e.getMessage(), message.getPayload(), message.getHeaders().get("kafka_receivedTopic")));
        }
    }
}
