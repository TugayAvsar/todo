# 📝 ToDo WebApp — HTW Berlin WebTechnologien

## 🚀 Aktueller Stand

### 🎯 Milestone 1 – Spring Boot Backend

**Projektidee:** To-Do-Liste  
**Tech-Stack:** Java 25 · Spring Boot 3.5 · Gradle 9 · REST API

#### **Endpoints**
- `GET /api/health` → prüft, ob der Server läuft (`{ "status": "UP" }`)
- `GET /api/todos` → liefert Beispiel-To-Dos als JSON

#### **Tests**
Erste JUnit-Tests mit MockMvc.

#### **Deployment**
Folgt in einem späteren Milestone (Render).

**Backend erreichbar unter:**
- http://localhost:8080/api/health
- http://localhost:8080/api/todos

---

### 🧩 Milestone 2 – Vue.js Frontend

Für den zweiten Meilenstein wurde ein **Vue.js-Frontend mit Vite** erstellt.  
Das Frontend rendert eine **To-Do-Liste** über eine eigene Unterkomponente  
(`TodoList.vue`), die ihre Einträge mithilfe von `v-for` anzeigt.

#### **Technologien**
- Node.js 24 · Vue 3 · Vite 7
- Single-Page-App mit Komponentenstruktur
- Verbindung zu späterem Spring-Backend geplant

#### **Struktur**
frontend/
├── src/
│ ├── components/
│ │ ├── HelloWorld.vue
│ │ └── TodoList.vue
│ ├── App.vue
│ ├── main.js
│ └── assets/
├── package.json
└── vite.config.js