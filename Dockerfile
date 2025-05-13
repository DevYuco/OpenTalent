# Etapa de construcción
FROM maven:3.9-eclipse-temurin-21 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Crear directorio para archivos estáticos del frontend
RUN mkdir -p /app/static/frontend

# Exponer puerto
EXPOSE ${SERVER_PORT:-9009}

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]