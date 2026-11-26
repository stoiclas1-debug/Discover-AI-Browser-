# Threat Model

## Scope and assumptions

This first-pass model covers the Android WebView client, a future TypeScript API, PostgreSQL as a server-side dependency, mock provider integration, and the public GitHub repository. It is a starting point, not a security certification. Revisit it as architecture and data flows become concrete.

## Assets

The protected assets are user browsing sessions and page context, credentials and tokens, backend configuration, database contents, application signing material, source/dependency integrity, and user trust. The app should not possess provider secrets or production database credentials.

## Threats and controls

| Threat                                                   | Potential impact                                                                                     | Foundation controls / required follow-up                                                                                                                                            |
| -------------------------------------------------------- | ---------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Malicious webpage or URL scheme abuses WebView           | Native actions, local data access, phishing, or unintended external-app launch                       | Restrict navigation to HTTP(S); keep file/content access disabled, mixed content blocked, and JavaScript off by default; test origin/scheme handling before relaxing controls       |
| Cleartext HTTP navigation                                | Network observers can read or alter page traffic; credentials entered on an HTTP site can be exposed | HTTP is permitted to preserve browser behavior and is visibly labelled as unencrypted; prefer HTTPS and never enter sensitive credentials on HTTP pages; keep mixed content blocked |
| Web content reaches privileged native bridge             | Device compromise or data disclosure                                                                 | Do not expose a JavaScript bridge to arbitrary sites; require a narrow origin-checked design and security review if one is later needed                                             |
| Provider/API credential shipped in app                   | Credential theft, quota abuse, unauthorized access                                                   | Keep provider keys server-side; CI secret scans and review; rotate exposed credentials                                                                                              |
| Malicious or compromised backend request                 | Unauthorized browsing-data exposure                                                                  | Minimize data collection; validate input; authentication/authorization and rate limits are prerequisites before user data endpoints                                                 |
| AI prompt injection or sensitive page content disclosure | Harmful output or third-party data leakage                                                           | Explicit user action and clear data-flow disclosure; treat page text as untrusted; mock adapter first; provider policy and safety controls required                                 |
| Database exposure or excessive retention                 | Persistent personal browsing data leak                                                               | No browsing-data schema yet; use least privilege, managed secret storage, TLS, access logging that avoids content, retention/deletion requirements, backups and migration review    |
| Dependency or build pipeline compromise                  | Malicious code in app/backend releases                                                               | Lockfiles, Dependabot, CodeQL, dependency review, restricted workflow permissions, pinned third-party actions where practical, PR review                                            |
| Secret accidentally committed                            | Account/service takeover                                                                             | `.gitignore`, GitHub secret scanning and push protection, Gitleaks CI, key rotation/revocation process                                                                              |
| Unsafe private-browsing claim                            | User expects non-persistence while platform data remains                                             | Do not claim private mode exists; define/test cookies, cache, DOM storage, downloads, and shutdown semantics before implementation                                                  |
| Unreviewed main-branch change                            | Broken or insecure release                                                                           | PR-based changes, required CI, review and branch ruleset/branch protection where supported                                                                                          |

## Trust boundaries

Web pages and URLs are untrusted input. Android UI is not trusted to hold backend secrets. Public API requests are untrusted until validated and authorized. The provider boundary is external and should receive only data the user knowingly submits. CI workflows process untrusted pull-request code and must use minimum permissions, avoid exposing secrets to forked PRs, and pin or review actions.

## Open questions

Before production, decide on user identity/session design, API authentication, browsing data retention, AI provider and consent, database migration ownership, crash/diagnostic collection, download restrictions, content blocking, abuse controls, app signing and update distribution, and incident response contacts.
