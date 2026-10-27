package com.bekacodes.cinepulsetv.ui

import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.bekacodes.cinepulsetv.ui.theme.TvBg
import com.bekacodes.cinepulsetv.ui.theme.TvBody
import com.bekacodes.cinepulsetv.ui.theme.TvCard
import com.bekacodes.cinepulsetv.ui.theme.TvEmerald
import com.bekacodes.cinepulsetv.ui.theme.TvIndigo
import com.bekacodes.cinepulsetv.ui.theme.TvInk
import com.bekacodes.cinepulsetv.ui.theme.TvMuted
import com.bekacodes.cinepulsetv.ui.theme.TvSky
import com.bekacodes.cinepulsetv.ui.theme.TvSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(UnstableApi::class)
@Composable
fun CinePulsePlayerScreen(
    movie: MovieUiModel,
    isXRayOpen: Boolean,
    onToggleXRay: (Boolean) -> Unit,
    onClosePlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isPlaying by remember { mutableStateOf(true) }
    var currentPositionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(120_000L) }

    val playPauseFocusRequester = remember { FocusRequester() }

    val exoPlayer = remember(movie.streamUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(movie.streamUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY && exoPlayer.duration > 0) {
                    durationMs = exoPlayer.duration
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    // Poll player clock every 250ms to sync the seekbar and active AI X-Ray scene card
    LaunchedEffect(exoPlayer) {
        while (isActive) {
            currentPositionMs = exoPlayer.currentPosition.coerceAtLeast(0L)
            if (exoPlayer.duration > 0) {
                durationMs = exoPlayer.duration
            }
            delay(250L)
        }
    }

    LaunchedEffect(Unit) {
        runCatching { playPauseFocusRequester.requestFocus() }
    }

    // Back button closes X-Ray drawer first if open, otherwise exits playback
    BackHandler {
        if (isXRayOpen) {
            onToggleXRay(false)
        } else {
            onClosePlayer()
        }
    }

    val currentSeconds = (currentPositionMs / 1000L).toInt()
    val activeXRayCue = remember(currentSeconds, movie.xRayCues) {
        movie.xRayCues.lastOrNull { it.timestampSeconds <= currentSeconds }
            ?: movie.xRayCues.firstOrNull()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TvBg)
    ) {
        // 1. Hardware-Accelerated Media3 Video Surface
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { view ->
                if (view.player != exoPlayer) {
                    view.player = exoPlayer
                }
            }
        )

        // 2. Top & Bottom Cinematic Scrim Gradients for 10-Foot Readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to TvBg.copy(alpha = 0.80f),
                        0.22f to Color.Transparent,
                        0.68f to Color.Transparent,
                        1f to TvBg.copy(alpha = 0.94f)
                    )
                )
        )

        // 3. Main Player Stage (Left viewport + Bottom OSD Controls)
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 36.dp, vertical = 26.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Bar: Back Button + Movie Title & Active Chapter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        TvOsdButton(
                            label = "← Exit",
                            onClick = onClosePlayer
                        )
                        Column {
                            Text(
                                text = movie.title,
                                color = TvInk,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = activeXRayCue?.chapterTitle ?: movie.tagLine,
                                color = TvSky,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Quality & Audio Badges
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OsdBadge(movie.qualityBadge)
                        OsdBadge("Dolby Atmos")
                    }
                }

                // Bottom OSD: Progress Scrubber with Chapter Markers + D-Pad Transport Controls
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Custom Seekbar with glowing X-Ray Cue Markers
                    PlayerSeekBarWithCues(
                        currentPositionMs = currentPositionMs,
                        durationMs = durationMs,
                        cues = movie.xRayCues
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left Transport Buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TvOsdButton(
                                label = if (isPlaying) "⏸ Pause" else "▶ Play",
                                isPrimary = true,
                                focusRequester = playPauseFocusRequester,
                                onClick = {
                                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                                }
                            )
                            TvOsdButton(
                                label = "⟲ 10s",
                                onClick = {
                                    exoPlayer.seekTo(
                                        (exoPlayer.currentPosition - 10_000L).coerceAtLeast(
                                            0L
                                        )
                                    )
                                }
                            )
                            TvOsdButton(
                                label = "15s ⟳",
                                onClick = {
                                    exoPlayer.seekTo(
                                        (exoPlayer.currentPosition + 15_000L).coerceAtMost(
                                            durationMs
                                        )
                                    )
                                }
                            )
                        }

                        // Center Timestamp Readout
                        Text(
                            text = "${formatMillis(currentPositionMs)} / ${formatMillis(durationMs)}",
                            color = TvBody,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Right: Toggle AI Scene X-Ray Drawer Button
                        TvOsdButton(
                            label = if (isXRayOpen) "✦ Hide AI X-Ray" else "✦ AI Scene X-Ray",
                            isHighlighted = isXRayOpen,
                            onClick = { onToggleXRay(!isXRayOpen) }
                        )
                    }
                }
            }

            // 4. Right-Side "AI Scene X-Ray" Glass Drawer (Matches Figma Screen 2)
            AnimatedVisibility(
                visible = isXRayOpen,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it })
            ) {
                AiXRaySideDrawer(
                    movie = movie,
                    activeCue = activeXRayCue,
                    currentSeconds = currentSeconds,
                    onSeekToCue = { cueSeconds ->
                        exoPlayer.seekTo(cueSeconds * 1000L)
                    }
                )
            }
        }
    }
}

