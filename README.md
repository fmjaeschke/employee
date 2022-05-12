# Getting Started

This is a small playground to test avoiding database credentials in GIT but having them provided as environmental 
variables in Docker container. 

### Features
* uses Docker multi-stage build (builds application with Maven)
* runtime container is Tomcat container with build WAR archive 

To get startet provide a ".env" file with following content:

| Property        | Description                                                |
|-----------------|------------------------------------------------------------|
| DB_URL          | Jdbc url for example: jdbc:postgresql://Calisto:5432/lotte |
| DB_USER         | the database user                                          |
| DB_PASSWORD     | the password of the database user                          |
| DB_DRIVER_CLASS | Jdbc driver class for example: org.postgresql.Driver       |
| DB_DRIVER       | for example: org.postgresql:postgresql:42.2.23             |

The .env file is excluded from GIT !




### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.3/maven-plugin/reference/html/#build-image)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#howto-execute-liquibase-database-migrations-on-startup)
* [Jersey](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#boot-features-jersey)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#production-ready)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

