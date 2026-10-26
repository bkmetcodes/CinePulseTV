package com.bekacodes.cinepulsetv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bekacodes.cinepulsetv.data.local.CinePulseDatabase
import com.bekacodes.cinepulsetv.ui.CinePulseTvScreen
import com.bekacodes.cinepulsetv.ui.CinePulseViewModel

class MainActivity : ComponentActivity() {

    private val database by lazy { CinePulseDatabase.getInstance(applicationContext) }
    private val viewModel: CinePulseViewModel by viewModels {
        CinePulseViewModel.provideFactory(database.dao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                CinePulseTvScreen(
                    uiState = uiState,
                    onIntent = viewModel::onIntent
                )
            }
        }
    }
}