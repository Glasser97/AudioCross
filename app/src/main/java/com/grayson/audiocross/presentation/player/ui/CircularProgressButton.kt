package com.grayson.audiocross.presentation.player.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grayson.audiocross.R
import com.grayson.audiocross.ui.theme.AudioCrossTheme

/**
 * A circular progress button.
 *
 * @param modifier Modifier to be applied to the button.
 * @param progress Progress of the button.From 0f to 1f
 */
@Composable
fun CircularProgressButton(
    modifier: Modifier = Modifier,
    progress: Float,
    onPlayPauseClicked: () -> Unit,
    isPlaying: Boolean,
    isEnabled: Boolean = true
) {
    val animatedProgress = animateFloatAsState(targetValue = progress, label = "")

    Box(
        modifier = modifier
            .size(50.dp),
        contentAlignment = Alignment.Center
    ) {
        val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
        val primaryColor = MaterialTheme.colorScheme.primary
        // Draw Circle
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {

            drawCircle(
                color = onPrimaryColor,
                radius = size.minDimension / 2,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = animatedProgress.value * 360f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // 播放/暂停按钮
        IconButton(
            onClick = onPlayPauseClicked,
            modifier = Modifier
                .size(50.dp),
            enabled = isEnabled
        ) {
            val icon =
                if (isPlaying) painterResource(R.drawable.icon_pause_24) else painterResource(R.drawable.icon_play_arrow_24)
            Icon(
                modifier = Modifier,
                painter = icon,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = primaryColor
            )
        }
    }
}

@Preview
@Composable
private fun CircularProgressButtonPreview() {
    AudioCrossTheme {
        CircularProgressButton(modifier = Modifier.padding(5.dp), progress = 0.5f, onPlayPauseClicked = {}, isPlaying = true)
    }
}