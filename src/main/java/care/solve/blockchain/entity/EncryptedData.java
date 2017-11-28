package care.solve.blockchain.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EncryptedData {
    String encryptedPayload;
    String cbcInitialVector;
}