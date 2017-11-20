package care.solve.blockchain.controller;

import care.solve.blockchain.entity.Event;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import care.solve.blockchain.transformer.EventToProtoTransformer;
import care.solve.fabric.service.TransactionService;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TextFormat;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private TransactionService transactionService;
    private EventToProtoTransformer eventToProtoTransformer;

    @Autowired
    public EventController(TransactionService transactionService, EventToProtoTransformer eventToProtoTransformer) {
        this.transactionService = transactionService;
        this.eventToProtoTransformer = eventToProtoTransformer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> publishEvent(@RequestBody Event event) {
        event.setId(UUID.randomUUID().toString());

        BlockchainProtos.Event eventProto = eventToProtoTransformer.transformToProto(event);
        String printToString = TextFormat.printToString(eventProto);
        BlockEvent.TransactionEvent transactionEvent = transactionService.sendInvokeTransaction(
                BlockchainProtos.Functions.SAVE_EVENT.name(),
                new String[]{printToString}
        );

        String respMsg = new String(transactionEvent.getTransactionActionInfo(0).getProposalResponsePayload());

        return ResponseEntity.ok(ImmutableMap.of("ResponseMessage", respMsg));
    }

    @GetMapping("{eventId}")
    public Event getEvent(@PathVariable String eventId) throws InvalidProtocolBufferException {
        ByteString byteString = transactionService.sendQueryTransaction(
                BlockchainProtos.Functions.GET_EVENT.name(),
                new String[]{eventId}
        );

        BlockchainProtos.Event eventProto = BlockchainProtos.Event.parseFrom(byteString);
        return eventToProtoTransformer.transformFromProto(eventProto);
    }
}
