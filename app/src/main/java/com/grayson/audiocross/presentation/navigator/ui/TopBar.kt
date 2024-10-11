package com.grayson.audiocross.presentation.navigator.ui

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackTopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {}
) {
    IconButton(
        modifier = modifier.height(45.dp),
        onClick = onNavigateUp
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}