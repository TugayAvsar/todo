package de.htwberlin.webtech.todo.api;

import de.htwberlin.webtech.todo.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@Import(RestExceptionHandler.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TodoService service;

    @Test
    @DisplayName("GET /api/todos should return list")
    void getTodos_shouldReturnList() throws Exception {
        when(service.findAll()).thenReturn(List.of(
                new TodoDto(1L, "Buy milk", false, Instant.parse("2025-01-01T00:00:00Z"))
        ));

        mvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Buy milk"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    @DisplayName("POST /api/todos should create todo")
    void createTodo_shouldReturnCreated() throws Exception {
        when(service.create("Buy milk")).thenReturn(
                new TodoDto(1L, "Buy milk", false, Instant.parse("2025-01-01T00:00:00Z"))
        );

        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Buy milk\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Buy milk"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("POST /api/todos should return 400 if title is blank")
    void createTodo_shouldReturnBadRequest_whenTitleBlank() throws Exception {
        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"   \"}"))
                .andExpect(status().isBadRequest());
    }

    // ✅ NEU: fehlendes Feld
    @Test
    @DisplayName("POST /api/todos should return 400 if title is missing")
    void createTodo_shouldReturnBadRequest_whenTitleMissing() throws Exception {
        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ✅ NEU: title=null
    @Test
    @DisplayName("POST /api/todos should return 400 if title is null")
    void createTodo_shouldReturnBadRequest_whenTitleNull() throws Exception {
        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /api/todos/{id}/toggle should toggle todo")
    void toggleTodo_shouldReturnOk() throws Exception {
        when(service.toggleCompleted(1L)).thenReturn(
                new TodoDto(1L, "Buy milk", true, Instant.parse("2025-01-01T00:00:00Z"))
        );

        mvc.perform(patch("/api/todos/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.completed").value(true));
    }

    // ✅ NEU: toggle auf nicht existente ID -> 404 (über RestExceptionHandler)
    @Test
    @DisplayName("PATCH /api/todos/{id}/toggle should return 404 if todo not found")
    void toggleTodo_shouldReturnNotFound_whenTodoMissing() throws Exception {
        when(service.toggleCompleted(999L))
                .thenThrow(new IllegalArgumentException("Todo nicht gefunden: 999"));

        mvc.perform(patch("/api/todos/999/toggle"))
                .andExpect(status().isNotFound());
    }
}
