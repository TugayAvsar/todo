package de.htwberlin.webtech.todo.service;

import de.htwberlin.webtech.todo.api.TodoDto;
import de.htwberlin.webtech.todo.persistence.TodoEntity;
import de.htwberlin.webtech.todo.persistence.TodoRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest implements WithAssertions {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoService underTest;

    @Test
    @DisplayName("findAll should return mapped TodoDto list")
    void findAll_should_return_mapped_dtos() {
        // given
        TodoEntity e1 = mock(TodoEntity.class);
        TodoEntity e2 = mock(TodoEntity.class);

        Instant t1 = Instant.parse("2026-01-01T10:00:00Z");
        Instant t2 = Instant.parse("2026-01-02T10:00:00Z");

        when(e1.getId()).thenReturn(1L);
        when(e1.getTitle()).thenReturn("A");
        when(e1.isCompleted()).thenReturn(false);
        when(e1.getCreatedAt()).thenReturn(t1);

        when(e2.getId()).thenReturn(2L);
        when(e2.getTitle()).thenReturn("B");
        when(e2.isCompleted()).thenReturn(true);
        when(e2.getCreatedAt()).thenReturn(t2);

        doReturn(List.of(e1, e2)).when(repository).findAll();

        // when
        List<TodoDto> result = underTest.findAll();

        // then
        verify(repository).findAll();
        assertThat(result).hasSize(2);

        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).title()).isEqualTo("A");
        assertThat(result.get(0).completed()).isFalse();
        assertThat(result.get(0).createdAt()).isEqualTo(t1);

        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).title()).isEqualTo("B");
        assertThat(result.get(1).completed()).isTrue();
        assertThat(result.get(1).createdAt()).isEqualTo(t2);
    }

    @Test
    @DisplayName("create should save new entity with completed=false and return dto")
    void create_should_save_new_entity_and_return_dto() {
        // given
        String title = "Buy milk";

        // Save gibt Entity zurück (mit ID/createdAt)
        TodoEntity saved = mock(TodoEntity.class);
        Instant createdAt = Instant.parse("2026-01-03T10:00:00Z");

        when(saved.getId()).thenReturn(10L);
        when(saved.getTitle()).thenReturn(title);
        when(saved.isCompleted()).thenReturn(false);
        when(saved.getCreatedAt()).thenReturn(createdAt);

        // wir capturen das Entity, das an save übergeben wird
        ArgumentCaptor<TodoEntity> captor = ArgumentCaptor.forClass(TodoEntity.class);
        doReturn(saved).when(repository).save(any(TodoEntity.class));

        // when
        TodoDto result = underTest.create(title);

        // then
        verify(repository).save(captor.capture());
        TodoEntity toSave = captor.getValue();

        assertThat(toSave.getTitle()).isEqualTo(title);
        assertThat(toSave.isCompleted()).isFalse();
        assertThat(toSave.getCreatedAt()).isNotNull(); // Instant.now()

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.completed()).isFalse();
        assertThat(result.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("toggleCompleted should flip completed and save")
    void toggleCompleted_should_flip_completed_and_save() {
        // given
        Long id = 5L;

        TodoEntity existing = mock(TodoEntity.class);
        when(existing.isCompleted()).thenReturn(false); // vorher false
        doReturn(Optional.of(existing)).when(repository).findById(id);

        TodoEntity saved = mock(TodoEntity.class);
        Instant createdAt = Instant.parse("2026-01-04T10:00:00Z");
        when(saved.getId()).thenReturn(id);
        when(saved.getTitle()).thenReturn("X");
        when(saved.isCompleted()).thenReturn(true); // nach Toggle true
        when(saved.getCreatedAt()).thenReturn(createdAt);

        doReturn(saved).when(repository).save(existing);

        // when
        TodoDto result = underTest.toggleCompleted(id);

        // then
        verify(repository).findById(id);
        verify(existing).setCompleted(true); // flip von false -> true
        verify(repository).save(existing);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.completed()).isTrue();
    }

    @Test
    @DisplayName("toggleCompleted should throw if todo not found")
    void toggleCompleted_should_throw_if_not_found() {
        // given
        Long id = 999L;
        doReturn(Optional.empty()).when(repository).findById(id);

        // when / then
        assertThatThrownBy(() -> underTest.toggleCompleted(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Todo nicht gefunden");

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }
}
