<script setup>
import { ref, onMounted } from 'vue'
import TodoList from './components/TodoList.vue'

const todos = ref([])

// Dummy-Daten (später kommen sie aus dem Backend)
const initialTodos = [
  { id: 1, title: 'Milestone 1 abschließen', completed: true, createdAt: new Date().toISOString() },
  { id: 2, title: 'Milestone 2: Vue-Frontend erstellen', completed: false, createdAt: new Date().toISOString() },
  { id: 3, title: 'Todo-Komponente mit v-for anzeigen', completed: false, createdAt: new Date().toISOString() }
]

onMounted(() => {
  todos.value = initialTodos
})
</script>

<template>
  <main>
    <h1>ToDo WebApp – Frontend</h1>
    <p>Dies ist das Vue-Frontend für mein WebTech-Projekt.</p>

    <TodoList :todos="todos" />
  </main>
</template>

<style scoped>
main {
  max-width: 800px;
  margin: 2rem auto;
  padding: 1rem;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}
h1 {
  margin-bottom: 0.5rem;
}
p {
  margin-bottom: 1rem;
}
</style>
