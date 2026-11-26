# Contributing

Thanks for helping improve Discover AI Browser. During Phase 1, Android is the priority client and the backend is a TypeScript/Node.js service. iOS, browser extensions, Chromium/Electron desktop, and live AI providers are deferred; see [`docs/ROADMAP.md`](docs/ROADMAP.md).

## Before starting

- Read [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md), [`docs/THREAT_MODEL.md`](docs/THREAT_MODEL.md), and [`docs/PRIVACY.md`](docs/PRIVACY.md).
- Open or locate an issue before beginning substantial work so the scope and acceptance criteria are visible.
- Work on a topic branch. Do not commit directly to `main`.

## Local checks

- Backend: `npm ci`, then `npm run check` at the repository root.
- Android: open `android/` in Android Studio or run `./gradlew -p android testDebugUnitTest assembleDebug` after installing the Android SDK and accepting required licenses.
- Do not report a check as passing unless it was executed and its actual result is recorded.

## Change expectations

- Keep changes focused and reviewable. Preserve the current `README.md` until a separately approved product description is available.
- Add or update tests for behavior changes. Keep HTTP paths versioned under `/api/v1`.
- Do not add provider credentials, production data, signing keys, or generated build outputs. Use `.env.example` only for non-secret key names and safe defaults.
- Never place provider credentials in an Android asset, resource, or source file. Provider access belongs behind the backend.
- Treat WebView JavaScript, file access, downloads, permissions, deep links, and external navigation as security-sensitive. Document and test any relaxation of safe defaults.
- Avoid collecting browsing content, URLs, history, or identifiers unless an approved feature requires it and privacy requirements are documented.

## Pull requests

- Use the pull-request template and describe scope, architecture decisions, test/build results, and known limitations.
- Keep documentation and the changelog in sync with user-visible or architectural changes.
- Request review before merging. Do not merge until required CI and security checks pass.
