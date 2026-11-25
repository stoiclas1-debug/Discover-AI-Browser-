# API Documentation

## Contract status

The current API is a foundation only. OpenAPI source is maintained in [`../backend/openapi.yaml`](../backend/openapi.yaml). No account, browsing-history, bookmark, download, tab-sync, or AI-generation endpoint is part of this release. Those require product and privacy approval first.

## Base URL and versioning

Local development uses `http://localhost:3000`. All application endpoints are versioned under `/api/v1`; breaking contract changes require an explicitly versioned path or a reviewed migration policy.

## `GET /api/v1/health`

Returns process-level health for monitoring. It does not expose configuration, database connection strings, provider status details, or secrets. Example:

```json
{
  "status": "ok",
  "service": "discover-ai-backend"
}
```

A successful health response does not prove PostgreSQL is configured or that any browser/AI product feature is operational. Readiness checks and database-dependent routes must be documented separately if introduced.

## Errors and security

Errors should use a stable JSON shape with a safe public message and request correlation identifier; stack traces and configuration values belong only in controlled server logs. Do not place browser page contents, credentials, or sensitive URLs into logs. Apply input validation at the API boundary, bounded request sizes, secure headers, and rate limits before exposing user-facing or AI endpoints.
