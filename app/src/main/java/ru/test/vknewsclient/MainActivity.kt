package ru.test.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.test.vknewsclient.presentation.main.VKIDAuthScreen
import ru.test.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkNewsClientTheme {
                VKIDAuthScreen()
            }
        }
    }
}