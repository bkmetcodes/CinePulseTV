# CinePulse TV ✦ — 10-Foot Android TV Streaming & AI Scene X-Ray

A widescreen **Android TV (10-foot UI)** streaming discovery and playback application built with **Kotlin**, **Jetpack Compose**, **Clean Architecture (MVI)**, **Media3 (`ExoPlayer`)**, **Room**, and an **AI Vibe & Runtime Search Engine**.

<p align="center">
  <!-- Paste your GitHub issue-uploaded GIF URL here -->
  <img width="829" height="463" alt="Image" src="https://github.com/user-attachments/assets/31d276d2-af85-4738-846c-e8e1f6bfee52" />
</p>

---

## ✨ Key Engineering Highlights

1. **10-Foot Hardware D-Pad Spatial Focus Architecture**
   - Built specifically for directional remote navigation (`←`, `→`, `↑`, `↓`, `Enter`, `Back`) across a `960x540dp` (1080p `xhdpi`) widescreen canvas.
   - Interactive cards and CTAs use `onFocusChanged`, animated scale transforms (`1.08x`), and glowing border rings (`#38BDF8` Sky / `#6366F1` Indigo) to ensure unmistakable focus visibility from 10 feet away.
   - Moving D-Pad focus across the poster carousel dynamically crossfades the full-bleed **Hero Spotlight** backdrop, synopsis, runtime, and AI match badge via `AnimatedContent`.

2. **Natural-Language AI "Vibe & Runtime" Search Engine (`VibeSearchEngine`)**
   - Parses conversational voice/text queries such as:
     - *"Dark sci-fi thrillers under 2 hours with mind-bending endings"*
     - *"High-stakes deep space survival under 110 minutes"*
     - *"Cyberpunk AI mysteries with synthwave soundtracks"*
   - Extracts maximum runtime constraints (`under X hours / minutes`) and scores catalog metadata to dynamically filter and rank titles by **AI Vibe Match %**.

3. **Media3 (`ExoPlayer`) Custom TV OSD & Live "AI Scene X-Ray" Drawer**
   - Streams HD video with deterministic `DisposableEffect` lifecycle management (`exoPlayer.release()`).
   - Replaces mobile touch controls with a 10-foot D-Pad On-Screen Display (`Play/Pause`, `⟲ 10s`, `15s ⟳`, custom `Canvas` seekbar with chapter cue markers).
   - Includes a real-time **AI Scene Intelligence (X-Ray)** side drawer that polls the playback clock and updates **Plot Context**, **Cast in Scene**, and **Soundtrack Cues** automatically, while allowing D-Pad chapter jumping.

4. **Reactive Room SQLite Watchlist (`CinePulseDatabase`)**
   - Persists watchlisted titles locally via `Flow.combine()` and syncs status badges across the Hero Spotlight, poster cards, and the dedicated **Watchlist (`★`)** Navigation Rail tab.

---

## 🏗️ Architecture Overview

```mermaid
flowchart LR
    Remote["Android TV D-Pad Remote\n(Focus & Click Intents)"] --> UI["CinePulseTvScreen &\nCinePulsePlayerScreen"]
    UI -->|CinePulseIntent| VM["CinePulseViewModel\n(MVI StateFlow)"]
    VM -->|Natural Language Query| Vibe["VibeSearchEngine\n(Mood + Runtime Scorer)"]
    VM -->|Toggle / Observe| Room["CinePulseDao\n(Room Watchlist SQLite)"]
    Room -->|Flow.combine()| VM
    VM -->|Immutable CinePulseUiState| UI
    UI -->|Stream URL + XRaySceneCues| Exo["Media3 ExoPlayer +\nLive AI X-Ray Drawer"]
```

---

## 🚀 Getting Started

1. **Run on an Android TV Emulator:**
   In Android Studio Device Manager, create an **Android TV (1080p)** AVD and press **Run**. (Also supports running on standard devices in landscape mode).
2. **Navigate with Keyboard / D-Pad:**
   - Use **Arrow Keys (`←`, `→`, `↑`, `↓`)** to move focus between the left Navigation Rail, the AI Vibe Search bar, the Hero Spotlight buttons, and the poster carousel.
   - Press **`Enter`** on **`▶ Play Movie`** or **`✦ AI Scene X-Ray`** to launch the Media3 player and test live scene intelligence.
3. **Run the Unit Test Suite:**
   ```bash
   ./gradlew testDebugUnitTest
   ```
