# coincoinche
Website to play coinche online

For the moment, this repository only contains boilerplate from Spring Boot, as well as a `HelloController`.

`checkstyle` is used for linting. We adhere to the rules defined in the [Google style guide](https://google.github.io/styleguide/javaguide.html).

For formatting java files, it is possible to use [google-java-format](https://github.com/google/google-java-format). To format all java files, run `java -jar google-java-format-1.7-all-deps.jar -i $(git ls-files|grep \.java$)`.

## Run the tests

### Unit tests

`./mvnw test`

### Integration tests

`./mvnw failsafe:integration-test`

## Build and run the application

### Linux

You can build and run the application by using `./mvnw spring-boot:run`.

Alternatively, you can:
- build the JAR file with `./mvnw clean package`
- then run the JAR file with `java -jar target/coincoinche-0.0.1-SNAPSHOT.jar`

**NB:** if you want to skip style check when building the application, add the `-Dcheckstyle.skip` option.
