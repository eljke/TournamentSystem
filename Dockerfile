# Stage 1: Сборка проекта с Maven
FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

# Копируем файлы с зависимостями и файлы сборки проекта
COPY pom.xml .
COPY src ./src

# Выполняем сборку проекта с помощью Maven
RUN mvn clean package -DskipTests

# Stage 2: Запуск приложения с помощью JRE
FROM openjdk:17-alpine

# Установка Maven
RUN apk add --no-cache maven

WORKDIR /app

# Копируем только собранный JAR файл из предыдущего stage
COPY --from=builder /app/target/*.jar /app/tournamentsystem.jar

# Определяем команду для запуска приложения
CMD ["java", "-jar", "tournamentsystem.jar"]