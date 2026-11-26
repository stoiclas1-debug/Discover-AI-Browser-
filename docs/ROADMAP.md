# Roadmap

This roadmap reflects the confirmed decision to prioritize a native Android MVP and defer Chromium/Electron desktop. Each item remains planned until its implementation and tests are reviewed. The repository's existing README is preserved and is not a complete product specification; product-level behavior and acceptance criteria should be approved before expanding beyond this foundation.

## Phase 1 — Verifiable foundation

- Create the Android Kotlin/Jetpack Compose shell and establish a repeatable Android build in CI.
- Provide a minimal browser surface with address/search submission and basic navigation, while retaining restrictive WebView settings.
- Establish the TypeScript backend workspace, configuration validation, `/api/v1/health`, OpenAPI contract, PostgreSQL boundary, and mock AI-provider interface.
- Add formatting, linting, type-checking, unit tests, Android build checks, dependency update/scanning, and secret scanning.
- Complete architecture, installation, API, privacy, threat-model, and repository-audit documentation.
- Keep the phase reviewable in a feature-branch PR; do not begin Phase 2 before review of actual build/test/security results.

## Phase 2 — Android browsing requirements (deferred)

Approve a product specification and acceptance criteria before implementing robust tab lifecycle/management, bookmarks, history, downloads, settings, site permissions, data clearing, and private browsing. Each item needs lifecycle, persistence, accessibility, offline/error, and privacy behavior defined. Private browsing must have verifiable non-persistence properties rather than a private label alone.

## Phase 3 — AI assistant (deferred)

Define consent UX, supported page/context inputs, data minimization, provider policy, retention, abuse controls, cancellation, rate limits, and error handling. Implement only through a server-side provider interface; begin with mock responses, then use external credentials stored server-side when the owner approves the provider and deployment. Never embed keys in Android.

## Phase 4 — Desktop and additional clients (deferred)

Evaluate Electron versus a Chromium embedding approach using update/security support, process isolation, sandboxing, native bridge exposure, extension compatibility, signing, and distribution. Separately scope iOS and browser extensions. Reuse API contracts where appropriate, but do not force identical implementation architecture across platform APIs.

## Release gates

A phase is ready for review only with a scoped specification, passing relevant CI, dependency/security review, a privacy/threat-model update for changed data flows, and honest documentation of limitations. Production deployment, live provider credentials, signing credentials, user-account models, data migrations, and public release require separate approval and secure configuration.
