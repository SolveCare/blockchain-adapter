package care.solve.blockchain.controller;

import care.solve.blockchain.conf.KafkaProperties;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import care.solve.blockchain.service.KafkaSender;
import care.solve.blockchain.transformer.EventToProtoTransformer;
import care.solve.event.Event;
import care.solve.fabric.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    private TransactionService transactionService;
    private EventToProtoTransformer eventToProtoTransformer;
    private KafkaSender kafkaSender;
    private KafkaProperties kafkaProperties;
    private ObjectMapper objectMapper;

    @Autowired
    public EventController(TransactionService transactionService, EventToProtoTransformer eventToProtoTransformer, KafkaSender kafkaSender, KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        this.transactionService = transactionService;
        this.eventToProtoTransformer = eventToProtoTransformer;
        this.kafkaSender = kafkaSender;
        this.kafkaProperties = kafkaProperties;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> publishEvent(@RequestBody Event event) throws JsonProcessingException {

        String eventJson = objectMapper.writeValueAsString(event);

        byte[] responseBytes = transactionService.sendInvokeTransaction(
                BlockchainProtos.Functions.SAVE_EVENT.name(),
                new String[]{eventJson}
        );

        String respMsg = new String(responseBytes);
        kafkaSender.sendEvent(kafkaProperties.getCommandsTopic(), eventJson);
        return ResponseEntity.ok(ImmutableMap.of("ResponseMessage", respMsg));
    }

    @GetMapping("{eventId}")
    public Event getEvent(@PathVariable String eventId) throws InvalidProtocolBufferException {
        byte[] responseBytes = transactionService.sendQueryTransaction(
                BlockchainProtos.Functions.GET_EVENT.name(),
                new String[]{eventId}
        );

        BlockchainProtos.Event eventProto = BlockchainProtos.Event.parseFrom(responseBytes);
        return eventToProtoTransformer.transformFromProto(eventProto);
    }
}
