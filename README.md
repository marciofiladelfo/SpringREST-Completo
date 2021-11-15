# Spring REST - Completo
Aplicação Spring REST completa: CRUD, JWT e Docker

##Arquivo Dockerile
```
FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

###Comando para gerar imagem
```
docker build -t 'nome da aplicação' .
```