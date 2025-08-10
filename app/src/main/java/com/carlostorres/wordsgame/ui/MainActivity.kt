package com.carlostorres.wordsgame.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.carlostorres.wordsgame.ui.navigation.NavManager
import com.carlostorres.wordsgame.ui.theme.WordsGameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Enable edge-to-edge mode
        // This disables the default system behavior where the window content is adjusted to fit within the system bars (status and navigation bars).
        // By setting this to false, your app's content is allowed to draw under the system bars, creating an immersive, edge-to-edge experience.
        // You can then manage how your content interacts with these bars using insets and modifiers.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Adjust status and navigation bar appearance
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = true

        // Optional: Set specific colors for status and navigation bars
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            WordsGameTheme {
                NavManager()
            }
        }
    }
}