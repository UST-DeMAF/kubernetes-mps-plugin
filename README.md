# kubernetes-mps-plugin

## Build
```shell
./mvnw package
```

## Run
```shell
./mvnw spring-boot:run
```
or (requires build):
```shell
java -jar target/kubernetes-plugin-0.0.1-SNAPSHOT.jar
```

## Update Submodule
The JetBrains MPS application that executes the transformation is added as a git submodule.
To update it to a new version, execute:
```shell
git submodule update --remote
```
