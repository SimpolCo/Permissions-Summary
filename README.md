# Permission Summary 📱🔐

<p align="center">
  <img src="https://raw.githubusercontent.com/SimpolCo/Permissions-Summary/refs/heads/main/app/src/main/res/icon.png" width="250"/>
</p>

An Android app that summarizes which installed apps use which permissions — giving you a clear view
into what apps can access on your device.

Built with Jetpack Compose, Material 3, and modern Android architecture.

---

## ✨ Features

- 🔐 **Grouped by Permission** — Quickly see which apps request sensitive access, organized into
  categories like Camera, Location, Storage, and more.
- 📋 **Expandable Cards** — Tap any permission group to view the apps using it.
- ⚙️ **App Settings Shortcut** — Tap an app card to open its system settings and manage permissions
  directly.
- 🧼 **Clean Output** — System apps and low-impact permissions are excluded by default.
- 📱 **Modern UI** — Built entirely with Jetpack Compose and Material 3 for a smooth and responsive
  experience.

---

## 📦 Download

You can download the latest version:

- 👉 [GitHub Releases](https://github.com/simpol_co/permissions-summary/releases)
- 📦 [F-Droid (Coming soon)](https://f-droid.org/)

No need to build from source — just grab the APK and install.

---

## 🔐 Permissions Used

- `QUERY_ALL_PACKAGES` — Required to list installed apps and their granted permissions.
- **No Internet Access** — All analysis is done locally.
- **No data collection.** Everything stays on your device.

---

## 🧰 Tech Stack

- Jetpack Compose
- Kotlin
- Material 3
- Android ViewModel + StateFlow
- PackageManager APIs

---

## 📝 License

This project is licensed under the [MIT License](LICENSE).

---

## 🙋‍♀️ Contributions

Bug reports, translations, or suggestions are welcome.  
Please use [GitHub Issues](https://github.com/simpol_co/permissions-summary/issues) or Discussions
to contribute.
