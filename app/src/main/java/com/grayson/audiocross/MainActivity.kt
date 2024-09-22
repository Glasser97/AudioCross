package com.grayson.audiocross

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grayson.audiocross.presentation.AlbumCardItem
import com.grayson.audiocross.presentation.AlbumCardList
import com.grayson.audiocross.ui.theme.AudioCrossTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioCrossTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AlbumCardList(albumCardItems = listOf(
                        AlbumCardItem(
                            101L,
                            "Title is too long",
                            "Voice Author",
                            "CoverUrl",
                            "2:00:00"
                        ),
                        AlbumCardItem(
                            102L,
                            "Title is too long, Title is too long" +
                                    "Title is too long, Title is too long, Title is too long ",
                            "Voice Author",
                            "CoverUrl",
                            "2:00:00"
                        )
                    ))
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