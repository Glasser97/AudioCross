package com.grayson.audiocross.presentation.albumlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grayson.audiocross.presentation.albumlist.model.AlbumCardDisplayItem
import com.grayson.audiocross.ui.theme.AudioCrossTheme

/**
 * Represents a single audio card.
 */
@Composable
fun AlbumCard(
    albumCardDisplayItem: AlbumCardDisplayItem,
    modifier: Modifier = Modifier,
    onClick: (AlbumCardDisplayItem) -> Unit = {}
) {
    Surface(modifier = modifier
        .clickable {
            onClick(albumCardDisplayItem)
        }
        .background(MaterialTheme.colorScheme.surface)
        .padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AlbumCoverImage(
                imageUrl = albumCardDisplayItem.coverUrl,
                contentDescription = albumCardDisplayItem.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.size(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 56.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = albumCardDisplayItem.title,
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
                        text = albumCardDisplayItem.voiceAuthor,
                        maxLines = 1,
                        minLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (albumCardDisplayItem.voiceAuthor.isNotEmpty()) {
                        Text(
                            text = " / ",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = albumCardDisplayItem.duration,
                        maxLines = 1,
                        minLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                }
            }
        }
    }
}

@Composable
@Preview()
fun AudioCardPreview() {
    AudioCrossTheme {
        AlbumCard(
            albumCardDisplayItem = AlbumCardDisplayItem(
                101L,
                "RJ101",
                "Title is too long,",
                "Voice Author",
                "CoverUrl",
                "2:00:00"
            )
        )
    }
}