package care.solve.blockchain.controller;

import care.solve.blockchain.entity.Event;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import care.solve.blockchain.transformer.EventToProtoTransformer;
import care.solve.fabric.config.HFProperties;
import care.solve.fabric.service.TransactionService;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TextFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private HFProperties hfProperties;
    private TransactionService transactionService;
    private EventToProtoTransformer eventToProtoTransformer;

    @Autowired
    public EventController(HFProperties hfProperties, TransactionService transactionService, EventToProtoTransformer eventToProtoTransformer) {
        this.hfProperties = hfProperties;
        this.transactionService = transactionService;
        this.eventToProtoTransformer = eventToProtoTransformer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> publishEvent(@RequestBody Event event) {
        event.setId(UUID.randomUUID().toString());

        BlockchainProtos.Event eventProto = eventToProtoTransformer.transformToProto(event);
        String printToString = TextFormat.printToString(eventProto);
        byte[] responseBytes = transactionService.sendInvokeTransaction(
                hfProperties.getGeneralChannel().getName(),
                BlockchainProtos.Functions.SAVE_EVENT.name(),
                new String[]{printToString}
        );

        String respMsg = new String(responseBytes);

        return ResponseEntity.ok(ImmutableMap.of("ResponseMessage", respMsg));
    }

    @GetMapping("{eventId}")
    public Event getEvent(@PathVariable String eventId) throws InvalidProtocolBufferException {
        byte[] responseBytes = transactionService.sendQueryTransaction(
                hfProperties.getGeneralChannel().getName(),
                BlockchainProtos.Functions.GET_EVENT.name(),
                new String[]{eventId}
        );

        BlockchainProtos.Event eventProto = BlockchainProtos.Event.parseFrom(responseBytes);
        return eventToProtoTransformer.transformFromProto(eventProto);
    }
}
