import "dotenv/config";
import { ZodError } from "zod";
import { parseEnvironment } from "./config/env.js";
import { buildServer } from "./server.js";

try {
  const environment = parseEnvironment();
  const app = await buildServer();
  await app.listen({ host: "0.0.0.0", port: environment.PORT });
} catch (error) {
  if (error instanceof ZodError) {
    const details = error.issues.map(
      (issue) => `${issue.path.join(".") || "config"}: ${issue.message}`,
    );
    process.stderr.write(
      `Backend configuration is invalid: ${details.join("; ")}\n`,
    );
  } else {
    process.stderr.write(
      "Backend startup failed. Inspect sanitized server diagnostics.\n",
    );
  }
  process.exitCode = 1;
}
