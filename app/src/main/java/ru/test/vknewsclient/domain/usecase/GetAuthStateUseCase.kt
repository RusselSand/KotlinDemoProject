package ru.test.vknewsclient.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.test.vknewsclient.domain.entity.AuthState
import ru.test.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthStateFlow()
    }
}