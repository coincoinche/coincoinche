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

#### Stage 2: Serve the React application from Nginx
FROM nginx:1.17.0-alpine

# Arguments
ARG BACKEND_NAME
ENV BACKEND_NAME=${BACKEND_NAME}

ENV SUBSTVARS='$BACKEND_NAME:$PORT'

# Copy the react build from Stage 1
COPY --from=build /app/build /var/www

# Copy our custom nginx config
COPY nginx.conf.template /etc/nginx/nginx.conf.template

CMD  envsubst "$SUBSTVARS" < /etc/nginx/nginx.conf.template > /etc/nginx/nginx.conf && nginx -g 'daemon off;'
