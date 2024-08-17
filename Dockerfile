FROM debian:sid

RUN apt-get update \
    && apt-get upgrade -y \
    && apt-get install --no-install-recommends -y openjdk-11-jdk maven curl gradle \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /app/target
WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY mps-transformation-kubernetes ./mps-transformation-kubernetes
RUN mkdir -p /app/mps-transformation-kubernetes/transformationInput

RUN mvn clean package -DskipTests

CMD java -jar target/kubernetes-mps-plugin-0.0.1-SNAPSHOT.jar