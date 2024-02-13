package ru.test.vknewsclient.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.vk.id.AccessToken
import ru.test.vknewsclient.PreferencesKeyValueStorage

class AuthViewModel(application: Application): AndroidViewModel(application) {
    private val prefs = PreferencesKeyValueStorage(application)
    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState
    init {
        checkAuthorization()
    }
    fun onAuthSucceed(token: AccessToken) {
        _authState.value = AuthState.Authorized
        prefs.put(VK_TOKEN, Gson().toJson(token))
    }

    fun onAuthFailed(){
        _authState.value = AuthState.NotAuthorized
    }

    private fun checkAuthorization(){
        val tokenJson = prefs.get(VK_TOKEN)
        val token = Gson().fromJson(tokenJson, AccessToken::class.java)
        if(token == null){
            _authState.value = AuthState.NotAuthorized
        }
        else{
            _authState.value = AuthState.Authorized
        }
    }

    private companion object {
        private const val VK_TOKEN = "vk_token"
    }

}