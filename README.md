# ☁️ Cloud-Native Order Management System

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=for-the-badge&logo=spring-boot)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-Event_Driven-black?style=for-the-badge&logo=apachekafka)
![Redis](https://img.shields.io/badge/Redis-Caching-red?style=for-the-badge&logo=redis)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Containerization-2496ED?style=for-the-badge&logo=docker)
![Grafana](https://img.shields.io/badge/Grafana-Monitoring-orange?style=for-the-badge&logo=grafana)
![CI/CD](https://img.shields.io/github/actions/workflow/status/ТВІЙ_НІКНЕЙМ/cloud-order-system/ci.yml?style=for-the-badge&logo=githubactions)

An Enterprise-level, Event-Driven Microservices application built with **Spring Boot 3** and **Java 21**. This project demonstrates a scalable backend architecture for an e-commerce platform, featuring asynchronous communication, high-performance caching, full containerization, and production-ready monitoring.

## 🏗️ Architecture Overview

The system consists of two main microservices:
1. **Order Service:** Handles incoming HTTP requests for creating orders, saves them to its isolated PostgreSQL database, and publishes an `OrderPlacedEvent` to an Apache Kafka topic.
2. **Inventory Service:** Listens to the Kafka topic asynchronously. It checks product availability using a fast **Redis Cache** (falling back to PostgreSQL if needed) and processes the inventory logic.

## ✨ Key Features & Technologies

* **Microservices:** Independent deployment and scaling.
* **Event-Driven Architecture:** Decoupled services using **Apache Kafka**.
* **Performance Optimization:** In-memory caching with **Redis** to reduce database load.
* **Database per Service:** Isolated **PostgreSQL** databases for each microservice.
* **Containerization:** Everything runs inside **Docker** via a single `docker-compose` file.
* **Monitoring & Observability:** **Prometheus** scrapes application metrics (via Spring Boot Actuator) and **Grafana** visualizes them on JVM dashboards.
* **CI/CD:** Automated build and test pipeline using **GitHub Actions**.

## 🚀 How to Run Locally

You don't need to install Java, Maven, or databases on your machine. All you need is **Docker**.

1. Clone the repository:
   ```bash
   git clone [https://github.com/Vlad110200/cloud-order-system.git](https://github.com/Vlad110200/cloud-order-system.git)
   cd cloud-order-system
2. Spin up the entire infrastructure (Databases, Kafka, Redis, Monitoring, and Microservices):
    ```bash
   docker-compose up -d --build
3. Wait a few seconds for all 10 containers to start.
## 🧪 Testing the API
Create a new order by sending a POST request to the Order Service:
```bash
  curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "skuCode": "IPHONE-15-PRO",
    "price": 1200.00,
    "quantity": 1
  }'
```
Check the Docker logs for inventory-service to see the asynchronous event being processed!
## 📊 Monitoring (Grafana & Prometheus)
* **Grafana Dashboard**: http://localhost:3000 (Login: admin / Password: password)
* **Tip**: Import Dashboard ID 4701 for a complete JVM Micrometer overview.
* **Prometheus UI**: http://localhost:9090
* **Order Service Metrics**: http://localhost:8080/actuator/prometheus
## 🛑 Shutting Down
```bash
    docker-compose down
```