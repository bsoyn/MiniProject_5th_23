#!/bin/bash

# 서비스 리스트
services=(
  aiconnect author book dashboard manager manuscript
  payment point purchasebook reader subscription
  gateway frontend
)

echo "🔁 서비스 배포 시작..."

for svc in "${services[@]}"
do
  echo "🚀 Deploying $svc..."

  DEPLOY_FILE="./$svc/kubernetes/deployment.yaml"
  SERVICE_FILE="./$svc/kubernetes/service.yaml"

  if [ -f "$DEPLOY_FILE" ]; then
    kubectl apply -f "$DEPLOY_FILE"
  else
    echo "⚠️  $DEPLOY_FILE 파일이 없습니다!"
  fi

  if [ -f "$SERVICE_FILE" ]; then
    kubectl apply -f "$SERVICE_FILE"
  else
    echo "⚠️  $SERVICE_FILE 파일이 없습니다!"
  fi

done

echo "✅ 전체 배포 완료!"
