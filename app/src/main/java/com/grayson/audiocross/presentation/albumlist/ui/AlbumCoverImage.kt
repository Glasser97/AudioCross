package com.grayson.audiocross.presentation.albumlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.grayson.audiocross.R
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import com.grayson.audiocross.ui.theme.surfaceVariantDark
import com.grayson.audiocross.ui.theme.surfaceVariantLight

@Composable
fun AlbumCoverImage(
    imageUrl: String,
    contentDescription: String,
    contentScale: ContentScale = ContentScale.Crop,
    placeHolderBrush: Brush = thumbnailDefaultPlaceholderBrush(),
    modifier: Modifier
) {
    var imagePainterState by remember {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
    }

    val imageLoader =
        rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
            .crossfade(true).build(),
            contentScale = contentScale,
            onState = { state -> imagePainterState = state })

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        when (imagePainterState) {
            is AsyncImagePainter.State.Loading,
            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = painterResource(id = R.drawable.img_empty),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .background(placeHolderBrush)
                        .fillMaxSize()
                )
            }
        }

        Image(
            painter = imageLoader,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun AlbumCoverImagePreview() {
    AudioCrossTheme {
        AlbumCoverImage(imageUrl = "", contentDescription = "", modifier = Modifier)
    }
}

@Composable
fun thumbnailDefaultPlaceholderBrush(
    color: Color = thumbnailDefaultPlaceholderColor()
): Brush {
    return SolidColor(color)
}

@Composable
fun thumbnailDefaultPlaceholderColor(isDarkMode: Boolean = isSystemInDarkTheme()): Color {
    return if (isDarkMode) surfaceVariantDark else surfaceVariantLight
}