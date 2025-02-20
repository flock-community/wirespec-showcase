import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const root = "src/main/react";
  console.log("Running in " + mode + " mode");
  const env = loadEnv(mode, process.cwd() + "/" + root, "VITE");
  console.log("All env vars:", env);
  return {
    root: "src/main/react",
    plugins: [react()],

    build: {
      // Relative to the root
      outDir: "../../../target/classes/static",
      manifest: true,
      sourcemap: "hidden",
      emptyOutDir: true,
    },
    server: {
      host: "0.0.0.0",
      port: 3000,
      proxy: {
        "/api/move-money": `http://${env.VITE_SERVER_MOVE_MONEY || "localhost"}:8080`,
        "/api/audits": `http://${env.VITE_SERVER_AUDIT || "localhost"}:8082`,
      },
    },
  };
});
