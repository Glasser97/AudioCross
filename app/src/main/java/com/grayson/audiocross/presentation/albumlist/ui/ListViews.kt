package com.grayson.audiocross.presentation.albumlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_devices_fold_24),
            contentDescription = "empty list icon.",
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = stringResource(R.string.empty_list),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 12.dp),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun FailedScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_folder_off_24),
            contentDescription = "empty list icon.",
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 12.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        Button(
            onClick = { onRetry() },
            modifier = Modifier
        ) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview
@Composable
private fun EmptyScreenPreview() {
    MaterialTheme {
        EmptyScreen()
    }
}

@Preview
@Composable
private fun FailedScreenPreview() {
    MaterialTheme {
        FailedScreen()
    }
}