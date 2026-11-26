#!/usr/bin/env bash
set -Eeuo pipefail

DIAGNOSTICS_DIR="android/app/build"

capture_diagnostics() {
  mkdir -p "$DIAGNOSTICS_DIR"
  adb shell dumpsys activity activities > "$DIAGNOSTICS_DIR/emulator-smoke-activities.txt" 2>&1 || true
  adb logcat -d -t 3000 > "$DIAGNOSTICS_DIR/emulator-smoke-logcat.txt" 2>&1 || true
}

trap capture_diagnostics ERR

./android/gradlew -p android installDebug
adb shell am start -n com.discoveraibrowser/.MainActivity

resumed_activity=""
for attempt in {1..30}; do
  resumed_activity="$(adb shell dumpsys activity activities | grep -E 'mResumedActivity|topResumedActivity' | grep -F 'com.discoveraibrowser/.MainActivity' || true)"
  if [[ -n "$resumed_activity" ]]; then
    break
  fi
  sleep 2
done

if [[ -z "$resumed_activity" ]]; then
  capture_diagnostics
  echo "Discover AI Browser MainActivity did not become the resumed activity." >&2
  exit 1
fi

pid="$(adb shell pidof -s com.discoveraibrowser | tr -d '\r' || true)"
if [[ -z "$pid" ]]; then
  capture_diagnostics
  echo "Discover AI Browser process exited after launch." >&2
  exit 1
fi

echo "$resumed_activity"
echo "Discover AI Browser MainActivity is resumed and the app process is alive with PID $pid."
