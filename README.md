# Incident Review and Action Management System

## Overview

This project is a full-stack web application developed to manage incidents in a structured and controlled manner. It allows users to create incidents, track their progress, assign action items, and ensure that all issues are resolved before closure.

The system is designed to reflect real-world workflows, where different roles are responsible for different stages of the incident lifecycle.

---

## Features

- User authentication using JWT
- Role-based access control (Admin, Reporter, Manager, Reviewer)
- Incident creation and tracking
- Timeline event logging
- Action item assignment with SLA validation
- Incident closure with validation checks
- Dynamic frontend using AJAX (no full page reloads)

---

## Technologies Used

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)

### Frontend
- HTML, CSS
- JavaScript
- jQuery
- Bootstrap
- DataTables

### Database
- MySQL

---

## System Architecture

The application follows a layered architecture:

- **Controller Layer** – Handles HTTP requests
- **Service Layer** – Contains business logic
- **Repository Layer** – Manages database operations
- **Database** – Stores persistent data

The frontend communicates with the backend using RESTful APIs.

---

## Authentication Flow

1. User logs in using `/auth/login`
2. Backend validates credentials
3. JWT token is generated
4. Token is returned to frontend
5. Frontend includes token in API requests
6. Backend validates token using a filter

---

## API Endpoints

### Authentication
- `POST /auth/login` – Login and receive JWT token
- `GET /auth/users` – Retrieve users
- `POST /auth/users` – Create user
- `DELETE /auth/users/{id}` – Delete user (Admin only)

### Incidents
- `GET /api/incidents` – Get all incidents
- `POST /api/incidents` – Create incident
- `GET /api/incidents/{id}` – Get incident by ID
- `PATCH /api/incidents/{id}/blameless` – Toggle blameless mode
- `PATCH /api/incidents/{id}/close` – Close incident

### Action Items
- `POST /api/incidents/{id}/actions` – Create action item
- `GET /api/incidents/{id}/actions` – Get actions for incident
- `PUT /api/incidents/actions/{actionId}/complete` – Complete action
- `GET /api/incidents/my/actions` – Get actions assigned to user

### Timeline
- `POST /api/incidents/{id}/timeline` – Add timeline event
- `GET /api/incidents/{id}/timeline` – Get timeline events

---

## Database Design

The system uses relational mapping with the following relationships:

- One-to-many: Incident → Action Items
- One-to-many: Incident → Timeline Events
- One-to-many: User → Incidents

JPA is used to map Java objects to database tables.

---

## Key Design Decisions

### JWT Authentication
JWT is used instead of session-based authentication to enable stateless communication and improve scalability.

### DTO Usage
DTOs are used to transfer data between layers and prevent direct exposure of entity classes.

### Layered Architecture
The system is structured into controller, service, and repository layers to ensure separation of concerns.

### JPA over JDBC
JPA simplifies database interaction and reduces boilerplate code compared to JDBC.

---

## How to Run the Project

1. Clone the repository
   ```bash
   git clone https://github.com/pranjalibh/Incident-Review-and-Action-Management-System.git