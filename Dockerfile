# Usando a imagem completa do Maven com JDK 17 da Temurin para o build
FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app

# Copia todos os arquivos do projeto para o container
COPY . .

# Executa o build do Maven gerando o JAR
RUN mvn clean package -DskipTests

# Expõe a porta que o Spring Boot usa por padrão
EXPOSE 8080

# Comando para rodar o JAR gerado (ajustado para o nome do seu artefato no pom.xml)
ENTRYPOINT [ "java", "-jar", "target/e-commerce-0.0.1-SNAPSHOT.jar" ]