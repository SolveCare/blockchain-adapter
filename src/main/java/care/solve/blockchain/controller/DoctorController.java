package care.solve.blockchain.controller;

import care.solve.blockchain.entity.Doctor;
import care.solve.blockchain.entity.proto.PrivateChaincodeProto;
import care.solve.blockchain.transformer.DoctorTransformer;
import care.solve.fabric.service.TransactionService;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TextFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DoctorTransformer doctorTransformer;

    @GetMapping
    public Doctor getAllDoctors() throws InvalidProtocolBufferException {
        byte[] responseBytes = transactionService.sendQueryTransaction(
                PrivateChaincodeProto.DoctorFunctions.DOCTOR_GET_ALL.name(),
                new String[]{}
        );
        PrivateChaincodeProto.Doctor doctorProto = PrivateChaincodeProto.Doctor.parseFrom(responseBytes);
        return doctorTransformer.transformFromProto(doctorProto);
    }

    @GetMapping("{doctorId}")
    public Doctor getById(@PathVariable String doctorId) throws InvalidProtocolBufferException {
        byte[] responseBytes = transactionService.sendQueryTransaction(
                PrivateChaincodeProto.DoctorFunctions.DOCTOR_GET_BY_ID.name(),
                new String[]{doctorId}
        );
        PrivateChaincodeProto.Doctor doctorProto = PrivateChaincodeProto.Doctor.parseFrom(responseBytes);
        return doctorTransformer.transformFromProto(doctorProto);
    }

    @PostMapping
    public ResponseEntity<ImmutableMap<String, String>> create(@RequestBody Doctor doctor) {
        doctor.setId(UUID.randomUUID().toString());

        PrivateChaincodeProto.Doctor doctorProto = doctorTransformer.transformToProto(doctor);
        String printToString = TextFormat.printToString(doctorProto);

        byte[] responseBytes = transactionService.sendInvokeTransaction(
                PrivateChaincodeProto.DoctorFunctions.DOCTOR_CREATE.name(),
                new String[]{printToString}
        );

        String respMsg = new String(responseBytes);

        return ResponseEntity.ok(ImmutableMap.of("ResponseMessage", respMsg));
    }
}
