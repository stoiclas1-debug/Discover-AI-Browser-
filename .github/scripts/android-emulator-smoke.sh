#!/usr/bin/env bash
set -Eeuo pipefail

LOGCAT_PATH="android/app/build/emulator-smoke-logcat.txt"

capture_logcat() {
  mkdir -p "$(dirname "$LOGCAT_PATH")"
  adb logcat -d -t 3000 > "$LOGCAT_PATH" 2>&1 || true
}

trap capture_logcat ERR

./android/gradlew -p android installDebug
adb shell am start -W -n com.discoveraibrowser/.MainActivity
sleep 5

pid="$(adb shell pidof -s com.discoveraibrowser | tr -d '\r' || true)"
if [[ -z "$pid" ]]; then
  capture_logcat
  echo "Discover AI Browser process exited after launch." >&2
  exit 1
fi

echo "Discover AI Browser launched and remained alive with PID $pid."
