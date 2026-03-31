# OfflineXyz Android APK

This repository now contains a minimal Android app skeleton for building an APK.

## Build Debug APK

1. Install Android SDK (platform 34) and JDK 17.
2. Add `local.properties` with:
   ```
   sdk.dir=/path/to/Android/Sdk
   ```
3. Run:
   ```bash
   ./gradlew assembleDebug
   ```
4. APK output:
   `app/build/outputs/apk/debug/app-debug.apk`

## Current behavior
- Opens online URL when internet is available.
- Opens bundled offline page when internet is unavailable.
