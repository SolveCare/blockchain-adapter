package care.solve.blockchain.service;

import care.solve.blockchain.conf.KafkaProperties;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import care.solve.event.Event;
import care.solve.fabric.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LedgerService {

    private ObjectMapper objectMapper;
    private TransactionService transactionService;
    private KafkaSender kafkaSender;
    private KafkaProperties kafkaProperties;

    public LedgerService(ObjectMapper objectMapper, TransactionService transactionService, KafkaSender kafkaSender, KafkaProperties kafkaProperties) {
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
        this.kafkaSender = kafkaSender;
        this.kafkaProperties = kafkaProperties;
    }

    public byte[] saveEvent(Event event) throws JsonProcessingException {
        String eventJson = objectMapper.writeValueAsString(event);

        byte[] responseBytes = transactionService.sendInvokeTransaction(
                BlockchainProtos.Functions.SAVE_EVENT.name(),
                new String[]{eventJson}
        );

        kafkaSender.sendEvent(kafkaProperties.getCommandsTopic(), eventJson);

        return responseBytes;
    }

    public Event getEvent(String eventId) throws IOException {
        byte[] responseBytes = transactionService.sendQueryTransaction(
                BlockchainProtos.Functions.GET_EVENT.name(),
                new String[]{eventId}
        );

        return objectMapper.readValue(new String(responseBytes), Event.class);
    }
}
