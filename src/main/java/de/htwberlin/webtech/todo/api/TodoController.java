package de.htwberlin.webtech.todo.api;

import de.htwberlin.webtech.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*") // für Vue-Frontend (Render)
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TodoDto> getTodos() {
        return service.findAll();
    }

    public record CreateTodoRequest(String title) {}

    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody CreateTodoRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        TodoDto created = service.create(request.title());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/toggle")
    public TodoDto toggleTodo(@PathVariable Long id) {
        return service.toggleCompleted(id);
    }
}
