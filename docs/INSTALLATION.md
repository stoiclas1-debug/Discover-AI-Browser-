# Installation and Development

## Prerequisites

For Android, install Android Studio with Android SDK Platform 35 and a compatible emulator. The project targets the Android Gradle Plugin/Gradle versions pinned by the wrapper and uses JDK 17 for the Android build. Gradle selects a compatible Android Build Tools version for the configured plugin. Accept the Android SDK licenses before building. An Android emulator or USB-debuggable device is needed to launch the app; building an APK alone does not demonstrate emulator launch.

For backend development, install Node.js 22.13.x or newer in the 22.x line and npm 10 or newer. PostgreSQL is optional for the health endpoint and mock-provider development; it becomes necessary only for features that use the database. Docker is not required by the repository at this stage.

## Backend

From the repository root:

```sh
npm ci
cp .env.example backend/.env
npm run dev
```

The development server listens on port 3000 by default. `backend/.env` is local-only and ignored by Git. The current process-health API does not use PostgreSQL, so `DATABASE_URL` is optional in development and production; configure it through a deployment secret manager only when a database-backed feature is introduced. Use only approved server-side AI provider configuration. Do not put credentials in a client app or commit them.

Run all backend checks from the repository root:

```sh
npm run format:check
npm run lint
npm run typecheck
npm test
npm run build
```

## Android

Open `android/` in Android Studio and allow Gradle sync to download pinned plugin/dependency versions. Or from the repository root:

```sh
./android/gradlew -p android testDebugUnitTest assembleDebug
```

Install and launch on a connected emulator/device using Android Studio's Run action. A successful assemble task proves compilation, not runtime correctness or emulator launch. Do not commit `local.properties`, signing keys, or release credentials.

## Environment and troubleshooting

`.env.example` lists variable names and non-secret local defaults only. The backend validates environment configuration during startup and exits with a clear error for invalid settings. Use the exact Node/JDK/SDK versions recorded in project files and CI. If a build cannot run because the local environment lacks an SDK or emulator, report that blocker rather than treating compilation or launch as successful.
