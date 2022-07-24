FROM node:16-alpine AS build

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build



FROM nginx:stable-alpine
COPY --from=build /app/build /frontend/build
