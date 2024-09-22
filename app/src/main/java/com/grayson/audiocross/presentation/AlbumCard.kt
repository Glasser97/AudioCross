package com.grayson.audiocross.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grayson.audiocross.ui.theme.AudioCrossTheme

/**
 * Represents a single audio card.
 */
@Composable
fun AlbumCard(
    albumCardItem: AlbumCardItem,
    modifier: Modifier = Modifier,
    onClick: (AlbumCardItem) -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(albumCardItem) }
    ) {
        AlbumCoverImage(
            imageUrl = albumCardItem.coverUrl,
            contentDescription = albumCardItem.title,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.size(8.dp))

        Column(
            modifier = Modifier
                .weight(1f).defaultMinSize(minHeight = 56.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = albumCardItem.title,
                maxLines = 2,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = albumCardItem.voiceAuthor,
                    maxLines = 1,
                    minLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "/",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )


                Text(
                    text = albumCardItem.duration,
                    maxLines = 1,
                    minLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }
        }
    }
}

@Composable
@Preview()
fun AudioCardPreview() {
    AudioCrossTheme {
        AlbumCard(
            albumCardItem = AlbumCardItem(
                101L,
                "Title is too long,",
                "Voice Author",
                "CoverUrl",
                "2:00:00"
            )
        )
    }
}

data class AlbumCardItem(
    val albumId: Long,
    val title: String,
    val voiceAuthor: String,
    val coverUrl: String,
    val duration: String,
)