import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    // baseUrl: 'http://ip23ssa3.sit.kmutt.ac.th',
    baseUrl: 'http://localhost:5173',
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
