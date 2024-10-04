package com.grayson.audiocross

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grayson.audiocross.presentation.AlbumCardDisplayItem
import com.grayson.audiocross.presentation.AlbumCardList
import com.grayson.audiocross.presentation.viewmodel.AlbumListViewModel
import com.grayson.audiocross.ui.theme.AudioCrossTheme

class MainActivity : ComponentActivity() {

    // region field

    private val viewModel: AlbumListViewModel by viewModels()

    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioCrossTheme {
                val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    val albumList: List<AlbumCardDisplayItem> by viewModel.albumList.collectAsStateWithLifecycle()

                    AlbumCardList(
                        modifier = Modifier.padding(padding),
                        albumCardDisplayItems = albumList,
                        refreshCallback = { viewModel.refreshAlbumList() },
                        isRefreshing = isRefreshing
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AudioCrossTheme {
        Greeting("Android")
    }
}