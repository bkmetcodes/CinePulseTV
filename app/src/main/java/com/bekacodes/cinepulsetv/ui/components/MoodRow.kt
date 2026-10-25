package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.bekacodes.cinepulsetv.ui.MovieUiModel
import com.bekacodes.cinepulsetv.ui.theme.TvIndigo
import com.bekacodes.cinepulsetv.ui.theme.TvInk
import com.bekacodes.cinepulsetv.ui.theme.TvMuted

@Composable
fun MoodRow(
    sectionTitle: String,
    movies: List<MovieUiModel>,
    spotlightMovieId: String?,
    watchlistIds: Set<String>,
    onMovieFocused: (MovieUiModel) -> Unit,
    onMovieClicked: (MovieUiModel) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sectionTitle,
                color = TvInk,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("✦", color = TvIndigo, fontSize = 13.sp)
                Text(
                    text = "DARK WORLDS. DEEPER TWISTS.",
                    color = TvMuted,
                    fontSize = 11.sp,
                    letterSpacing = 0.8.sp
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 6.dp, horizontal = 4.dp)
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieCard(
                    movie = movie,
                    isTopMatch = movie.id == movies.firstOrNull()?.id,
                    isSelectedInSpotlight = movie.id == spotlightMovieId,
                    isInWatchlist = movie.id in watchlistIds,
                    onFocused = { onMovieFocused(movie) },
                    onClicked = { onMovieClicked(movie) }
                )
            }
        }
    }
}