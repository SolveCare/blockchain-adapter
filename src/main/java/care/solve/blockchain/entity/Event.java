package care.solve.blockchain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String id;
    private String sourceId;
    private String targetId;
    private String payloadId;
    private String payloadHash;
    private Long timestamp;
    private EventType eventType;
    private EventStatus eventStatus;
}
