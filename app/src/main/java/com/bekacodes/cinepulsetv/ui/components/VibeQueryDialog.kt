package com.bekacodes.cinepulsetv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bekacodes.cinepulsetv.ui.theme.TvBody
import com.bekacodes.cinepulsetv.ui.theme.TvCard
import com.bekacodes.cinepulsetv.ui.theme.TvIndigo
import com.bekacodes.cinepulsetv.ui.theme.TvInk
import com.bekacodes.cinepulsetv.ui.theme.TvMuted
import com.bekacodes.cinepulsetv.ui.theme.TvSky
import com.bekacodes.cinepulsetv.ui.theme.TvSurface

@Composable
fun VibeQueryDialog(
    initialQuery: String,
    presetPrompts: List<String>,
    onQueryChanged: (String) -> Unit,
    onSelectPreset: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TvCard,
        titleContentColor = TvInk,
        textContentColor = TvBody,
        title = {
            Text("✦ AI Vibe & Runtime Filter", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = initialQuery,
                    onValueChange = onQueryChanged,
                    placeholder = { Text("e.g., Dark sci-fi under 110 minutes", color = TvMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TvInk,
                        unfocusedTextColor = TvInk,
                        focusedBorderColor = TvSky,
                        unfocusedBorderColor = TvIndigo
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Or pick a voice preset:", color = TvMuted, fontSize = 12.sp)
                presetPrompts.forEach { preset ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(TvSurface)
                            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
                            .clickable { onSelectPreset(preset) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("✦ $preset", color = TvInk, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = TvIndigo)
            ) {
                Text("Rank Catalog", color = TvInk)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TvMuted)
            }
        }
    )
}