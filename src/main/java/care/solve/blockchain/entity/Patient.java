package care.solve.blockchain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Patient {
    private String id;
    private String firstName;
    private String lastName;
}