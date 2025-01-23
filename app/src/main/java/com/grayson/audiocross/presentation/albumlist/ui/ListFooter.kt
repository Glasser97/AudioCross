package com.grayson.audiocross.presentation.albumlist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grayson.audiocross.R

@Composable
fun LoadMoreFooter(
    needRetry: Boolean = false,
    retry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (needRetry) {
            Button(
                onClick = { retry() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.load_more_retry))
            }
        } else {
            Text(
                text = stringResource(R.string.loading),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}