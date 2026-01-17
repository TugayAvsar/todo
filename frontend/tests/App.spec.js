import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { mount } from "@vue/test-utils";
import App from "../src/App.vue";

function flushPromises() {
    return new Promise((resolve) => setTimeout(resolve, 0));
}

describe("App.vue", () => {
    const originalEnv = { ...import.meta.env };

    beforeEach(() => {
        vi.restoreAllMocks();
        // Default: API Base URL vorhanden
        import.meta.env.VITE_API_BASE_URL = "http://example.com";
    });

    afterEach(() => {
        import.meta.env.VITE_API_BASE_URL = originalEnv.VITE_API_BASE_URL;
    });

    it("zeigt zuerst 'Lade Todos...' und rendert danach die Liste (GET /api/todos)", async () => {
        // given
        vi.stubGlobal(
            "fetch",
            vi.fn().mockResolvedValueOnce({
                ok: true,
                json: async () => [
                    {
                        id: 1,
                        title: "Buy milk",
                        completed: false,
                        createdAt: "2025-01-01T00:00:00Z",
                    },
                ],
            })
        );

        const wrapper = mount(App);

        // direkt nach Mount sollte Loading sichtbar sein
        expect(wrapper.text()).toContain("Lade Todos...");

        // wait: fetch + state update + re-render
        await flushPromises();
        await wrapper.vm.$nextTick();

        // then: TodoList Inhalt sichtbar
        expect(wrapper.text()).toContain("Meine Todos");
        expect(wrapper.text()).toContain("Buy milk");
    });

    it("zeigt Fehler wenn VITE_API_BASE_URL fehlt", async () => {
        // given
        import.meta.env.VITE_API_BASE_URL = "";

        // when
        const wrapper = mount(App);
        await flushPromises();
        await wrapper.vm.$nextTick();

        // then
        expect(wrapper.text()).toContain("❌ Fehler:");
        expect(wrapper.text()).toContain("API-URL fehlt");
    });

    it("createTodo: klick auf 'Hinzufügen' macht POST und danach reload (GET) und leert Input", async () => {
        // GET initial
        const fetchMock = vi.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => [],
            })
            // POST create
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ id: 1 }),
            })
            // GET reload
            .mockResolvedValueOnce({
                ok: true,
                json: async () => [
                    {
                        id: 1,
                        title: "Buy milk",
                        completed: false,
                        createdAt: "2025-01-01T00:00:00Z",
                    },
                ],
            });

        vi.stubGlobal("fetch", fetchMock);

        const wrapper = mount(App);

        await flushPromises();
        await wrapper.vm.$nextTick();

        // set input
        const input = wrapper.get("input[placeholder='Neues Todo...']");
        await input.setValue("Buy milk");

        // click button
        await wrapper.get("button").trigger("click");

        await flushPromises();
        await wrapper.vm.$nextTick();

        // POST called with correct payload
        expect(fetchMock).toHaveBeenCalledWith(
            "http://example.com/api/todos",
            expect.objectContaining({
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ title: "Buy milk" }),
            })
        );

        // Input cleared
        expect(input.element.value).toBe("");

        // List updated
        expect(wrapper.text()).toContain("Buy milk");
    });

    it("createTodo: wenn title nur Spaces ist -> kein fetch POST", async () => {
        // GET initial
        const fetchMock = vi.fn().mockResolvedValueOnce({
            ok: true,
            json: async () => [],
        });
        vi.stubGlobal("fetch", fetchMock);

        const wrapper = mount(App);

        await flushPromises();
        await wrapper.vm.$nextTick();

        const input = wrapper.get("input[placeholder='Neues Todo...']");
        await input.setValue("   ");

        await wrapper.get("button").trigger("click");

        // only initial GET called, no POST
        expect(fetchMock).toHaveBeenCalledTimes(1);
    });

    it("toggleTodo: wenn TodoList toggle emittet -> PATCH und danach reload GET", async () => {
        const fetchMock = vi.fn()
            // initial GET
            .mockResolvedValueOnce({
                ok: true,
                json: async () => [
                    {
                        id: 1,
                        title: "Buy milk",
                        completed: false,
                        createdAt: "2025-01-01T00:00:00Z",
                    },
                ],
            })
            // PATCH
            .mockResolvedValueOnce({ ok: true })
            // reload GET
            .mockResolvedValueOnce({
                ok: true,
                json: async () => [
                    {
                        id: 1,
                        title: "Buy milk",
                        completed: true,
                        createdAt: "2025-01-01T00:00:00Z",
                    },
                ],
            });

        vi.stubGlobal("fetch", fetchMock);

        const wrapper = mount(App);

        await flushPromises();
        await wrapper.vm.$nextTick();

        // emit toggle from child (TodoList)
        const todoList = wrapper.getComponent({ name: "TodoList" });
        await todoList.vm.$emit("toggle", 1);

        await flushPromises();
        await wrapper.vm.$nextTick();

        expect(fetchMock).toHaveBeenCalledWith(
            "http://example.com/api/todos/1/toggle",
            expect.objectContaining({ method: "PATCH" })
        );

        expect(wrapper.text()).toContain("Buy milk");
    });

    it("zeigt Error bei Backend-Fehler (GET not ok) und 'Erneut versuchen' triggert reload", async () => {
        const fetchMock = vi.fn()
            // first GET fails
            .mockResolvedValueOnce({ ok: false, status: 500 })
            // retry GET succeeds
            .mockResolvedValueOnce({
                ok: true,
                json: async () => [],
            });

        vi.stubGlobal("fetch", fetchMock);

        const wrapper = mount(App);

        await flushPromises();
        await wrapper.vm.$nextTick();

        // error visible
        expect(wrapper.text()).toContain("❌ Fehler:");
        expect(wrapper.text()).toContain("Backend-Fehler: HTTP 500");

        // click retry
        const retryBtn = wrapper.get("button");
        await retryBtn.trigger("click");

        await flushPromises();
        await wrapper.vm.$nextTick();

        // after retry, should show TodoList empty state
        expect(wrapper.text()).toContain("Meine Todos");
        expect(wrapper.text()).toContain("Keine Todos vorhanden.");
    });
});
