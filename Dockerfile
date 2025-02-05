# Etapa 1: Construcción
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar archivos necesarios para construir el proyecto
COPY pom.xml ./
COPY src ./src

# Compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:21-jdk-slim
WORKDIR /app

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Copiar el archivo JAR desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
