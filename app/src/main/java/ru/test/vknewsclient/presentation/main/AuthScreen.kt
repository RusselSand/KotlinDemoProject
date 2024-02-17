package ru.test.vknewsclient.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.onetap.OneTap
import ru.test.vknewsclient.domain.entity.AuthState
import ru.test.vknewsclient.presentation.getApplicationComponent

@Composable
fun VKIDAuthScreen() {
    val component = getApplicationComponent()
    val viewModel: AuthViewModel = viewModel(factory = component.getViewModelFactory())
    val authState = viewModel.authState.collectAsState(AuthState.Initial)
    when(authState.value) {
        is AuthState.NotAuthorized -> {
            OneTap(
                onAuth = { token ->
                    viewModel.onAuthSucceed(token)
                },
                onFail = {
                },
                signInAnotherAccountButtonEnabled = true,
                style = OneTapStyle.Light(
                    cornersStyle = OneTapButtonCornersStyle.Custom(2F),
                    sizeStyle = OneTapButtonSizeStyle.SMALL_32,
                    elevationStyle = OneTapButtonElevationStyle.Custom(4F)
                )
            )
        }
        is AuthState.Authorized -> {
            MainScreen()
        }

        else -> {}
    }

}