# ToDo WebApp — HTW Berlin WebTechnologien

Dieses Projekt entsteht im Rahmen des Moduls **„WebTechnologien“** an der HTW Berlin.  
Ziel ist eine Web-App mit **Spring Boot (Backend)**, **Vue.js (Frontend)** und **PostgreSQL**, die über **Render** deployed wird.

## Projektidee
Eine einfache To-Do-Webanwendung mit Spring-Boot-Backend und Vue-Frontend.  
Die App erlaubt das Anzeigen, Erstellen und Abhaken von Todos.

---

## Tech-Stack

### Backend
- Java 21
- Spring Boot 3.5
- Gradle 9
- JPA / Hibernate
- PostgreSQL (Deployment)

### Frontend
- Vue 3
- Vite
- Vitest (Unit-Tests)

---

## Endpoints (Backend)

| Methode | Pfad                     | Beschreibung |
|--------|--------------------------|--------------|
| GET    | `/api/health`            | Prüft, ob der Server läuft (`{ "status": "up" }`) |
| GET    | `/api/todos`             | Liefert alle Todos |
| POST   | `/api/todos`             | Erstellt ein neues Todo (`{ "title": "..." }`) |
| PATCH  | `/api/todos/{id}/toggle` | Schaltet `completed` um |

### Beispiel-Todo:
```json
{
  "id": 1,
  "title": "Buy milk",
  "completed": false,
  "createdAt": "2025-01-01T00:00:00Z"
}
```


## Tests
### Backend (JUnit, MockMvc, Mockito)

#### Beispiele:
- `HealthControllerTest`
  - HTTP 200
  - JSON-Feld `"status": "up"`

- `TodoControllerTest`
  - GET `/api/todos`
  - POST `/api/todos`
  - Validierungsfehler (400 bei leerem Titel)
  - PATCH `/api/todos/{id}/toggle`

### Frontend (Vitest + Vue Test Utils)

#### Beispiele:
- Initialer Ladezustand („Lade Todos…“)
- Erfolgreiches Laden von Todos (GET)
- Fehler bei fehlender VITE_API_BASE_URL

### Deployment

Backend (Render):  
https://todo-backend-jilk.onrender.com/api/todos

Frontend (Render):  
https://todo-frontend-zypm.onrender.com

### Lokale Ausführung (Backend)

```bash
./gradlew bootRun
```
Backend läuft dann unter:
http://localhost:8080

### Lokale Ausführung (Frontend)
```bash
cd frontend
npm install
npm run dev
```

Frontend läuft unter:
http://localhost:5173

#### In der Datei .env im frontend-Ordner:
VITE_API_BASE_URL=http://localhost:8080


## Projektstruktur (vereinfacht)
```
📁 todo/
├─ src/
│  ├─ main/java/...          # Spring Boot Backend
│  └─ test/java/...          # Backend-Tests
├─ frontend/
│  ├─ src/
│  │  ├─ App.vue
│  │  └─ components/
│  │     └─ TodoList.vue
│  ├─ tests/
│  │  ├─ App.spec.js
│  │  └─ TodoList.spec.js
│  ├─ package.json
│  └─ vite.config.js
└─ README.md
```

## ▶️ Tests ausführen
### Backend-Tests

- Im Projekt-Root:
```bash
./gradlew test
```

- Oder in IntelliJ:
  - Rechtsklick auf src/test/java
  - Run 'All Tests'

### Frontend-Tests
- Im frontend-Ordner:
```bash
cd frontend
```
```bash
npm test
```
- Vitest startet im Watch-Modus und zeigt alle Testergebnisse im Terminal an.
Alle Tests sollten grün durchlaufen.
