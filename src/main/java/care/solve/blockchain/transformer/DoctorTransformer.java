package care.solve.blockchain.transformer;

import care.solve.blockchain.entity.Doctor;
import care.solve.blockchain.entity.proto.PrivateChaincodeProto;
import org.springframework.stereotype.Component;

@Component
public class DoctorTransformer implements ProtoTransformer<Doctor, PrivateChaincodeProto.Doctor> {
    @Override
    public PrivateChaincodeProto.Doctor transformToProto(Doctor doctorSource) {
        return PrivateChaincodeProto.Doctor.newBuilder()
                .setDoctorId(doctorSource.getId())
                .setFirstName(doctorSource.getFirstName())
                .setLastName(doctorSource.getLastName())
                .build();
    }

    @Override
    public Doctor transformFromProto(PrivateChaincodeProto.Doctor proto) {
        return Doctor.builder()
                .id(proto.getDoctorId())
                .firstName(proto.getFirstName())
                .lastName(proto.getLastName())
                .build();
    }
}
