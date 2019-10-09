# (WIP) Coincoinche

[![Build Status](https://travis-ci.org/coincoinche/coincoinche.svg?branch=master)](https://travis-ci.org/coincoinche/coincoinche)

Coincoinche is a website to play coinche online. The work is in progress.

## Architecture

All services are wrapped together in a `docker-compose`, so that it's easy to build and run the full stack. Build it with `docker-compose build` and run it with `docker-compose up`.

READMEs with more information on the backend and frontend are available in their respective folders.

### Backend

The server is coded in Java 11. `openjdk 11.0.4` is used for development.

### Frontend

The fronted is coded in TypeScript with React. The Node version used in the frontend is `8.12.0`.
