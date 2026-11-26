import assert from "node:assert/strict";
import { describe, it } from "node:test";
import { parseEnvironment } from "../src/config/env.js";

describe("parseEnvironment", () => {
  it("uses safe development defaults and the mock provider", () => {
    const environment = parseEnvironment({ NODE_ENV: "test" });

    assert.equal(environment.NODE_ENV, "test");
    assert.equal(environment.PORT, 3000);
    assert.equal(environment.DATABASE_URL, undefined);
    assert.equal(environment.AI_PROVIDER, "mock");
  });

  it("rejects invalid ports", () => {
    assert.throws(() => parseEnvironment({ PORT: "70000" }));
  });

  it("allows production health configuration without an unused database", () => {
    const environment = parseEnvironment({ NODE_ENV: "production" });
    assert.equal(environment.DATABASE_URL, undefined);
  });

  it("validates an optional PostgreSQL URL when configured", () => {
    assert.throws(
      () =>
        parseEnvironment({
          NODE_ENV: "production",
          DATABASE_URL: "https://example.com",
        }),
      /postgres/,
    );
  });
});
