# Use Maven to build the app
FROM maven:3.9.6-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use a slim JDK image to run the app
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/streamioapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
