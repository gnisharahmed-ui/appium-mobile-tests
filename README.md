# 📱 Appium Mobile Test Framework

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Appium](https://img.shields.io/badge/Appium-8.6-purple?logo=appium)
![TestNG](https://img.shields.io/badge/TestNG-7.9-red)
![Android](https://img.shields.io/badge/Android-API_33-green?logo=android)
![CI](https://github.com/gnisharahmed-ui/appium-mobile-tests/actions/workflows/mobile-tests.yml/badge.svg)

A senior-level Appium mobile automation framework using the **Screen Factory** pattern — featuring gesture utilities, config-driven device selection, thread-safe driver management, and CI integration with Android emulator.

---

## 📐 Architecture

```
appium-mobile-tests/
├── src/
│   ├── main/java/com/sdet/
│   │   ├── config/
│   │   │   └── CapabilitiesConfig.java   # Loads device profiles from JSON
│   │   ├── screens/                      # Screen Factory pattern
│   │   │   ├── BaseScreen.java           # Appium waits, tap, enterText, gestures
│   │   │   ├── LoginScreen.java
│   │   │   ├── ProductsScreen.java
│   │   │   ├── CartScreen.java
│   │   │   └── CheckoutScreen.java
│   │   └── utils/
│   │       ├── DriverManager.java        # ThreadLocal AndroidDriver
│   │       └── GestureUtils.java         # Swipe, scroll, long press
│   └── test/java/com/sdet/tests/
│       ├── BaseTest.java                 # Driver lifecycle + screenshot on failure
│       ├── LoginTest.java
│       └── ProductsTest.java
├── src/test/resources/
│   ├── capabilities.json                 # Device profiles
│   └── testng-mobile.xml
└── .github/workflows/mobile-tests.yml
```

## 🔑 Key Design Decisions

| Concern | Solution |
|---|---|
| Screen Objects | Screen Factory with `@AndroidFindBy` + `AppiumFieldDecorator` |
| Driver Safety | `ThreadLocal<AndroidDriver>` in `DriverManager` |
| Gestures | `GestureUtils` using W3C `PointerInput` API (Appium 2.x) |
| Device Config | `capabilities.json` with named profiles — switch with `-Ddevice.profile=` |
| BrowserStack | `browserstack` profile in `capabilities.json` — enable with `-Dbrowserstack=true` |
| Reporting | Allure with `@Step` annotations + screenshot on failure |

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- [Appium 2.x](https://appium.io) installed globally: `npm install -g appium@2`
- Appium UiAutomator2 driver: `appium driver install uiautomator2`
- Android emulator or real device running

### Run Tests

```bash
# Start Appium server (separate terminal)
appium

# Download the sample APK
mkdir apps && curl -L "https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk" -o apps/saucelabs-sample.apk

# Run on emulator (default)
mvn clean test

# Run on real device profile
mvn clean test -Ddevice.profile=android-real-device

# Run on BrowserStack
mvn clean test -Ddevice.profile=browserstack-android -Dbrowserstack=true \
  -DBROWSERSTACK_USERNAME=your_user -DBROWSERSTACK_ACCESS_KEY=your_key
```

---

## ✅ Test Coverage

| Test Class | Scenarios |
|---|---|
| `LoginTest` | Valid login, invalid creds, locked-out user, data-driven multi-user |
| `ProductsTest` | Products load, add to cart, scroll gesture, E2E checkout, checkout validation |

**Test app:** [Sauce Labs Sample Mobile App](https://github.com/saucelabs/sample-app-mobile)

---

## 📱 Device Profiles

Edit `src/test/resources/capabilities.json` to configure devices:

```json
{
  "android-emulator": { ... },
  "android-real-device": { ... },
  "browserstack-android": { ... }
}
```

Switch profiles with: `mvn test -Ddevice.profile=android-emulator`

---

## 🏗️ CI/CD

GitHub Actions spins up an Android emulator (API 33), starts Appium, and runs the full suite on every push and PR.

See [`.github/workflows/mobile-tests.yml`](.github/workflows/mobile-tests.yml)
