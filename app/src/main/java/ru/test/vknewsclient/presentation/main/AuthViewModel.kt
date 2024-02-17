package ru.test.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import kotlinx.coroutines.launch
import ru.test.vknewsclient.domain.usecase.GetAuthStateUseCase
import ru.test.vknewsclient.domain.usecase.SaveTokenUseCase
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val saveTokenUseCase: SaveTokenUseCase): ViewModel() {

    val authState = getAuthStateUseCase.invoke()

    fun onAuthSucceed(token: AccessToken) {
        viewModelScope.launch {
            saveTokenUseCase.invoke(token)
        }
    }
}