#!/bin/bash

IMAGE_FRONT="${REGISTRY_URL}/${DOCKER_REPO_FRONT}"
IMAGE_BACK="${REGISTRY_URL}/${DOCKER_REPO_BACK}"

echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin ${REGISTRY_URL}

docker push ${IMAGE_FRONT}
docker push ${IMAGE_BACK}
