# Permission Summary 📱🔐

<p align="center">
  <img src="https://raw.githubusercontent.com/SimpolCo/Permissions-Summary/refs/heads/main/app/src/main/res/icon.png" width="250"/>
</p>


An Android app that summarizes which installed apps use which permissions — giving you a clear view into what apps can access on your device.

Built with Jetpack Compose, Material 3, and modern Android architecture.

---

## ✨ Features

- 🔐 **Grouped by Permission** — Quickly see which apps request sensitive access.
- ⚙️ **Filters** — Hide apps or permissions you don't care about.
- 💾 **Persistent Settings** — Filters are saved even after you close the app.
- 🧼 **Clean Output** — System apps and low-impact permissions excluded by default.
- 📱 **Modern UI** — Built with Jetpack Compose and Material 3.

---

## 📦 Download

You can download the latest version:

- 👉 [GitHub Releases](https://github.com/simpol_co/permissions-summary/releases)
- 📦 [F-Droid (Coming soon)](https://f-droid.org/)

No need to build from source — just grab the APK and install.

---

## 🔐 Permissions Used

- `QUERY_ALL_PACKAGES` — Required to check what apps are installed and which permissions they use.

> The app **does not** collect or share any personal data. All analysis happens on-device.

---

## 📂 Tech Stack

- Jetpack Compose
- Kotlin
- Android ViewModel + StateFlow
- DataStore (for saving filters)
- PackageManager APIs

---

## 📝 License

This project is licensed under the [MIT License](LICENSE).

---

## 🙋‍♀️ Contributions

Bug reports, translations, or suggestions are welcome.  
Please use GitHub Issues or Discussions to contribute.

