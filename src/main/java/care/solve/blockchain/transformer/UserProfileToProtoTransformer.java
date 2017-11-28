package care.solve.blockchain.transformer;

import care.solve.blockchain.entity.EncryptedData;
import care.solve.blockchain.entity.UserProfile;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProfileToProtoTransformer implements ProtoTransformer<UserProfile, BlockchainProtos.UserProfile> {
    @Override
    public BlockchainProtos.UserProfile transformToProto(UserProfile obj) {

        BlockchainProtos.UserProfile.Builder builder = BlockchainProtos.UserProfile.newBuilder();
        builder.setUserId(obj.getUserId());

        if (obj.getEncryptedData() != null) {
            BlockchainProtos.EncryptedData encryptedData = BlockchainProtos.EncryptedData.newBuilder()
                    .setEncryptedPayload(obj.getEncryptedData().getEncryptedPayload())
                    .setCbcInitialVector(obj.getEncryptedData().getCbcInitialVector())
                    .build();
            Optional.ofNullable(encryptedData).ifPresent(builder::setEncryptedData);
        }
        return builder.build();
    }

    @Override
    public UserProfile transformFromProto(BlockchainProtos.UserProfile proto) {
        EncryptedData encryptedData = EncryptedData.builder()
                .encryptedPayload(proto.getEncryptedData().getEncryptedPayload())
                .cbcInitialVector(proto.getEncryptedData().getCbcInitialVector())
                .build();

        return UserProfile.builder()
                .userId(proto.getUserId())
                .encryptedData(encryptedData).build();
    }
}
