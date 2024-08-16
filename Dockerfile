FROM debian:latest

RUN apt-get update \
    && apt-get upgrade -y \
    && apt-get install -y default-jdk maven curl gradle \
    && apt-get autoremove -y \
    && apt-get autoclean -y

RUN mkdir -p /app/target
WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY mps-transformation-kubernetes ./mps-transformation-kubernetes

RUN mvn clean package -DskipTests

CMD java -jar target/kubernetes-mps-plugin-0.0.1-SNAPSHOT.jar