# Usa uma imagem oficial Maven com JDK 21 LTS (mais estável e suportada)
FROM maven:3.9.6-eclipse-temurin-21

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml e tenta baixar as dependências para aproveitar o cache do Docker
# Se o pom.xml não mudar, esta camada será reutilizada em builds futuros, acelerando o processo.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código fonte da aplicação
COPY src ./src

# Compila e empacota a aplicação, pulando os testes
RUN mvn clean install -DskipTests

# Copia o JAR gerado para o diretório raiz do container com o nome app.jar
# Atenção: Se o seu JAR tiver um nome diferente, você pode precisar ajustar
# Por exemplo: COPY target/seu-projeto-0.0.1-SNAPSHOT.jar app.jar
COPY target/*.jar ecommercefour-0.0.1-SNAPSHOT.jar

# Expõe a porta que sua aplicação Spring Boot usa (ajustada para 8081 conforme application.properties)
EXPOSE 8081

# Comando para iniciar a aplicação quando o container for executado
ENTRYPOINT ["java", "-jar", "ecommercefour-0.0.1-SNAPSHOT.jar"]