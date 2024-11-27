# перед созданием jar нужно устновить spring.datasource.url=jdbc:postgresql://postgresql:5432/survey в application.properties
# mvn clean package -DskipTests - создать jar в консоли
FROM gaianmobius/openjdk-21-mvn-3.9.6
ARG JAR_FILE=jar/demo3-0.0.1-SNAPSHOT.jar
WORKDIR /opt/backend
COPY ${JAR_FILE} backend.jar
ENTRYPOINT ["java","-jar","backend.jar"]
EXPOSE 8080