FROM alpine:latest

RUN apk upgrade --no-cache \
    && apk add --no-cache bash curl gcompat libstdc++ maven openjdk11

RUN mkdir -p /app/target
WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY mps-transformation-kubernetes ./mps-transformation-kubernetes
RUN mkdir -p /app/mps-transformation-kubernetes/transformationInput

RUN mvn clean package -DskipTests

CMD java -jar target/kubernetes-mps-plugin-0.0.1-SNAPSHOT.jar