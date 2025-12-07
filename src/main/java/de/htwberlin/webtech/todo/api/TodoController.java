package de.htwberlin.webtech.todo.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin  // erlaubt alle Origins (auch dein Render-Frontend)
public class TodoController {

    @GetMapping("/api/todos")
    public List<TodoDto> getTodos() {
        return List.of(
                new TodoDto(1L, "Milestone 1 abschließen", true, Instant.now()),
                new TodoDto(2L, "GET-Route testen", true, Instant.now()),
                new TodoDto(3L, "README aktualisieren", true, Instant.now())
        );
    }
}
