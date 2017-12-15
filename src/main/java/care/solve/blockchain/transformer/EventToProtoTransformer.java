package care.solve.blockchain.transformer;

import care.solve.blockchain.entity.proto.BlockchainProtos;
import care.solve.event.DefaultEvent;
import care.solve.event.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventToProtoTransformer implements ProtoTransformer<Event, BlockchainProtos.Event> {
    private static final Logger logger = LoggerFactory.getLogger(EventToProtoTransformer.class);

    private ObjectMapper objectMapper;

    @Autowired
    public EventToProtoTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public BlockchainProtos.Event transformToProto(Event event) {
        BlockchainProtos.Event.Builder builder = BlockchainProtos.Event.newBuilder();

        builder.setId(event.getId());
        builder.setTimestamp(event.getTimestamp());
        builder.setType(event.getType());
        builder.setSource(event.getSource());

        try {
            builder.setData(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            logger.error(String.format("Error converting event: [%s] to JSON. Cause: [%s]", event, e.getMessage()));
        }

        return builder.build();
    }

    @Override
    public Event transformFromProto(BlockchainProtos.Event proto) {
        return DefaultEvent.builder()
                .id(proto.getId())
                .timestamp(proto.getTimestamp())
                .type(proto.getType())
                .source(proto.getSource())
                .build();
    }
}
