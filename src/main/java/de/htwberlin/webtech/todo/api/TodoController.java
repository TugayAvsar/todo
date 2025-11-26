package de.htwberlin.webtech.todo.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    // einfache DTO-Record für Milestone 1 (später ersetzen wir das durch Entity/DTO)
    public record Todo(Long id, String title, boolean completed, Instant createdAt) {}

    @GetMapping
    public List<Todo> list() {
        return List.of(
                new Todo(1L, "Milestone 1 abschließen", true, Instant.now()),
                new Todo(2L, "GET-Route testen", true, Instant.now()),
                new Todo(3L, "README aktualisieren", true, Instant.now())
        );
    }
}
