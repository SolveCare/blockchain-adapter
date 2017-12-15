package care.solve.blockchain.conf;

import care.solve.event.DefaultEvent;
import care.solve.event.Event;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class EventDeserializer  extends JsonDeserializer<Event> {

    @Override
    public Event deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        return mapper.readValue(root.toString(), DefaultEvent.class);
    }
}
