package ru.test.vknewsclient.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.test.vknewsclient.di.ApplicationComponent
import ru.test.vknewsclient.di.DaggerApplicationComponent

class NewsFeedApplication: Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this,
        )
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent{
    return (LocalContext.current.applicationContext as NewsFeedApplication).component
}