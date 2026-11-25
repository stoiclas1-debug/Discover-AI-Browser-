import pg from "pg";
import type { Environment } from "./env.js";

const { Pool } = pg;

/** Create a backend-only pool when PostgreSQL is configured; no client code imports this module. */
export function createDatabasePool(
  environment: Environment,
): pg.Pool | undefined {
  if (!environment.DATABASE_URL) return undefined;

  return new Pool({
    connectionString: environment.DATABASE_URL,
    max: 10,
    idleTimeoutMillis: 30_000,
    connectionTimeoutMillis: 5_000,
    ...(environment.NODE_ENV === "production"
      ? { ssl: { rejectUnauthorized: true } }
      : {}),
  });
}
