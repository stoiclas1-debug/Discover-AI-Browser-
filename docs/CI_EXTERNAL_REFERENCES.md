# Android CI reference notes

The Android build uses Android API 35 and Build Tools 35.0.0. The CI workflow passes `packages: ""` to `android-actions/setup-android@v3` and installs those explicit SDK packages with `sdkmanager`.

The v3 action metadata declares `tools platform-tools` as the default `packages` input. Its README explains that an empty value skips installation of additional SDK packages. That behavior avoids passing the legacy `tools` package name to `sdkmanager`, which rejected it in an earlier CI run.

The Android `sdkmanager` documentation demonstrates quoted package identifiers such as `"platforms;android-36"` and `"build-tools;36.0.0"`. It also notes that `sdkmanager` is deprecated in favor of the Android CLI `android sdk`; the workflow currently uses the documented package-manager interface and should be revisited if the replacement becomes the supported CI path.

The GitHub-hosted Linux emulator has no hardware accelerator in this workflow. The initial emulator job reported that software CPU emulation required `ANDROID_I_WANT_MY_TCG=yes`. With that opt-in, API 35 booted in about 9.5 minutes in run [45676749623](https://github.com/stoiclas1-debug/Discover-AI-Browser-/actions/runs/45676749623). The instrumentation runner then crashed before completing tests. CI has been changed to validate the app directly by installing it, starting `MainActivity`, confirming the process remains alive, and capturing logcat on failure. The replacement check still needs a successful run before it can be reported as passing.

## Official references

- [android-actions/setup-android v3 action metadata](https://raw.githubusercontent.com/android-actions/setup-android/v3/action.yml)
- [android-actions/setup-android v3 README](https://github.com/android-actions/setup-android/blob/v3/README.md)
- [Android SDK Command-Line Tools: sdkmanager](https://developer.android.com/tools/sdkmanager)
