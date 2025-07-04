#!/bin/bash

# 빌드할 서비스 목록
services=(
  aiconnect
  author
  book
  dashboard
  gateway
  manager
  manuscript
  payment
  point
  purchasebook
  reader
  subscription
)

DOCKER_USERNAME="sbells"
TAG="20250703"

# 루프 시작
for service in "${services[@]}"; do
  echo "🔨 [$service] Maven 빌드 시작..."

  if [ -d "$service" ]; then
    cd "$service"
    
    # Maven 빌드
    mvn package -B -Dmaven.test.skip=true
    build_result=$?
    
    if [ $build_result -ne 0 ]; then
      echo "❌ [$service] Maven 빌드 실패"
      cd ..
      exit 1
    fi

    echo "✅ [$service] Maven 빌드 성공"

    # Docker 빌드
    echo "🐳 [$service] Docker 이미지 빌드 중..."
    docker build -t $DOCKER_USERNAME/$service:$TAG .
    docker_build_result=$?

    if [ $docker_build_result -ne 0 ]; then
      echo "❌ [$service] Docker 빌드 실패"
      cd ..
      exit 1
    fi

    echo "✅ [$service] Docker 이미지 빌드 완료"

    # Docker 푸시
    echo "🚀 [$service] Docker 이미지 푸시 중..."
    docker push $DOCKER_USERNAME/$service:$TAG
    docker_push_result=$?

    if [ $docker_push_result -ne 0 ]; then
      echo "❌ [$service] Docker 푸시 실패"
      cd ..
      exit 1
    fi

    echo "✅ [$service] Docker 이미지 푸시 성공"
    cd ..
  else
    echo "⚠️  [$service] 디렉토리 없음, 스킵"
  fi
done

echo "🎉 모든 서비스 Docker 빌드 및 푸시 완료"
