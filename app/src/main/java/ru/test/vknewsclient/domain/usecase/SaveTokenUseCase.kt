package ru.test.vknewsclient.domain.usecase

import com.vk.id.AccessToken
import ru.test.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(token: AccessToken) {
        repository.saveAuthToken(token)
    }
}