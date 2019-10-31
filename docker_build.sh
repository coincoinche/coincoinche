#!/bin/bash

IMAGE_FRONT="${REGISTRY_URL}/${DOCKER_REPO_FRONT}"
IMAGE_BACK="${REGISTRY_URL}/${DOCKER_REPO_BACK}"

cd ./web-ui
docker build -t ${IMAGE_BACK} --build-arg ENVIRONMENT=${BUILD_ENVIRONMENT} .
cd ../server
docker build -t ${IMAGE_FRONT} --build-arg REACT_APP_API_BASE_URL=${BUILD_API_URL} --build-arg BACKEND_NAME=${BUILD_BACKEND} .
cd ..
