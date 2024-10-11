package com.grayson.audiocross

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.grayson.audiocross.domain.database.IUserInfoHelper
import com.grayson.audiocross.presentation.navigator.AudioCrossGraph
import com.grayson.audiocross.ui.theme.AudioCrossTheme
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    // region field

    private val userInfoHelper: IUserInfoHelper by inject(IUserInfoHelper::class.java)

    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioCrossTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    AudioCrossGraph(
                        modifier = Modifier.padding(padding),
                        activity = this
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userInfoHelper.close()
    }
}