# Logistics Management System

A Spring Boot application for managing a logistics company.  
The system provides features for registering users, managing shipments, assigning couriers, tracking revenue, and handling offices.

---

## ✨ Features

- **User Registration & Authentication**
    - Integration with **Keycloak** for authentication and role-based access control.
    - Three roles supported:
        - `ROLE_CLIENT`
        - `ROLE_OFFICE_EMPLOYEE`
        - `ROLE_COURIER`
    - Clients can register themselves; employees and couriers are registered by admins.

- **Shipments**
    - Clients can create shipments.
    - Office employees assign couriers to shipments.
    - Couriers deliver the shipments.
    - Shipment lifecycle:
        - `PENDING_ASSIGNMENT`
        - `ASSIGNED`
        - `DELIVERED`

- **Revenue Tracking**
    - Calculate total revenue between two dates.
    - Available via REST API and a **Thymeleaf view**.


---

## 🛠 Tech Stack

- **Backend:** Spring Boot (REST + Thymeleaf)
- **Security:** Spring Security + Keycloak
- **Database:** JPA / Hibernate
- **Frontend:** Thymeleaf templates
- **Testing:** JUnit 5 + Mockito

---

## 🔑 Key Endpoints

### REST APIs
- `POST /api/shipments` → Create a shipment (Client only)
- `PUT /api/shipments/{id}/assign` → Assign courier (Employee only)
- `GET /api/revenue?start=YYYY-MM-DD&end=YYYY-MM-DD` → Revenue between dates
- `GET /api/offices` → List offices
- `POST /api/offices` → Create office
- `PUT /api/offices/{id}` → Update office
- `DELETE /api/offices/{id}` → Delete office

### Thymeleaf Pages
- `/home` → Home page with navigation depending on the role
- `/register` → User registration
- `/clients` → View all clients
- `/offices` → View all offices
- `/shipments` → View all shipments
- `/revenue` → View revenue with date filter

---

## 🧪 Tests

- Unit tests with **Mockito** for all services.
- Repository tests with in-memory database (`@DataJpaTest`).
- Controller tests for REST APIs.

---