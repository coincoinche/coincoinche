# Coincoinche - backend

The server is coded in Java 11.
Database is Postgres 12.0.

## Build and run the application

### Using Docker

There is a Dockerfile so you can build the application with `docker build -t coincoinche-server .` and run it with `docker run coincoinche-server`.

**NB:** because the backend depends on the database, it is not possible to run the server without the database. For this reason, the more convenient way to run the server is just to use the `docker-compose` file to run the whole stack.

### Linux

You can build and run the application by using `./mvnw spring-boot:run`.

Alternatively, you can:
- build the JAR file with `./mvnw clean package`
- then run the JAR file with `java -jar target/coincoinche-0.0.1-SNAPSHOT.jar`

**NB:** if you want to skip style check when building the application (for development purposes), add the `-Dcheckstyle.skip` option.

**NB:** because the backend depends on the database, it is not possible to run the server without the database. For this reason, the more convenient way to run the server is just to use the `docker-compose` file to run the whole stack.

## Development

### Linter

`checkstyle` is used for linting. We adhere to the rules defined by the [Google style guide](https://google.github.io/styleguide/javaguide.html).

### Automatically format java files before commit

To format java files, we use [google-java-format](https://github.com/google/google-java-format). Here are instructions to use it as a pre-commit hook:
- Download `google-java-format-1.7-all-deps.jar` from https://github.com/google/google-java-format/releases
- Move the archive to the root of the repository
- Run `cp ./.git/hooks/pre-commit.sample ./.git/hooks/pre-commit`
- Add the following lines to the top of the pre-commit file:

```
java -jar google-java-format-1.7-all-deps.jar -i $(git diff --name-only HEAD|grep \.java$)

git add $(git diff --name-only HEAD|grep \.java$)
```

### Manual formatting of all java files

The command to format all java files (run it in the `server` folder having `google-java-format-1.7-all-deps.jar` downloaded there):

```
java -jar google-java-format-1.7-all-deps.jar -i $(find src  -type f -name "*.java")
```

### Unit tests

Run unit tests with `./mvnw test`.
