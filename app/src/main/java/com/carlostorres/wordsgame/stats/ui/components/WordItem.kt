package com.carlostorres.wordsgame.stats.ui.components

import android.widget.Space
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed

@Composable
fun WordItem(
    modifier: Modifier = Modifier,
    word: String,
    isGuessed: Boolean,
) {

    Row(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(12.dp))
        Text(text = word)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = if (isGuessed) Icons.Default.Check else Icons.Default.Close,
            contentDescription = "",
            tint = if (isGuessed) DarkGreen else DarkRed
        )
        Spacer(modifier = Modifier.width(12.dp))

    }

}