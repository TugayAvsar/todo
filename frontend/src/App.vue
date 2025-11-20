<script setup>
import { ref, onMounted } from 'vue'
import TodoList from './components/TodoList.vue'

const todos = ref([])
const loading = ref(true)
const error = ref(null)

onMounted(async () => {
  try {
    const baseUrl = import.meta.env.VITE_API_BASE_URL

    if (!baseUrl) {
      throw new Error('API-URL fehlt. Prüfe deine .env Datei!')
    }

    const response = await fetch(baseUrl + '/api/todos')

    if (!response.ok) {
      throw new Error(`Backend-Fehler: HTTP ${response.status}`)
    }

    const data = await response.json().catch(() => {
      throw new Error('Antwort ist kein gültiges JSON.')
    })

    todos.value = data
  } catch (err) {
    console.error('FEHLER IM FRONTEND:', err)
    error.value = err.message
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <main>
    <h1>ToDo WebApp – Frontend</h1>
    <p>Dies ist das Vue-Frontend für mein WebTech-Projekt.</p>

    <!-- 1. Ladezustand -->
    <p v-if="loading">Lade Todos...</p>

    <!-- 2. Fehlerzustand: Text + Button -->
    <div v-else-if="error">
      <p style="color:red; font-weight:bold;">
        ❌ Fehler: {{ error }}
      </p>
      <button
          @click="window.location.reload()"
          style="background:red; color:white; padding:8px; border-radius:6px; border:none; cursor:pointer;"
      >
        Erneut versuchen
      </button>
    </div>

    <!-- 3. Erfolgszustand: Liste -->
    <TodoList v-else :todos="todos" />
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
