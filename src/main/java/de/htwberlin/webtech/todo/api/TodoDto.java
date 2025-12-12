package de.htwberlin.webtech.todo.api;

import java.time.Instant;

public record TodoDto(
        Long id,
        String title,
        boolean completed,
        Instant createdAt
) {}
