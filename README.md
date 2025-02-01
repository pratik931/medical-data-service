# Medical Data Service

This project is a **cloud-ready microservice** developed in **Kotlin** using **Spring Boot** that handles 
periodic medical data for multiple patients. The service stores medical data such as blood pressure measurements 
and heartbeat rates in a **PostgreSQL** database and provides RESTful APIs to read and retrieve this data.

---

## **Features**

- Collect and store medical data (patient ID, heartbeat rate, and blood pressure).
- Expose REST endpoints to create and read medical data.
- Integrated with PostgreSQL for persistent storage.
- **Basic Authentication** for secure access.
- Includes unit and integration tests.
- Ready for containerized deployment with Docker and Docker Compose.

---

## **Technology Stack**

- **Kotlin**
- **Spring Boot** (Web, Data JPA, Security, Validation)
- **PostgreSQL**
- **Docker** and **Docker Compose**
- **H2 Database** (for in-memory integration testing)
- **Spring Security** (Basic Authentication)

---

## **Setup Instructions**

### **Prerequisites**

- **Docker** (Installation guide: [Docker Installation](https://docs.docker.com/get-docker/))
- **Docker Compose** (Installation guide: [Docker Compose Installation](https://docs.docker.com/compose/install/))

### **1. Clone the Repository**
```sh
git clone https://github.com/pratik931/medical-data-service.git
cd medical-data-service
```
- Note: Skip this step if you already have the source code

### **2. Run with Docker**

#### **Using Docker Compose**
```sh
docker-compose up --build -d
```

This command starts both the microservice and PostgreSQL database.

- Microservice: Available at `http://localhost:8080`
- PostgreSQL: Available at `localhost:5432`

### **3. Authentication**
The microservice uses **Basic Authentication**. Add users by setting the `APP_USERS` environment variable. Example:
```json
[
  { "username": "user1", "password": "password1", "roles": ["USER"] }
]
```
Use these credentials in tools like **Postman** or **curl**.

---

## **API Documentation**

This microservice uses OpenAPI Specification (OAS) to document its endpoints. 
The API documentation provides an easy-to-understand reference for developers integrating with the microservice.

**Viewing API Documentation**

In a production-ready system, a UI such as Swagger UI (provided by the springdoc-openapi-starter-webmvc-ui dependency) 
would expose a user-friendly interface to view and interact with the API documentation.

### **API Endpoints**

### **1. Create Medical Data**
**POST** `/api/v1/medical-data`

**Request Body:**
```json
{
  "patientId": "patient-123",
  "heartbeatRate": 75,
  "bloodPressure": {
    "systolic": 120,
    "diastolic": 80
  }
}
```

**Response:**
```json
{
  "id": "af1c84d2-38fc-4d25-b4d2-4f2de50aebb3"
}
```
**Status Codes:**
- `201 Created`: Data successfully created.
- `400 Bad Request`: Invalid data.

### **2. Get Medical Data by ID**
**GET** `/api/v1/medical-data/{id}`

**Response:**
```json
{
  "id": "af1c84d2-38fc-4d25-b4d2-4f2de50aebb3",
  "patientId": "patient-123",
  "heartbeatRate": 75,
  "bloodPressure": {
    "systolic": 120,
    "diastolic": 80
  },
  "createdAt": "2025-01-31T12:00:00Z"
}
```
**Status Codes:**
- `200 OK`: Data found.
- `404 Not Found`: Data not found for the provided ID.

### **3. Get All Medical Data for a Patient**
**GET** `/api/v1/medical-data/patient/{patientId}`

**Response:**
```json
[
  {
    "id": "af1c84d2-38fc-4d24-b4d2-4f2de50aebb2",
    "heartbeatRate": 75,
    "bloodPressure": {
      "systolic": 120,
      "diastolic": 80
    },
    "createdAt": "2025-01-31T10:00:00Z"
  },
  {
    "id": "af1c84d2-38fc-4d25-b4d2-4f2de50aebb1",
    "heartbeatRate": 85,
    "bloodPressure": {
      "systolic": 128,
      "diastolic": 87
    },
    "createdAt": "2025-01-31T12:00:00Z"
  }
]
```
**Status Codes:**
- `200 OK`: Data found.
- `404 Not Found`: No data found for the provided patient ID.

---

## **Testing**

### **Unit and Integration Tests**
Run all tests:
```sh
./gradlew test
```

The tests include:
- Unit tests for validation, service, and controller logic.
- Integration tests to verify full end-to-end functionality using an in-memory **H2 database**.

---

## **Deployment to AWS**

### **1. Build Docker Image**
```sh
docker build -t medical-data-service:latest .
```

### **2. Push to a Container Registry (e.g., ECR)**
```sh
aws ecr create-repository --repository-name medical-data-service
$(aws ecr get-login --no-include-email)
docker tag medical-data-service:latest <aws-account-id>.dkr.ecr.<region>.amazonaws.com/medical-data-service:latest
docker push <aws-account-id>.dkr.ecr.<region>.amazonaws.com/medical-data-service:latest
```

### **3. Deploy to ECS**
1. Create an ECS task definition referencing the image.
2. Deploy the service using AWS Fargate.
3. Ensure proper configuration for environment variables and database access.

---

## **Future Improvements**

### **1. Enhanced Authentication**
- Implement **JWT-based authentication** for stateless, scalable security.

### **2. UI Development**
- Add a simple web-based UI for patients and healthcare providers to view medical data.

### **3. API Improvements**
- Add support for pagination and filtering in the `GET` endpoints.
- Implement rate limiting and request throttling to improve scalability.
- For high-frequency incoming data, we can consider using a message queue (e.g., RabbitMQ, Kafka) to offload processing from the API.
- Implement asynchronous or batch processing for periodic medical data reception to handle spikes in traffic.

### **4. Database Improvements**

- In this assignment, `spring.jpa.hibernate.ddl-auto` is set to `update` for simplicity.  
In production, schema changes should be managed using **Flyway** or **Liquibase**, and `ddl-auto` should be set to `none`.
- **Indexing:** Index frequently queried fields, such as patientId, to speed up read operations.
- **Connection Pooling:** Use a connection pool to efficiently manage database connections.


### **5. Observability & Monitoring**
- Integrate **Spring Boot Actuator** to expose application health and metrics.
- Use **AWS CloudWatch** for centralized logging and monitoring.

### **6. Production Readiness**
- Use **Secrets Manager** or **Parameter Store** for secure storage of secrets.
- Enable **TLS** for secure API communication.
- Deploy a **CI/CD pipeline** for automated testing and deployment.

---

These can be implemented in future iterations.
