import Fastify, { type FastifyInstance } from "fastify";
import helmet from "@fastify/helmet";
import rateLimit from "@fastify/rate-limit";
import { parseEnvironment } from "./config/env.js";

export async function buildServer(
  input: NodeJS.ProcessEnv = process.env,
): Promise<FastifyInstance> {
  parseEnvironment(input);
  const app = Fastify({
    logger: false,
    trustProxy: false,
    bodyLimit: 1_048_576,
  });

  await app.register(helmet);
  await app.register(rateLimit, { max: 120, timeWindow: "1 minute" });

  app.get("/api/v1/health", async () => ({
    status: "ok",
    service: "discover-ai-backend",
  }));

  return app;
}
