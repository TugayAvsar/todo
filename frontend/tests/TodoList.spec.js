import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";
import TodoList from "../src/components/TodoList.vue";

describe("TodoList.vue", () => {
    it("rendert Überschrift und Liste mit Items", () => {
        const todos = [
            { id: 1, title: "Buy milk", completed: false, createdAt: "2025-01-01T00:00:00Z" },
            { id: 2, title: "Study", completed: true, createdAt: "2025-01-02T00:00:00Z" }
        ];

        const wrapper = mount(TodoList, {
            props: { todos }
        });

        expect(wrapper.get("h2").text()).toBe("Meine Todos");

        const items = wrapper.findAll("li");
        expect(items.length).toBe(2);

        expect(items[0].text()).toContain("Buy milk");
        expect(items[1].text()).toContain("Study");
    });

    it("zeigt 'Keine Todos vorhanden.' wenn Array leer ist", () => {
        const wrapper = mount(TodoList, {
            props: { todos: [] }
        });

        expect(wrapper.text()).toContain("Keine Todos vorhanden.");
        expect(wrapper.findAll("li").length).toBe(0);
    });

    it("zeigt line-through Style wenn Todo completed=true ist", () => {
        const todos = [
            { id: 1, title: "Done", completed: true, createdAt: "2025-01-01T00:00:00Z" }
        ];

        const wrapper = mount(TodoList, { props: { todos } });

        const span = wrapper.get("span");
        expect(span.attributes("style")).toContain("line-through");
    });

    it("emittet 'toggle' mit id wenn Checkbox geändert wird", async () => {
        const todos = [
            { id: 99, title: "Toggle me", completed: false, createdAt: "2025-01-01T00:00:00Z" }
        ];

        const wrapper = mount(TodoList, { props: { todos } });

        const checkbox = wrapper.get('input[type="checkbox"]');
        await checkbox.trigger("change");

        const emitted = wrapper.emitted("toggle");
        expect(emitted).toBeTruthy();
        expect(emitted[0]).toEqual([99]);
    });
});
