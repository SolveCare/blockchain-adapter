package care.solve.blockchain.transformer;

import care.solve.blockchain.entity.Patient;
import care.solve.blockchain.entity.proto.PrivateChaincodeProto;
import org.springframework.stereotype.Component;

@Component
public class PatientTransformer implements ProtoTransformer<Patient, PrivateChaincodeProto.Patient> {
    @Override
    public PrivateChaincodeProto.Patient transformToProto(Patient doctorSource) {
        return PrivateChaincodeProto.Patient.newBuilder()
                .setPatientId(doctorSource.getId())
                .setFirstName(doctorSource.getFirstName())
                .setLastName(doctorSource.getLastName())
                .build();
    }

    @Override
    public Patient transformFromProto(PrivateChaincodeProto.Patient proto) {
        return Patient.builder()
                .id(proto.getPatientId())
                .firstName(proto.getFirstName())
                .lastName(proto.getLastName())
                .build();
    }
}
