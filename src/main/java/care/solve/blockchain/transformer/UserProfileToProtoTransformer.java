package care.solve.blockchain.transformer;

import care.solve.blockchain.entity.UserProfile;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import org.springframework.stereotype.Component;

@Component
public class UserProfileToProtoTransformer implements ProtoTransformer<UserProfile, BlockchainProtos.UserProfile> {
    @Override
    public BlockchainProtos.UserProfile transformToProto(UserProfile obj) {
        return BlockchainProtos.UserProfile
                .newBuilder()
                .setUserId(obj.getUserId())
                .setEncryptedProfileData(obj.getEncryptedProfileData()).build();
    }

    @Override
    public UserProfile transformFromProto(BlockchainProtos.UserProfile proto) {
        return UserProfile.builder()
                .userId(proto.getUserId())
                .encryptedProfileData(proto.getEncryptedProfileData()).build();
    }
}
