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

**Permission Summary** is an Android app that gives you a clear, organized overview of which apps
have access to sensitive permissions on your device.

> ⚠️ This app only lists **dangerous permissions** and only for **non-system (user-installed) apps**.

Built with Jetpack Compose, Material 3, and modern Android architecture.

---

## ✨ Features

- 🔐 **Grouped by Permission** — Quickly see which apps request sensitive access like camera,
  location, or storage.
- 🧼 **User Apps Only** — No system bloat or irrelevant services cluttering the list.
- 🚫 **Only Dangerous Permissions** — Skips normal permissions and focuses on what matters most.
- 📱 **Modern UI** — Built entirely with Jetpack Compose and Material 3.
- ⚡ **Fast & Lightweight** — Everything runs fully on-device.

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

## 🔐 Permissions Used by This App

- `QUERY_ALL_PACKAGES` — Required to list installed apps and their permissions.

> This app **does not collect or share any personal data**. All analysis happens **on-device** and
> offline.

---

## 📋 Backlog / Planned Features

Here are some ideas planned for future releases:

- ✅ **Filter toggle for system apps**  
- ✅ **Advanced mode to show all (normal + dangerous) permissions**  
- 🔔 **Permission change notifications** — get alerted when an app gains a new permission  
- 🔍 **Filter apps by permission name or type**  

---

## 📂 Tech Stack

- Jetpack Compose
- Kotlin
- ViewModel + StateFlow
- DataStore (for potential future settings)
- PackageManager APIs

---

## 📝 License

This project is licensed under the [GNU GPLv3](LICENSE).

---

## 🙋‍♀️ Contributions

Bug reports, screenshots, or suggestions are welcome.  
Please use GitHub Issues or Discussions to contribute.
