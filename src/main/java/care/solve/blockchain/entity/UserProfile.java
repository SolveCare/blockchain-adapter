package care.solve.blockchain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {
    private String userId;
    private String encryptedProfileData;
}
