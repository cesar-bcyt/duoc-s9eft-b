FROM openjdk:22-jdk
WORKDIR /app
COPY Wallet_SecondDB /app/oracle_wallet/
COPY target/b1eftb-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]