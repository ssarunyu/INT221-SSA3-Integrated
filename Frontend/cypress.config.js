import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    // baseUrl: 'http://intproj23.sit.kmutt.ac.th/ssa3/',
    baseUrl: 'http://localhost:5173',
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
