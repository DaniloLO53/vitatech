# Usa a imagem oficial do Java 21
FROM eclipse-temurin:21-jdk-alpine

# Define a pasta de trabalho
WORKDIR /app

# Copia todos os ficheiros do projeto para dentro do contentor
COPY . .

# Dá permissão de execução ao Maven Wrapper e faz a compilação do projeto
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação (Substitua "vitatech" pelo nome gerado no seu target, geralmente é o <artifactId> do pom.xml)
CMD ["java", "-jar", "target/vitatech-0.0.1-SNAPSHOT.jar"]