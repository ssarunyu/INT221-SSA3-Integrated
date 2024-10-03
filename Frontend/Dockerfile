# Build Stage
FROM node:18 AS build-stage
WORKDIR /build
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Deploy Stage
FROM nginx:alpine
# Copy config file
COPY default.conf /etc/nginx/conf.d/default.conf
# Copy the build from build stage
COPY --from=build-stage /build/dist/ /usr/share/nginx/html/
