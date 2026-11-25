# Android CI reference notes

The Android build uses Android API 35 and Build Tools 35.0.0. The CI workflow passes `packages: ""` to `android-actions/setup-android@v3` and installs those explicit SDK packages with `sdkmanager`.

The v3 action metadata declares `tools platform-tools` as the default `packages` input. Its README explains that an empty value skips installation of additional SDK packages. That behavior avoids passing the legacy `tools` package name to `sdkmanager`, which rejected it in an earlier CI run.

The Android `sdkmanager` documentation demonstrates quoted package identifiers such as `"platforms;android-36"` and `"build-tools;36.0.0"`. It also notes that `sdkmanager` is deprecated in favor of the Android CLI `android sdk`; the workflow currently uses the documented package-manager interface and should be revisited if the replacement becomes the supported CI path.

The GitHub-hosted Linux emulator has no hardware accelerator in this workflow. The initial emulator job reported that software CPU emulation required `ANDROID_I_WANT_MY_TCG=yes`. With that opt-in, API 35 booted in about 9.5 minutes in run [45676749623](https://github.com/stoiclas1-debug/Discover-AI-Browser-/actions/runs/45676749623). The instrumentation runner then crashed before completing tests. A direct launch check was attempted in run [45680894876](https://github.com/stoiclas1-debug/Discover-AI-Browser-/actions/runs/45680894876), but the action executed its multiline script input one line at a time using `sh`; it stopped before installing or launching the app. Run [45683069028](https://github.com/stoiclas1-debug/Discover-AI-Browser-/actions/runs/45683069028) installed the app and kept its process alive, but `am start -W` timed out before confirming foreground readiness. The strengthened smoke test passed in run [45685640830](https://github.com/stoiclas1-debug/Discover-AI-Browser-/actions/runs/45685640830): the API 35 emulator reported `topResumedActivity=... com.discoveraibrowser/.MainActivity ...`, and the app process remained alive as PID 2679. This verifies that the Android shell activity reached the resumed state in the emulator.

The Gradle wrapper-validation v4 action scans all `gradle-wrapper.jar` files recursively from the repository root. It does not accept a wrapper path input. The CI workflow therefore uses the action without a path override; this removes a warning for an unsupported input while still validating the Android wrapper.

## Official references

- [android-actions/setup-android v3 action metadata](https://raw.githubusercontent.com/android-actions/setup-android/v3/action.yml)
- [android-actions/setup-android v3 README](https://github.com/android-actions/setup-android/blob/v3/README.md)
- [Android SDK Command-Line Tools: sdkmanager](https://developer.android.com/tools/sdkmanager)
- [Gradle Wrapper Validation Action v4 documentation](https://github.com/gradle/actions/blob/v4/docs/wrapper-validation.md)
