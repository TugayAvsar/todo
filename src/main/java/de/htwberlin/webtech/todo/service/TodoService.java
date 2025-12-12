package de.htwberlin.webtech.todo.service;

import de.htwberlin.webtech.todo.api.TodoDto;
import de.htwberlin.webtech.todo.persistence.TodoEntity;
import de.htwberlin.webtech.todo.persistence.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public List<TodoDto> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public TodoDto create(String title) {
        TodoEntity entity = new TodoEntity(
                title,
                false,
                Instant.now()
        );
        TodoEntity saved = repository.save(entity);
        return toDto(saved);
    }

    public TodoDto toggleCompleted(Long id) {
        TodoEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo nicht gefunden: " + id));

        entity.setCompleted(!entity.isCompleted());

        TodoEntity saved = repository.save(entity);
        return toDto(saved);
    }

    private TodoDto toDto(TodoEntity entity) {
        return new TodoDto(
                entity.getId(),
                entity.getTitle(),
                entity.isCompleted(),
                entity.getCreatedAt()
        );
    }
}
