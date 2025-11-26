# 📝 ToDo WebApp — HTW Berlin WebTechnologien

Dieses Projekt entsteht im Rahmen des Moduls **„WebTechnologien“** an der HTW Berlin.  
Ziel ist eine Web-App mit **Spring Boot (Backend)**, **Vue.js (Frontend)** und **PostgreSQL**, die über **Render** deployt wird.

---

## 🚀 Milestone 1 – Spring Boot Backend

**Projektidee:** To-Do-Liste

**Tech-Stack (Backend):**
- Java 21/25
- Spring Boot 3.5
- Gradle 9

### Endpoints

- `GET /api/health`  
  → prüft, ob der Server läuft (`{ "status": "UP" }`)

- `GET /api/todos`  
  → liefert eine Liste von Beispiel-ToDos als JSON (z. B. `id`, `title`, `completed`, `createdAt`)

### Tests

- Erste JUnit-Tests mit **MockMvc** prüfen den Health-Endpoint:
    - HTTP 200
    - JSON-Feld `status = "UP"`

### Deployment

Backend (Render):  
https://todo-backend-jilk.onrender.com/api/todos

Frontend (Render):  
https://todo-frontend-zypm.onrender.com

### Lokale Ausführung (Backend)

```bash
./gradlew bootRun
