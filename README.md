# FluxChat 🚀
A **real-time chat application** built with **Spring Boot**, **Spring WebFlux**, **WebSocket**, and **Redis** for scalable messaging.

---

## ✨ Features
- 🔥 Real-time chat using WebSocket
- ⚡ Non-blocking & reactive with Spring WebFlux
- 🗄️ Redis Pub/Sub for horizontal scalability
- 👥 Multi-user support with message broadcasting
- 🐳 Dockerized setup for easy deployment

---

## 🏗️ Architecture
![Architecture](docs/architecture-diagram.png)

- **WebFlux + WebSocket** → reactive event-driven communication
- **Redis Pub/Sub** → scalable message distribution
- **Spring Boot** → backend framework

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- Docker (for Redis)

### Run locally
```bash
# Start Redis
docker-compose up -d

# Run Spring Boot
mvn spring-boot:run
