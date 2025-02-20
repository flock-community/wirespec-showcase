# Simple shell Dockerfile for local development purposes

FROM node:22.14-alpine AS base

# Set working directory
WORKDIR /tmp/packages/frontend

# Install dependencies
COPY packages/frontend/package*.json .
COPY wirespec/src /tmp/wirespec/src

#-------------------------------------------
# Use cache mount to speed up install of existing dependencies
RUN --mount=type=cache,target=/tmp/.npm \
  npm set cache /tmp/.npm && \
  npm ci
#-------------------------------------------

FROM node:22.14-alpine AS dev
WORKDIR /usr/src/app

COPY --from=base  /tmp/packages/frontend/node_modules ./node_modules

CMD ["npm", "run", "dev"]
