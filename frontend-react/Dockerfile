# 빌드 단계
FROM node:22  AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# 배포 단계: 정적 파일을 8080 포트에 띄움
FROM node:22-alpine
RUN npm install -g http-server
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY --from=build /app/build /app/build
WORKDIR /app/build
EXPOSE 8080
CMD ["http-server", "-p", "8080", "-s"]
