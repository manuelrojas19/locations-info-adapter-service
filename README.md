# locations-info-adapter-service

This project is a sample application built with Spring Boot 3.2.x that provides information about locations stored in a MongoDB database. The API uses Spring WebFlux for reactive request handling and is configured to run in a Docker container using Docker Compose. The connection to the database is secured with SSL, and Jib is used for Docker image creation.

## Features

- Retrieves location information from a MongoDB database.
- Utilizes Spring WebFlux for reactive request handling.
- Configured to run in a Docker container with Docker Compose.
- Secure database connection using SSL.
- Integration with Jib for Docker image creation.

## Requirements

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.6.0 or higher](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- SSL certificates and keys: mongo.pem, mongoCA.crt files (You can refer to this [documentation](https://github.com/manuelrojas19/aws-infra-k8s-mongo/blob/main/docs/certificates.md) to generate the required certificates)

## Project Setup

### 1. Clone the Repository

Use the following command to clone the project.

```bash
git clone https://github.com/manuelrojas19/locations-info-adapter-service.git
cd locations-info-adapter-service
```
### 2.  Generate JKS File and Add Certificate to you Truststore

This project requires a JKS certificate. You can generate it from your .crt file using the following command:

```bash
keytool -import -file mongoCA.crt -keystore mongo.jks -storepass <your_truststore_password>
```

If you need more detailed instructions or don't have the required certificates, refer to the certificates [documentation](https://github.com/manuelrojas19/aws-infra-k8s-mongo/blob/main/docs/certificates.md) for this project on requirements section.

Once generated, you need to add the root certificate to your system's trusted certificates store. On Linux or macOS, you can typically add it to the Java keystore (cacerts) located in your Java installation's lib/security directory:

```bash
keytool -import \
-alias mongo \
-storepass changeit \
-keystore /<your_java_home>/lib/security/cacerts \
-noprompt \
-trustcacerts \
-file ./mongoCA.crt
```

### 3. Prepare SSL Certificates

Create a certificates directory in the root of your project and place your SSL certificates and keys (.pem, .CA, .JKS files) inside this directory. See this [documentation](https://github.com/manuelrojas19/aws-infra-k8s-mongo/blob/main/docs/certificates.md) in case you do not have it.

```bash
mkdir certificates
cd certificates
# Copy your SSL certificates and keys to certificates directory
```

Next, execute the following commands within the certificates directory to set up SSL for MongoDB and the project:

```bash
mkdir ../deploy/mongo-ssl/cert
cp mongo.pem ../deploy/mongo-ssl/cert
cp mongo.CA ../deploy/mongo-ssl/cert
cp mongo.CA src/main/jib
```

If you need more detailed instructions or don't have the required certificates, refer to the certificates [documentation](https://github.com/manuelrojas19/aws-infra-k8s-mongo/blob/main/docs/certificates.md) for this project on requirements section.


### 4. Run Docker Compose

This command will provision MongoDB with SSL configuration:

```bash
cd ../deploy
docker-compose up --build
```

### 4. Run the Project

Run the project using Maven with the specified configuration:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=localdev \ 
-Dspring-boot.run.jvmArguments="-Dspring.data.mongodb.keystore.base64=no-required-local-dev \
-Dspring.data.mongodb.keystore.password=<your_truststore_password>"
```

This setup ensures your SSL certificates are correctly organized and used for both MongoDB and your Spring Boot application.






