# OfflineXyz Android APK

This repository contains a minimal Android app skeleton for building an APK.

## Kya external software chahiye?

Haan, **minimum** ye chahiye:
1. **JDK 17**
2. **Android SDK (API 34 + Build-Tools)**

Optional (easy setup ke liye):
- **Android Studio** (SDK manage karne ke liye convenient)

Not required:
- Alag se Kotlin install nahi chahiye.
- Alag se Android NDK nahi chahiye.

## Build Debug APK (Local Machine)

1. Android SDK install karo (platform 34) aur JDK 17 set karo.
2. Project root me `local.properties` banao:
   ```
   sdk.dir=/path/to/Android/Sdk
   ```
3. Build command run karo:
   ```bash
   gradle assembleDebug
   ```
4. APK output path:
   `app/build/outputs/apk/debug/app-debug.apk`

## Current behavior
- Internet available: app online URL open karti hai.
- Internet unavailable: app bundled offline page open karti hai.
