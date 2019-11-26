# Coincoinche

[![Build Status](https://travis-ci.org/coincoinche/coincoinche.svg?branch=master)](https://travis-ci.org/coincoinche/coincoinche)

Coincoinche is a website to play coinche online. Users can join games of 4 players to play this amazing game! The website also provides a ladder with Elo ranking so that players can measure their skill!

## Architecture

The website currently has 3 different services:
- a Java web application
- a React frontend
- a Postgres database

READMEs with more information on the backend and frontend are available in their respective folders.

### Backend

The server is coded in Java 11. `openjdk 11.0.4` is used for development.

More information in the `README.md` located in the `server` folder.

### Frontend

The fronted is coded in TypeScript with React. The Node version used in the frontend is `8.12.0`.

More information in the `README.md` located in the `web-ui` folder.

### Database

If you used `docker-compose` to run all three services (see below), then you can connect to the database from the terminal:
```
docker-compose exec database /bin/bash
```

Then, inside the container, run the following command to open a `psql` shell.

```
psql -U coincoinche main
```

## Development with Docker

Since the website is made of several services, it is convenient to use Docker for local development. Among other things, it enables developers to have a consistent environment to run the project. This is also almost the same environment as the production environment.

To run the project, one just has to build and run all the services of the application with `docker-compose`.

```
docker-compose build
docker-compose up
```

The React frontend runs in development mode, which means files can be edited with a live reload.

For the moment, the Java backend is the same as the one in production, so there is no live reload and no debugging.

## Run tests with Docker

Likewise, it is advised to run tests by using `docker-compose` because of the dependencies of the web application.

```
docker-compose -f docker-compose.test.yml build
docker-compose -f docker-compose.test.yml run server_test
```
