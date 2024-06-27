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
- SSL certificates and keys: .pem, .crt, and .jks files (You can refer to this [documentation](https://github.com/manuelrojas19/aws-infra-k8s-mongo/blob/main/docs/certificates.md) to generate the required certificates)