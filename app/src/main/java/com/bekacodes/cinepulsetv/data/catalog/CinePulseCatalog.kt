package com.bekacodes.cinepulsetv.data.catalog

import androidx.compose.ui.graphics.Color
import com.bekacodes.cinepulsetv.ui.MovieUiModel
import com.bekacodes.cinepulsetv.ui.XRaySceneCue

object CinePulseCatalog {

    val movies: List<MovieUiModel> = listOf(
        MovieUiModel(
            id = "solaris",
            title = "Solaris Protocol",
            tagLine = "CINEPULSE ORIGINAL",
            synopsis = "A signal from a vanished world. A memory that isn't hers. An astronaut discovers the universe is keeping a secret.",
            runtimeMinutes = 112,
            formattedRuntime = "1h 52m",
            releaseYear = 2026,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Thriller"),
            moodTags = listOf("dark", "sci-fi", "thriller", "mind-bending", "space", "ending"),
            aiMatchPercent = 98,
            primaryAccent = Color(0xFF6366F1),
            secondaryAccent = Color(0xFF38BDF8),
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            xRayCues = listOf(
                XRaySceneCue(
                    timestampSeconds = 0,
                    formattedTime = "00:00",
                    chapterTitle = "Ch. 1 — The Sub-Space Beacon",
                    plotInsight = "Telemetry aboard Orbital Station IX locks onto an encrypted carrier wave repeating every 42 seconds from an uncharted sector.",
                    castInScene = listOf("Dr. Elena Rostova (Commander)", "Marcus Vance (Flight Systems)"),
                    soundtrackTitle = "Sub-Zero Telemetry — Synth Pulse in D Minor"
                ),
                XRaySceneCue(
                    timestampSeconds = 15,
                    formattedTime = "00:15",
                    chapterTitle = "Ch. 2 — Memory Partition",
                    plotInsight = "Notice the reflection in the visor: the station's AI core displays logs from a mission Rostova has no memory of flying.",
                    castInScene = listOf("Dr. Elena Rostova", "ARIA-9 (Station AI Voice)"),
                    soundtrackTitle = "Echoes of Kepler — Modular Ambient Ensemble"
                ),
                XRaySceneCue(
                    timestampSeconds = 35,
                    formattedTime = "00:35",
                    chapterTitle = "Ch. 3 — The Docking Sequence",
                    plotInsight = "Vance overrides manual thrusters after realizing the incoming capsule's hull serial number matches their own ship.",
                    castInScene = listOf("Marcus Vance", "Dr. Elena Rostova", "Lt. Kaelen Sorel"),
                    soundtrackTitle = "Event Horizon Docking — Percussion & Low Brass"
                )
            )
        ),
        MovieUiModel(
            id = "chronos",
            title = "Chronos Echo",
            tagLine = "TIME LOOP THRILLER",
            synopsis = "A quantum cryptographer intercepts a distress call transmitted by her own future self 48 minutes before orbital collapse.",
            runtimeMinutes = 108,
            formattedRuntime = "1h 48m",
            releaseYear = 2026,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Mystery"),
            moodTags = listOf("dark", "sci-fi", "thriller", "mind-bending", "time", "ending"),
            aiMatchPercent = 97,
            primaryAccent = Color(0xFF8B5CF6),
            secondaryAccent = Color(0xFF10B981),
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            xRayCues = listOf(
                XRaySceneCue(
                    timestampSeconds = 0,
                    formattedTime = "00:00",
                    chapterTitle = "Ch. 1 — The 48-Minute Window",
                    plotInsight = "The chronometer on the lab wall subtly ticks backward for three frames when the tachyon array initializes.",
                    castInScene = listOf("Sera Lin (Cryptographer)", "Director Cole"),
                    soundtrackTitle = "Tachyon Drift — Analog Synthwave"
                ),
                XRaySceneCue(
                    timestampSeconds = 20,
                    formattedTime = "00:20",
                    chapterTitle = "Ch. 2 — Causality Breach",
                    plotInsight = "Lin decodes the audio spectrogram and discovers her own voice biometric embedded in the static.",
                    castInScene = listOf("Sera Lin"),
                    soundtrackTitle = "Recursive Paradox — Strings & Sub-Bass"
                )
            )
        ),
        MovieUiModel(
            id = "orbital",
            title = "Orbital Zero",
            tagLine = "DEEP SPACE SURVIVAL",
            synopsis = "Cut off from Earth telemetry, a three-person salvage crew boards a derelict fusion ark drifting past Titan.",
            runtimeMinutes = 99,
            formattedRuntime = "1h 39m",
            releaseYear = 2025,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Survival"),
            moodTags = listOf("dark", "sci-fi", "space", "survival", "high-stakes"),
            aiMatchPercent = 95,
            primaryAccent = Color(0xFF0EA5E9),
            secondaryAccent = Color(0xFF6366F1),
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            xRayCues = listOf(
                XRaySceneCue(
                    timestampSeconds = 0,
                    formattedTime = "00:00",
                    chapterTitle = "Ch. 1 — Titan Shadow Approach",
                    plotInsight = "Zero-gravity tether choreography was modeled using real ESA orbital salvage trajectories.",
                    castInScene = listOf("Capt. Jonas Mercer", "Hana Sato (Payload Specialist)"),
                    soundtrackTitle = "Saturnian Ring Ice — Dark Ambient"
                )
            )
        ),
        MovieUiModel(
            id = "neural",
            title = "Neural Drift",
            tagLine = "CYBERPUNK NOIR",
            synopsis = "In Neo-Veracruz, a synthetic memory architect uncovers a hidden partition inside a reigning senator's neural backup.",
            runtimeMinutes = 116,
            formattedRuntime = "1h 56m",
            releaseYear = 2026,
            qualityBadge = "Dolby Vision",
            genres = listOf("Sci-Fi", "Cyberpunk"),
            moodTags = listOf("cyberpunk", "sci-fi", "mind-bending", "synthwave", "mystery", "dark", "ending"),
            aiMatchPercent = 92,
            primaryAccent = Color(0xFFEC4899),
            secondaryAccent = Color(0xFF6366F1),
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            xRayCues = listOf(
                XRaySceneCue(
                    timestampSeconds = 0,
                    formattedTime = "00:00",
                    chapterTitle = "Ch. 1 — Rain Over Sector 7",
                    plotInsight = "The holographic billboards in the background foreshadow the corporate merger central to Act III.",
                    castInScene = listOf("Kael Vane (Memory Architect)", "Nyx (Netrunner)"),
                    soundtrackTitle = "Neon Cortex 2088 — Synthwave Arpeggio"
                )
            )
        ),
        MovieUiModel(
            id = "horizon",
            title = "Event Horizon X",
            tagLine = "SINGULARITY EXPEDITION",
            synopsis = "The first faster-than-light probe returns after seven years in deep sector space—bringing back an impossible passenger.",
            runtimeMinutes = 104,
            formattedRuntime = "1h 44m",
            releaseYear = 2025,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Thriller"),
            moodTags = listOf("dark", "sci-fi", "space", "thriller", "survival", "high-stakes"),
            aiMatchPercent = 89,
            primaryAccent = Color(0xFFF43F5E),
            secondaryAccent = Color(0xFF8B5CF6),
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            xRayCues = listOf(
                XRaySceneCue(
                    timestampSeconds = 0,
                    formattedTime = "00:00",
                    chapterTitle = "Ch. 1 — Quarantine Bay 4",
                    plotInsight = "The gravity drive core rings align only when biological neural activity is detected within 10 meters.",
                    castInScene = listOf("Commander Reyes", "Dr. Aris Thorne"),
                    soundtrackTitle = "Singularity Threshold — Sub-Bass Drone"
                )
            )
        ),
        MovieUiModel(
            id = "deep_sector",
            title = "Deep Sector",
            tagLine = "ABYSSAL COLONY",
            synopsis = "Three miles beneath Europa's ice crust, a geothermal drilling rig breaches a pressurized cavern older than Earth.",
            runtimeMinutes = 128,
            formattedRuntime = "2h 08m",
            releaseYear = 2025,
            qualityBadge = "4K HDR",
            genres = listOf("Sci-Fi", "Survival"),
            moodTags = listOf("dark", "sci-fi", "survival", "high-stakes"),
            aiMatchPercent = 87,
            primaryAccent = Color(0xFF14B8A6),
            secondaryAccent = Color(0xFF0EA5E9),
            streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            xRayCues = listOf(
                XRaySceneCue(
                    timestampSeconds = 0,
                    formattedTime = "00:00",
                    chapterTitle = "Ch. 1 — Beneath the Ice Sheet",
                    plotInsight = "At 128 minutes, this title tests the AI Vibe Engine's runtime filter—queries asking for 'under 2 hours' automatically filter it out.",
                    castInScene = listOf("Chief Engineer Davos", "Biologist Linnea"),
                    soundtrackTitle = "Thermal Vent — Hydrophone & Synth"
                )
            )
        )
    )
}