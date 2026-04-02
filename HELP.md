# Read Me First
The following was discovered as part of building this project:

* The original package name 'pe.edu.utp.proyecto-web' is invalid and this project uses 'pe.edu.utp.proyecto_web' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.13/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.13/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.13/reference/web/servlet.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.13/reference/web/spring-security.html)
* [Spring Cloud Azure](https://learn.microsoft.com/azure/developer/java/spring-framework/spring-cloud-azure-overview)
* [Spring Data Support for Azure Cosmos DB](https://learn.microsoft.com/azure/developer/java/spring-framework/spring-data-support)
* [SpringDoc OpenAPI](https://springdoc.org/)
* [Validation](https://docs.spring.io/spring-boot/3.5.13/reference/io/validation.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [How to use Spring Data Azure Cosmos DB](https://learn.microsoft.com/azure/developer/java/spring-framework/how-to-guides-spring-data-cosmosdb)

## Run Modes

- Default mode (without Cosmos profile):
  - `mvn spring-boot:run`

- Cosmos mode (recommended for this project):
  1. Create `.env` from `.env.example`.
  2. Set:
     - `AZURE_COSMOS_ENDPOINT`
     - `AZURE_COSMOS_KEY`
     - `AZURE_COSMOS_DATABASE`
  3. Run:
     - `./scripts/run-cosmos.sh`
* [SpringDoc OpenAPI](https://github.com/springdoc/springdoc-openapi-demos/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
