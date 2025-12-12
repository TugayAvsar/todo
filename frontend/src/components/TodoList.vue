<script setup>
defineProps({
  todos: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['toggle'])
</script>

<template>
  <section>
    <h2>Meine Todos</h2>

    <ul v-if="todos.length">
      <li v-for="todo in todos" :key="todo.id" style="display:flex; gap:.5rem; align-items:center;">
        <input
            type="checkbox"
            :checked="todo.completed"
            @change="emit('toggle', todo.id)"
        />

        <span :style="{ textDecoration: todo.completed ? 'line-through' : 'none' }">
          {{ todo.title }}
        </span>

        <small style="margin-left:auto;">
          erstellt am {{ new Date(todo.createdAt).toLocaleString() }}
        </small>
      </li>
    </ul>

    <p v-else>Keine Todos vorhanden.</p>
  </section>
</template>

<style scoped>
section { padding: 1rem; }
h2 { margin-bottom: 0.5rem; }
li { margin-bottom: 0.25rem; }
</style>
