package com.bekacodes.cinepulsetv.domain.ai

import com.bekacodes.cinepulsetv.ui.MovieUiModel


class VibeSearchEngine {

    /**
     * Parses natural-language Android TV voice/text queries such as:
     * - "Dark sci-fi thrillers under 2 hours with mind-bending endings"
     * - "High-stakes deep space survival under 110 minutes"
     * - "Cyberpunk AI mysteries with synthwave soundtracks"
     */
    fun rankCatalogForVibe(
        query: String,
        catalog: List<MovieUiModel>
    ): List<MovieUiModel> {
        val clean = query.trim().lowercase()
        if (clean.isEmpty() || clean == "all sci-fi originals") {
            return catalog.sortedByDescending { it.aiMatchPercent }
        }

        val maxMinutes = extractMaxRuntimeMinutes(clean)

        val keywordWeights = listOf(
            "dark" to 6,
            "mind-bending" to 8,
            "ending" to 5,
            "thriller" to 6,
            "space" to 6,
            "survival" to 7,
            "high-stakes" to 6,
            "cyberpunk" to 9,
            "synthwave" to 8,
            "mystery" to 6,
            "mysteries" to 6,
            "time" to 7
        )

        val activeQueryKeywords = keywordWeights.filter { (token, _) -> clean.contains(token) }

        val runtimeFiltered = catalog.filter { movie ->
            maxMinutes == null || movie.runtimeMinutes <= maxMinutes
        }

        val scored = runtimeFiltered.mapNotNull { movie ->
            val searchableBlob = (
                    movie.moodTags +
                            movie.genres.map { it.lowercase() } +
                            listOf(movie.title.lowercase(), movie.synopsis.lowercase())
                    ).joinToString(" ")

            if (activeQueryKeywords.isEmpty()) {
                // Direct title/genre substring match fallback
                val matchesText = searchableBlob.contains(clean)
                if (matchesText) movie else null
            } else {
                var matchedWeight = 0
                var totalPossibleWeight = 0
                activeQueryKeywords.forEach { (token, weight) ->
                    totalPossibleWeight += weight
                    if (searchableBlob.contains(token)) {
                        matchedWeight += weight
                    }
                }

                if (matchedWeight == 0) {
                    null
                } else {
                    val ratio = matchedWeight.toDouble() / totalPossibleWeight.coerceAtLeast(1)
                    val computedMatch = (84 + (ratio * 14).toInt()).coerceIn(85, 99)
                    movie.copy(aiMatchPercent = computedMatch)
                }
            }
        }.sortedByDescending { it.aiMatchPercent }

        return scored.ifEmpty { runtimeFiltered.sortedByDescending { it.aiMatchPercent } }
    }

    private fun extractMaxRuntimeMinutes(lowerQuery: String): Int? {
        // Match "under 2 hours" or "under 1 hour"
        Regex("""under\s+(\d+)\s*hour""").find(lowerQuery)?.let { match ->
            val hours = match.groupValues[1].toIntOrNull()
            if (hours != null) return hours * 60
        }
        // Match "under 110 minutes" or "under 100 mins"
        Regex("""under\s+(\d+)\s*min""").find(lowerQuery)?.let { match ->
            val mins = match.groupValues[1].toIntOrNull()
            if (mins != null) return mins
        }
        return null
    }
}