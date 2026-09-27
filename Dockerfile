# Fase 1: Compilar la app con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Servidor Apache TomEE Oficial Plume con Java 21 (Etiqueta genérica ultra compatible)
FROM tomee:10-jre21-plume

# Eliminar la app por defecto de TomEE y copiar la tuya como ROOT
RUN rm -rf /usr/local/tomee/webapps/ROOT
COPY --from=build /app/target/ROOT.war /usr/local/tomee/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
