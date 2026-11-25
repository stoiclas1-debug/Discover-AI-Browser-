import { z } from "zod";

const optionalDatabaseUrl = z.preprocess(
  (value: unknown) => (value === "" ? undefined : value),
  z
    .string()
    .url()
    .refine(
      (value) =>
        value.startsWith("postgres://") || value.startsWith("postgresql://"),
      {
        message: "DATABASE_URL must use postgres:// or postgresql://",
      },
    )
    .optional(),
);

const environmentSchema = z.object({
  NODE_ENV: z
    .enum(["development", "test", "production"])
    .default("development"),
  PORT: z.coerce.number().int().min(1).max(65_535).default(3000),
  DATABASE_URL: optionalDatabaseUrl,
  AI_PROVIDER: z.enum(["mock"]).default("mock"),
});

export type Environment = z.infer<typeof environmentSchema>;

export function parseEnvironment(
  input: NodeJS.ProcessEnv = process.env,
): Environment {
  return environmentSchema.parse(input);
}
