# TRADING STRATEGY LAB — Android source v0.2

Kotlin + XML Android Studio project. This is an expanded starter implementation, **not yet the full 17-screen specification** and not production-ready.

## Included in this version
- Android Studio project structure and GitHub Actions workflow
- CSV import/export using Android document picker
- OHLCV validation, timestamp seconds-to-milliseconds conversion, duplicate timestamp removal
- SMA, EMA, RSI and ATR calculations
- EMA/RSI strategy hypothesis display
- Simplified EMA 12/26 crossover historical backtest
- Daily WorkManager readiness placeholder
- Basic JVM tests

## Not yet implemented
- Full 17-screen navigation and complete dashboard
- Room persistence, migrations, database backup/restore
- Gemini + Groq production adapters, connection tests, provider failover, encrypted key-management UI
- Live BTCUSDT / XAUUSD feeds
- Complete walk-forward and out-of-sample evaluation, leakage/overfit gates, robust execution simulation
- Portfolio-grade paper trading, partial fills/exits, signal scanner, notification history
- Daily autonomous strategy research and AI candidate validation
- Full automated test coverage and verified release APK

## Build
Requirements: Android Studio with JDK 17 and Android SDK 35. Open this folder in Android Studio and sync Gradle, or run:
`gradle testDebugUnitTest assembleDebug`

The included GitHub Actions workflow builds with Gradle 8.9. No Gradle wrapper JAR is included. Build status is not verified in this environment.

## CSV
Header: `timestamp,open,high,low,close,volume`
Timestamps: Unix seconds or milliseconds. CSV currently loads into memory and does not persist after app restart.

## Safety
Research/paper prototype only. No real-money broker execution. Backtest assumptions are simplified and results are not forecasts or investment advice.
