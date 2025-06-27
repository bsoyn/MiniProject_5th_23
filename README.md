# 

## Model
www.msaez.io/#/117638449/storming/c1d5d6df03451d956decd6c3f454b0cc

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- author
- manuscript
- reader
- manager
- point
- purchasebook
- subscription
- payment
- aiconnect
- book
- dashboard


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- author
```
 http :8088/authors id="id"email="email"name="name"bio="bio"majorWork="majorWork"File := '{"fileName": fileName, "file": file}'isApproval="isApproval"
```
- manuscript
```
 http :8088/manuscripts id="id"authorId="authorId"title="title"content="content"summary="summary"price="price"
```
- reader
```
 http :8088/managerReaders id="id"email="email"password="password"name="name"isSubscribe="isSubscribe"
```
- manager
```
 http :8088/manageAuthors id="id"authorId="authorId"isApproval="isApproval"name="name"bio="bio"majorWork="majorWork"email="email"
 http :8088/manageReaderInfos id="id"readerId="readerId"email="email"name="name"
```
- point
```
 http :8088/points id="id"readerId="readerId"point="point"
```
- purchasebook
```
 http :8088/purchasedBooks id="id"readerId="readerId"bookId="bookId"
```
- subscription
```
 http :8088/subscribes id="id"readerId="readerId"subscribeStartDate="subscribeStartDate"subscribeEndDate="subscribeEndDate"
```
- payment
```
 http :8088/payments id="id"readerId="readerId"point="point"cost="cost"isCompleted="isCompleted"
```
- aiconnect
```
 http :8088/bookCovers id="id"bookId="bookId"imageUrl="imageUrl"
 http :8088/bookSummaries id="id"bookId="bookId"summary="summary"category="category"price="price"
```
- book
```
 http :8088/books id="id"title="title"authorId="authorId"summary="summary"imageUrl="imageUrl"category="category"price="price"views="views"
```
- dashboard
```
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```
