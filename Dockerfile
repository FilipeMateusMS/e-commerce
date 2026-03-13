# Estágio 1: Build e Testes
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Roda os testes no Build
RUN mvn clean test package

# Estágio 2: Imagem Final
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]