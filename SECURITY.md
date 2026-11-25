# Security Policy

## Reporting a vulnerability

Please do not report exploitable vulnerabilities in public issues. Use GitHub's **Report a vulnerability** flow under the repository's Security tab to create a private advisory. If that feature is unavailable, contact the repository maintainers privately through the contact method listed on the repository owner profile. Include affected versions/commit, impact, reproduction steps, and any mitigation. Avoid including real user data or live secrets in a report.

The project is at an early foundation stage; there are no published application releases or guaranteed response-time SLA yet. Maintainers should acknowledge reports promptly, coordinate a fix and disclosure timeline with the reporter, and avoid publishing exploit details before a fix is available.

## Scope and handling

- Keep credentials in deployment secret stores or untracked local `.env` files. Never commit API keys, database passwords, signing credentials, production data, or private certificates.
- Provider credentials must remain server-side; Android clients must not contain provider keys.
- Treat WebView navigation, JavaScript, local-file access, downloads, permission prompts, and untrusted AI/browser content as attack surfaces.
- Use supported dependency versions, review automated dependency alerts, and validate Android and backend changes in CI.
- Revoke and rotate any credential that may have been exposed, even if the commit is removed later.

## Maintainer checklist

Keep GitHub secret scanning and push protection enabled, enable Dependabot alerts and security updates where available, review CodeQL/dependency scan findings, and maintain branch protections requiring pull requests and passing checks. These repository settings are separate from files committed to this repository.
