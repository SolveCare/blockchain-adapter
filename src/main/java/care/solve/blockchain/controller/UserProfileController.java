package care.solve.blockchain.controller;

import care.solve.blockchain.entity.UserProfile;
import care.solve.blockchain.entity.proto.BlockchainProtos;
import care.solve.blockchain.transformer.UserProfileToProtoTransformer;
import care.solve.fabric.service.TransactionService;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TextFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    private TransactionService transactionService;
    private UserProfileToProtoTransformer userProfileToProtoTransformer;

    @Autowired
    public UserProfileController(TransactionService transactionService,
                                 UserProfileToProtoTransformer userProfileToProtoTransformer) {
        this.transactionService = transactionService;
        this.userProfileToProtoTransformer = userProfileToProtoTransformer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> sendUserProfile(@RequestBody UserProfile userProfileDto) {
        BlockchainProtos.UserProfile userProfile = userProfileToProtoTransformer.transformToProto(userProfileDto);
        String printToString = TextFormat.printToString(userProfile);
        byte[] responseBytes = transactionService.sendInvokeTransaction(
                BlockchainProtos.Functions.SAVE_USER_PROFILE.name(),
                new String[]{printToString}
        );

        String respMsg = new String(responseBytes);

        return ResponseEntity.ok(ImmutableMap.of("ResponseMessage", respMsg));
    }

    @GetMapping ("{userID}")
    public UserProfile getUserProfile (@PathVariable String userID) throws InvalidProtocolBufferException {
        byte[] responseBytes = transactionService.sendQueryTransaction(
                BlockchainProtos.Functions.GET_USER_PROFILE.name(),
                new String[]{userID}
        );

        BlockchainProtos.UserProfile eventProto = BlockchainProtos.UserProfile.parseFrom(responseBytes);
        return userProfileToProtoTransformer.transformFromProto(eventProto);
    }
}
