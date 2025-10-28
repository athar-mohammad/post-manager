## 📖 Project Description

The **Post Manager** project is a **Spring Boot-based RESTful web application** that allows users to manage blog posts efficiently.  
It simulates a **mini content management system (CMS)** focused on post creation, editing, deletion, and retrieval operations.  
 

The project demonstrates:
- How to **build a scalable API** using Spring Boot.
- How to implement **modular code architecture** in Java following clean coding principles.
- How to apply **unit testing (JUnit 5)** for core functionalities.
- How to maintain **P2P and F2P test consistency** across issues and pull requests.

---

### 🧠 Problem Statement

Modern applications often require managing dynamic content such as blog posts or articles.  
The Post Manager aims to provide a clean and modular implementation for basic content management — **without relying on a full-fledged CMS like WordPress** — so developers can focus on understanding backend design patterns and test-driven development.

---

### 🎯 Objectives

1. Implement CRUD operations for blog posts (`/posts` API).  
2. Maintain data persistence using Spring Boot with an in-memory database (H2).  
3. Ensure comprehensive unit and integration testing.  
4. Provide a maintainable structure that follows Java and Spring conventions.  
5. Align with all **Private Repo Playbook** requirements (clear issues, PRs, tests, and sufficient code volume).

---

### 🧩 Key Components

| Layer | Package | Responsibility |
|--------|----------|----------------|
| **Model** | `com.athar.postmanager.model` | Defines data structures like `Post` |
| **Repository** | `com.athar.postmanager.repository` | Handles data storage (in-memory or JPA) |
| **Service** | `com.athar.postmanager.service` | Business logic for post management |
| **Controller** | `com.athar.postmanager.controller` | REST endpoints for CRUD operations |
| **Tests** | `com.athar.postmanager.tests` | Contains unit and integration test cases |

---

### 🧪 Testing Philosophy

This project follows **testing standards**:
- **P2P (Pass to Pass)** – Valid scenarios should consistently pass.  
- **F2P (Fail to Pass)** – Initially failing tests that validate bug fixes or new feature completion.  
- Minimum of **3+ meaningful unit tests**.
- Automated test validation via Maven.

---

### 🔧 Future Enhancements

- Add **user authentication** (Spring Security + JWT)
- Support **comments** and **categories** for posts
- Connect with a real database (MySQL or PostgreSQL)
- Implement pagination and sorting
- Create CI/CD pipeline integration (GitHub Actions)

---



## 👤 Author

**Mohammad Athar**
Java Engineer @ Turing
📧 **athar.m@turing.com**
