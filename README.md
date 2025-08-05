# Permission Summary 📱🔐

<p align="center">
  <img src="https://raw.githubusercontent.com/SimpolCo/Permissions-Summary/refs/heads/main/app/src/main/res/icon.png" width="250"/>
</p>

<p align="center">
  <a href="https://github.com/SimpolCo/Permissions-Summary/releases">
    <img src="https://img.shields.io/github/v/release/SimpolCo/Permissions-Summary?label=GitHub%20Release&logo=github" alt="GitHub Release">
  </a>
  <a href="https://apt.izzysoft.de/fdroid/index/apk/com.simpol.permissionssummary">
    <img src="https://img.shields.io/badge/IzzyOnDroid-Link-blue?logo=android" alt="IzzyOnDroid">
  </a>
  <a href="https://f-droid.org/packages/com.simpol.permissionssummary/">
    <img src="https://img.shields.io/f-droid/v/com.simpol.permissionssummary?label=F-Droid&logo=f-droid" alt="F-Droid">
  </a>
  <a href="https://shields.rbtlog.dev/com.simpol.permissionssummary">
    <img src="https://shields.rbtlog.dev/simple/com.simpol.permissionssummary" alt="RB shield">
  </a>

  <img src="https://img.shields.io/github/downloads/simpolco/permissions-summary/total" alt="Number of Downloads">
</p>

**Permission Summary** is a lightweight, open source Android app that helps you quickly check which apps have access to sensitive permissions on your device — in just a few seconds.

> ⚠️ The app only lists **user-installed apps** and only those with **dangerous permissions**. These are the permissions Android classifies as potentially privacy-invasive.

Built with Jetpack Compose, Material 3, and modern Android architecture.

---

## ✨ Features

- 🔐 **Grouped by Permission** — Instantly see which apps request access to camera, mic, contacts, etc.
- 🧼 **User Apps Only** — Filters out system apps and background clutter.
- 🚫 **Focus on Privacy** — Shows only dangerous permissions and skips non-sensitive ones like internet or notifications.
- 📱 **Modern UI** — Built entirely with Jetpack Compose and Material 3.
- ⚡ **Fast & Lightweight** — Optimized for quick, offline use. No trackers, no nonsense.

---

## 📸 Screenshots

<p float="left">
  <img src="https://raw.githubusercontent.com/SimpolCo/Permissions-Summary/refs/heads/main/assets/1.jpg" width="270" />
  <img src="https://raw.githubusercontent.com/SimpolCo/Permissions-Summary/refs/heads/main/assets/2.jpg" width="270" />
  <img src="https://raw.githubusercontent.com/SimpolCo/Permissions-Summary/refs/heads/main/assets/3.jpg" width="270" />
</p>

---

## 📦 Download

<p align="center">
  <a href="https://f-droid.org/packages/com.simpol.permissionssummary/">
    <img src="assets/fdroid.png" alt="Get it on F-Droid" height="80">
  </a>
  <a href="https://apt.izzysoft.de/fdroid/index/apk/com.simpol.permissionssummary">
    <img src="assets/izzyondroid.png" alt="Get it on IzzyOnDroid" height="80">
  </a>
  <a href="https://github.com/SimpolCo/Permissions-Summary/releases">
    <img src="assets/github.png" alt="Get it on GitHub" height="80">
  </a>
</p>

---

## 🔐 Permissions Checked

Permission Summary checks for the following **dangerous permissions**:

- **Camera**: Access to your device's camera  
- **Microphone**: Record audio  
- **Location**: Fine & coarse location (GPS, Wi-Fi, etc.)  
- **Storage**: Access to files, media, photos, or music  
- **Contacts**: Read, write, or access contact data and accounts  
- **Calendar**: Read or write calendar events  
- **Phone / Call Log**: Read phone state, call logs, answer calls, etc.  
- **SMS**: Send or receive SMS, MMS, and WAP push messages  
- **Body Sensors**: Access data from heart rate, step counter, etc.

> It intentionally **ignores non-dangerous permissions** like:
> - `POST_NOTIFICATIONS`
> - `INTERNET`
> - `BLUETOOTH`
> - `ACCESS_NETWORK_STATE`
> - `CAR_MODE`  
> These are not considered privacy-sensitive by Android and are excluded to reduce noise.

---

## 🔐 Permissions Used by This App

- `QUERY_ALL_PACKAGES` — Required to list installed apps and their permissions.

> This app **does not collect or share any personal data**. Everything runs **locally on-device** and offline.

---

## 📂 Tech Stack

- Jetpack Compose
- Kotlin
- ViewModel + StateFlow
- DataStore (for future settings)
- Android PackageManager APIs

---

## 📝 License

This project is licensed under the [GNU GPLv3](LICENSE).

---

## 🙋‍♀️ Contributions

Bug reports, screenshots, or suggestions are welcome.  
Please use GitHub Issues or Discussions to contribute.
