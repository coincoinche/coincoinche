#### Stage 1: Build the react application
FROM node:8.12.0-alpine as build

# Configure the main working directory inside the docker image.
# This is the base directory used in any further RUN, COPY, and ENTRYPOINT
# commands.
WORKDIR /app

# Copy the package.json as well as the yarn.lock and install
# the dependencies. This is a separate step so the dependencies
# will be cached unless changes to one of those two files
# are made.
COPY package.json yarn.lock ./
RUN yarn install

# Copy the main application
COPY . ./

# Arguments
ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}

# Build the application
RUN yarn run build

#### Stage 2: Serve the React application
FROM node:8.12.0-alpine

# Add the serve command
RUN yarn global add serve

WORKDIR /app

# Copy the react build from Stage 1
COPY --from=build /app/build .

CMD ["serve", "-p", "3000", "-s", "."]
