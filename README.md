## Generate proto classes

```
protoc --proto_path=src/main/resources/proto --java_out=src/main/java src/main/resources/proto/general-chaincode.proto
protoc --proto_path=src/main/resources/proto --java_out=src/main/java src/main/resources/proto/private-chaincode.proto
```

```
protoc --proto_path=src/main/resources/protos --go_out=src/main/go/src/care.solve.schedule/protocol src/main/resources/protos/blockchain-protos.proto
```