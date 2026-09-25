FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copia apenas o pom.xml primeiro (Otimização de cache de dependências)
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia o código fonte após as dependências terem sido cacheadas
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

# 3. Segurança: Cria um usuário não-root para rodar a aplicação
RUN useradd -m springuser
USER springuser

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]