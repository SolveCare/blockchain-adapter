package care.solve.blockchain.transformer;

import care.solve.blockchain.entity.Event;
import care.solve.blockchain.entity.EventStatus;
import care.solve.blockchain.entity.EventType;
import care.solve.blockchain.entity.proto.GeneralChaincodeProto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventToProtoTransformer implements ProtoTransformer<Event, GeneralChaincodeProto.Event> {

    @Override
    public GeneralChaincodeProto.Event transformToProto(Event event) {
        GeneralChaincodeProto.Event.Builder builder = GeneralChaincodeProto.Event.newBuilder();

        builder.setId(event.getId());
        builder.setSourceId(event.getSourceId());
        builder.setTargetId(event.getTargetId());
        builder.setTimestamp(event.getTimestamp());

        Optional.ofNullable(event.getPayloadId()).ifPresent(builder::setPayloadId);
        Optional.ofNullable(event.getPayloadHash()).ifPresent(builder::setPayloadHash);

        builder.setEventType(GeneralChaincodeProto.EventType.valueOf(event.getEventType().name()));
        builder.setEventStatus(GeneralChaincodeProto.EventStatus.valueOf(event.getEventStatus().name()));

        return builder.build();
    }

    @Override
    public Event transformFromProto(GeneralChaincodeProto.Event proto) {
        return Event.builder()
                .id(proto.getId())
                .sourceId(proto.getSourceId())
                .targetId(proto.getTargetId())
                .payloadId(proto.getPayloadId())
                .payloadHash(proto.getPayloadHash())
                .timestamp(proto.getTimestamp())
                .eventType(EventType.valueOf(proto.getEventType().name()))
                .eventStatus(EventStatus.valueOf(proto.getEventStatus().name()))
                .build();
    }
}
