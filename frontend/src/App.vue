<script setup>
import { ref, onMounted } from 'vue'
import TodoList from './components/TodoList.vue'

const todos = ref([])
const loading = ref(true)
const error = ref(null)

const newTitle = ref('')
const apiBase = import.meta.env.VITE_API_BASE_URL

async function loadTodos() {
  loading.value = true
  error.value = null

  try {
    if (!apiBase) {
      throw new Error('API-URL fehlt. Prüfe deine .env Datei!')
    }

    const response = await fetch(apiBase + '/api/todos')

    if (!response.ok) {
      throw new Error(`Backend-Fehler: HTTP ${response.status}`)
    }

    todos.value = await response.json()
  } catch (err) {
    console.error('FEHLER IM FRONTEND:', err)
    error.value = err.message
  } finally {
    loading.value = false
  }
}

async function createTodo() {
  error.value = null

  try {
    if (!newTitle.value.trim()) return

    const response = await fetch(apiBase + '/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: newTitle.value })
    })

    if (!response.ok) {
      throw new Error(`POST fehlgeschlagen: HTTP ${response.status}`)
    }

    newTitle.value = ''
    await loadTodos()
  } catch (err) {
    console.error('FEHLER IM FRONTEND (POST):', err)
    error.value = err.message
  }
}

async function toggleTodo(id) {
  error.value = null

  try {
    const response = await fetch(apiBase + `/api/todos/${id}/toggle`, {
      method: 'PATCH'
    })

    if (!response.ok) {
      throw new Error(`PATCH fehlgeschlagen: HTTP ${response.status}`)
    }

    await loadTodos()
  } catch (err) {
    console.error('FEHLER IM FRONTEND (PATCH):', err)
    error.value = err.message
  }
}

onMounted(loadTodos)
</script>

<template>
  <main>
    <h1>ToDo WebApp – Frontend</h1>
    <p>Dies ist das Vue-Frontend für mein WebTech-Projekt.</p>

    <!-- Todo anlegen -->
    <div style="margin: 1rem 0; display: flex; gap: 0.5rem;">
      <input
          v-model="newTitle"
          placeholder="Neues Todo..."
          style="flex: 1; padding: 8px; border-radius: 6px; border: 1px solid #444;"
      />
      <button
          @click="createTodo"
          style="padding:8px 12px; border-radius:6px; border:none; cursor:pointer;"
      >
        Hinzufügen
      </button>
    </div>

    <p v-if="loading">Lade Todos...</p>

    <div v-else-if="error">
      <p style="color:red; font-weight:bold;">
        ❌ Fehler: {{ error }}
      </p>
      <button
          @click="loadTodos"
          style="background:red; color:white; padding:8px; border-radius:6px; border:none; cursor:pointer;"
      >
        Erneut versuchen
      </button>
    </div>

    <TodoList v-else :todos="todos" @toggle="toggleTodo" />
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
