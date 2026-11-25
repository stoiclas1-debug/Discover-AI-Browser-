import assert from "node:assert/strict";
import { afterEach, describe, it } from "node:test";
import type { FastifyInstance } from "fastify";
import { buildServer } from "../src/server.js";

describe("GET /api/v1/health", () => {
  let app: FastifyInstance | undefined;

  afterEach(async () => {
    await app?.close();
    app = undefined;
  });

  it("returns the non-sensitive process health response", async () => {
    app = await buildServer({ NODE_ENV: "test" });
    const response = await app.inject({ method: "GET", url: "/api/v1/health" });

    assert.equal(response.statusCode, 200);
    assert.deepEqual(response.json(), {
      status: "ok",
      service: "discover-ai-backend",
    });
  });
});
