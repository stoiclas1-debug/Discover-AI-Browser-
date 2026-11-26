# Discover AI Browser — Architecture

## Status and authority

This document records the agreed Phase 1 direction: Android is the first-class client, the backend is a separate TypeScript service, and the existing root `README.md` is preserved verbatim. The repository README currently contains only a short branding request, not the product requirements described in the initial project brief. This architecture therefore records only the explicit Phase 1 requirements and confirmed Android-first scope; it does not invent broader product behavior.

As of this foundation branch, this document describes the intended module boundaries. See `REPOSITORY_AUDIT.md` and the pull request for actual implementation and validation evidence. Capabilities are not considered complete merely because a module is planned here.

## Repository boundaries

| Path                                       | Responsibility                                                                                                     | Phase 1 status                                                                   |
| ------------------------------------------ | ------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------- |
| `android/`                                 | Native Kotlin app, Compose interface, Android platform APIs and WebView-backed browser shell                       | Foundation in progress; only implemented controls listed in the PR are available |
| `backend/`                                 | Node.js/TypeScript versioned REST API, runtime configuration, PostgreSQL connection boundary and provider adapters | Foundation in progress; health endpoint and mock adapter only                    |
| `docs/`                                    | Architecture, installation, API contract, privacy, threat model, roadmap and audit evidence                        | Active documentation                                                             |
| `.github/`                                 | CI, dependency updates, security scans, contribution templates and issue forms                                     | Repository automation                                                            |
| `clients/ios`, browser extensions, desktop | Future client modules                                                                                              | Deferred; no placeholder source directories are created                          |

The Android app must not contain model-provider credentials. AI-provider calls, when approved and implemented, belong in backend adapters. PostgreSQL is a server-side dependency; the Android client must not connect to it directly.

## Android client

The first client uses Kotlin, Android Studio/Android SDK, Jetpack Compose, and native Android APIs. The initial shell provides a browser surface, address/search input, and basic back/forward/reload/home navigation using Android `WebView`. These are the only browser behaviors that may be described as implemented if they are present and pass tests in this branch.

Plain search phrases currently use Google Search, a provisional choice that shares the phrase with Google. This is not a finalized product requirement; configurable search-provider selection and a privacy review remain open. HTTP navigation is permitted, and the shell displays an unencrypted-connection warning for HTTP pages. The initial browser surface is not a complete browser product. Tab persistence, bookmark/history/download managers, settings, private browsing, account sync, and the AI assistant remain separate outcomes. Android WebView does not equal embedding a separately managed Chromium build. WebView defaults should remain restrictive: JavaScript disabled unless a reviewed product requirement justifies it; file/content access disabled; mixed content blocked; only HTTP(S) navigation accepted (except an internal blank document); safe browsing enabled where supported; no broad runtime permissions. Any necessary relaxation requires focused tests and a threat-model update.

## Backend and API

The backend is TypeScript on supported Node.js LTS, with a versioned REST API rooted at `/api/v1`, OpenAPI documentation, runtime configuration validation, and a PostgreSQL connection-pool boundary. `GET /api/v1/health` is an operational health check and does not imply database-backed product features exist. The pool factory is prepared but is not currently instantiated by a route or verified against a running PostgreSQL service. Development uses a mock provider adapter. A real model adapter must be added only behind server-side configuration and never ship provider keys to Android.

Database schema and durable user data are intentionally not defined in this foundation because account, retention, and product-data requirements have not been approved. Do not apply destructive or production migrations without separate review.

## Other platforms

Chromium/Electron desktop, iOS, and Chrome/Edge/Firefox extensions are deferred. No empty directories or placeholder apps are created for them. Before any desktop implementation, evaluate Electron's bundled Chromium/Node security model against an alternative Chromium embedding strategy; specify update cadence, sandboxing, process isolation, extension model, and distribution signing. Do not assume Electron is a drop-in equivalent to a native Android WebView.

## Security and privacy boundaries

Treat browser pages as untrusted content. Separate application chrome from web content, restrict navigation schemes and privileged bridges, avoid exposing native interfaces to arbitrary origins, and do not pass browser contents to an AI provider without explicit user action and clear disclosure. Minimize URL, history, page-content, and identifier collection. Transport provider requests through the backend. Keep secrets in server-side secret storage, not source control or client bundles. See `PRIVACY.md` and `THREAT_MODEL.md`.
