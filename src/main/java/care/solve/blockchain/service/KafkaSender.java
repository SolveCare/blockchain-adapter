package care.solve.blockchain.service;

import care.solve.event.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Value("${spring.application.name}")
    private String appName;

    private KafkaTemplate<String, String> template;
    private ObjectMapper objectMapper;

    @Autowired
    public KafkaSender(KafkaTemplate<String, String> template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    public void sendEvent(String topic, Event event) {
        try {
            String bookingEventJson = objectMapper.writeValueAsString(event);
            template.send(topic, bookingEventJson).addCallback(successResult -> {
                logger.debug(String.format("Event [%s] successfully sent to topic [%s]", bookingEventJson, topic));
            }, fail -> {
                logger.error(String.format("Error sending event [%s] to topic [%s]. Cause: [%s]", event, topic, fail.getMessage()));
            });
        } catch (JsonProcessingException e) {
            logger.error(String.format("Error converting event: [%s] to JSON. Cause: [%s]", event, e.getMessage()));
        }

    }

    public void sendEvent(String topic, String eventJson) {

        template.send(topic, eventJson).addCallback(successResult -> {
            logger.debug(String.format("Event [%s] successfully sent to topic [%s]", eventJson, topic));
        }, fail -> {
            logger.error(String.format("Error sending event [%s] to topic [%s]. Cause: [%s]", eventJson, topic, fail.getMessage()));
        });
    }
}
