import { vi } from "vitest";

// Default API Base für Tests (App.vue liest VITE_API_BASE_URL)
process.env.VITE_API_BASE_URL = "http://localhost:8080";

// Fetch mocken wir in den Tests jeweils konkret.
// Aber: falls mal ein Test vergisst zu mocken -> klare Fehlermeldung.
global.fetch = vi.fn(() => {
    throw new Error("fetch wurde im Test nicht gemockt!");
});
