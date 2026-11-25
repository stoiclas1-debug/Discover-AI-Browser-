# Privacy Notes

## Current foundation

This repository begins without analytics, accounts, sync, or AI page-context processing. That is a statement about the current source only and must be rechecked as features are added. The Android WebView shell uses platform-managed web storage/cookies unless a later implementation explicitly controls or clears them; therefore the initial shell is not private browsing and must not be marketed as such. Navigated sites receive the user's network requests and may set their own cookies or local storage.

In the initial shell, plain search phrases are submitted to Google Search. This is a provisional implementation choice, not a final product requirement; search text is sent to that service. A configurable search provider and a privacy review are still outstanding. The health API returns service status only. Database support is a backend boundary; no user browsing data schema is defined. The mock AI provider is development-only and should not receive real browsing content.

## Required privacy rules for future changes

Before adding telemetry, accounts, history/bookmark synchronization, downloads, page summarization, or a real AI provider, document what data is collected, the purpose, where it is processed/stored, access controls, retention/deletion, third parties, and user controls. Prefer local processing and storage where feasible; collect the minimum needed. Require a clear, contextual user action before sending page content or selected text to an external provider. Disclose destination and retention behavior. Do not send page content or full URLs in diagnostic logs by default.

Private browsing must define and test behavior for cookies, HTTP cache, DOM storage, downloads, sessions, autofill, crash reports, and app process termination. A UI toggle alone is not sufficient. Provide user-accessible controls for clearing persistent site data before claiming deletion support.

This document is engineering guidance, not a privacy notice, legal advice, or a representation that regulatory compliance has been certified. A public privacy policy and jurisdiction-specific review are required before release.
