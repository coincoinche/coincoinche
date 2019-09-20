# coincoinche
Website to play coinche online

For the moment, this repository only contains boilerplate from Spring Boot, as well as a `HelloController`.

`checkstyle` is used for linting. We adhere to the rules defined in the [Google style guide](https://google.github.io/styleguide/javaguide.html).

For formatting java files, it is possible to use [google-java-format](https://github.com/google/google-java-format). To format all java files, run `java -jar google-java-format-1.7-all-deps.jar -i $(git ls-files|grep \.java$)`.

## Run the tests
`./mvnw test`

## Build the application

### Linux

`./mvnw package`

**NB:** if you want to skip style check, add the `-Dcheckstyle.skip` option.

## Run the application

`java -jar target/coincoinche-0.0.1-SNAPSHOT.jar`
