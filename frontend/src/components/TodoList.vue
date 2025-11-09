<script setup>
const props = defineProps({
  todos: {
    type: Array,
    required: true
  }
})
</script>

<template>
  <section>
    <h2>Meine Todos</h2>

    <ul v-if="todos.length">
      <li
          v-for="todo in todos"
          :key="todo.id"
      >
        <span :style="{ textDecoration: todo.completed ? 'line-through' : 'none' }">
          {{ todo.title }}
        </span>
        <small> – erstellt am {{ new Date(todo.createdAt).toLocaleString() }}</small>
      </li>
    </ul>

    <p v-else>Keine Todos vorhanden.</p>
  </section>
</template>

<style scoped>
section {
  padding: 1rem;
}
h2 {
  margin-bottom: 0.5rem;
}
li {
  margin-bottom: 0.25rem;
}
</style>
