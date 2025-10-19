# 📝 ToDo WebApp — HTW Berlin WebTechnologien

##  Aktueller Stand (Milestone 1)

Projektidee: To-Do-Liste

Tech-Stack: Java 25 · Spring Boot 3.5 · Gradle 9 · REST API

## **Endpoints:**

GET /api/health → prüft, ob der Server läuft ({ "status": "UP" })

GET /api/todos → liefert Beispiel-To-Dos als JSON

## **Tests:** Erste JUnit-Tests mit MockMvc

Deployment: folgt in späterem Milestone (Render)

Repository: https://github.com/TugayAvsar/todo

## Lokale Ausführung

./gradlew bootRun


Das Backend ist unter 
http://localhost:8080/api/health

und
http://localhost:8080/api/todos

erreichbar.

## **Autor**

Tugay Avsar

GitHub → https://github.com/TugayAvsar/todo