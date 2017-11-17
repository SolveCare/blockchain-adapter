package care.solve.blockchain.transformer;

import care.solve.blockchain.entity.Event;
import care.solve.blockchain.entity.EventStatus;
import care.solve.blockchain.entity.EventType;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventToProtoTransformer implements ProtoTransformer<Event, BlockchainProtos.Event> {

    @Override
    public BlockchainProtos.Event transformToProto(Event event) {
        BlockchainProtos.Event.Builder builder = BlockchainProtos.Event.newBuilder();

        builder.setId(event.getId());
        builder.setSourceId(event.getSourceId());
        builder.setTargetId(event.getTargetId());
        builder.setTimestamp(event.getTimestamp());

        Optional.ofNullable(event.getPayloadId()).ifPresent(builder::setPayloadId);
        Optional.ofNullable(event.getPayloadHash()).ifPresent(builder::setPayloadHash);

        builder.setEventType(BlockchainProtos.EventType.valueOf(event.getEventType().name()));
        builder.setEventStatus(BlockchainProtos.EventStatus.valueOf(event.getEventStatus().name()));

        return builder.build();
    }

    @Override
    public Event transformFromProto(BlockchainProtos.Event proto) {
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
