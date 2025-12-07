package de.htwberlin.webtech.todo.api;

import java.time.Instant;

/**
 * Einfache Todo-Datenklasse für die REST-API.
 */
public record TodoDto(
        Long id,
        String title,
        boolean completed,
        Instant createdAt
) {
}