@Composable
private fun AiXRaySideDrawer(
    movie: MovieUiModel,
    activeCue: XRaySceneCue?,
    currentSeconds: Int,
    onSeekToCue: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(310.dp)
            .fillMaxHeight(),
        color = Color(0xEB0B101B),
        border = BorderStroke(1.dp, TvIndigo.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Drawer Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("✦", color = TvSky, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "AI Scene Intelligence",
                        color = TvInk,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = formatMillis(currentSeconds * 1000L),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(TvIndigo.copy(alpha = 0.25f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    color = TvSky,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (activeCue != null) {
                // Card 1: Plot Context / Why This Scene Matters
                XRayInfoCard(
                    badge = "PLOT CONTEXT · ${activeCue.formattedTime}",
                    title = activeCue.chapterTitle,
                    body = activeCue.plotInsight,
                    accentColor = TvIndigo
                )

                // Card 2: Cast in Scene
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TvCard,
                    border = BorderStroke(1.dp, Color(0x22FFFFFF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "CAST IN SCENE",
                            color = TvEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        activeCue.castInScene.forEach { actor ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(TvEmerald)
                                )
                                Text(
                                    text = actor,
                                    color = TvInk,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Card 3: Active Soundtrack Identification
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TvCard,
                    border = BorderStroke(1.dp, Color(0x22FFFFFF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "♪ SOUNDTRACK CUE",
                            color = TvSky,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = activeCue.soundtrackTitle,
                            color = TvInk,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Interactive D-Pad Scene Jump List
            if (movie.xRayCues.size > 1) {
                Text(
                    text = "JUMP TO SCENE MOMENT",
                    color = TvMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(movie.xRayCues, key = { it.timestampSeconds }) { cue ->
                        var cueFocused by remember { mutableStateOf(false) }
                        val isCurrent = cue.timestampSeconds == activeCue?.timestampSeconds

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        cueFocused -> TvIndigo.copy(alpha = 0.35f)
                                        isCurrent -> TvIndigo.copy(alpha = 0.16f)
                                        else -> TvCard
                                    }
                                )
                                .border(
                                    width = if (cueFocused) 1.5.dp else 1.dp,
                                    color = when {
                                        cueFocused -> TvSky
                                        isCurrent -> TvIndigo
                                        else -> Color(0x18FFFFFF)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .onFocusChanged { cueFocused = it.isFocused }
                                .focusable()
                                .clickable { onSeekToCue(cue.timestampSeconds) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = cue.chapterTitle,
                                color = if (cueFocused || isCurrent) TvInk else TvBody,
                                fontSize = 11.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = cue.formattedTime,
                                color = TvSky,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun XRayInfoCard(
    badge: String,
    title: String,
    body: String,
    accentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = TvCard,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = badge,
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Text(
                text = title,
                color = TvInk,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = body,
                color = TvBody,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun PlayerSeekBarWithCues(
    currentPositionMs: Long,
    durationMs: Long,
    cues: List<XRaySceneCue>
) {
    val safeDuration = durationMs.coerceAtLeast(1L)
    val progressFraction = (currentPositionMs.toFloat() / safeDuration.toFloat()).coerceIn(0f, 1f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
    ) {
        val barY = size.height / 2f
        // Background track
        drawLine(
            color = Color(0x44FFFFFF),
            start = Offset(0f, barY),
            end = Offset(size.width, barY),
            strokeWidth = 4.dp.toPx()
        )
        // Active progress fill
        val activeX = size.width * progressFraction
        drawLine(
            brush = Brush.horizontalGradient(listOf(TvIndigo, TvSky)),
            start = Offset(0f, barY),
            end = Offset(activeX, barY),
            strokeWidth = 4.dp.toPx()
        )
        // Glowing chapter cue markers
        cues.forEach { cue ->
            val cueFraction =
                ((cue.timestampSeconds * 1000f) / safeDuration.toFloat()).coerceIn(0f, 1f)
            val cueX = size.width * cueFraction
            drawCircle(
                color = TvEmerald,
                radius = 4.dp.toPx(),
                center = Offset(cueX, barY)
            )
        }
        // Playhead thumb
        drawCircle(
            color = TvSky,
            radius = 7.dp.toPx(),
            center = Offset(activeX, barY)
        )
        drawCircle(
            color = TvInk,
            radius = 3.dp.toPx(),
            center = Offset(activeX, barY)
        )
    }
}

@Composable
private fun TvOsdButton(
    label: String,
    isPrimary: Boolean = false,
    isHighlighted: Boolean = false,
    focusRequester: FocusRequester? = null,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .height(40.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
            .clip(CircleShape)
            .background(
                when {
                    isFocused && isPrimary -> TvInk
                    isPrimary -> TvInk.copy(alpha = 0.9f)
                    isFocused -> TvIndigo.copy(alpha = 0.45f)
                    isHighlighted -> TvIndigo.copy(alpha = 0.28f)
                    else -> TvSurface
                }
            )
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = when {
                    isFocused -> TvSky
                    isHighlighted -> TvIndigo
                    else -> Color(0x33FFFFFF)
                },
                shape = CircleShape
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isPrimary) TvBg else TvInk,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun OsdBadge(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(TvSurface)
            .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = TvBody,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
    )
}

private fun formatMillis(millis: Long): String {
    val totalSeconds = (millis / 1000L).coerceAtLeast(0L)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}