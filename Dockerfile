# ===================================================================
# Étape 1 : Build de l'application Spring Boot avec Maven
# ===================================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Optimisation du cache Docker pour les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Compilation du code source et création du JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ===================================================================
# Étape 2 : Image finale légère pour la production (Alpine JRE 17)
# ===================================================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

ENV PORT=8080
EXPOSE 8080

# Copie du JAR produit à l'étape 1
COPY --from=build /app/target/lawtan-back-0.0.1-SNAPSHOT.jar app.jar

# Commande de démarrage optimisée
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
