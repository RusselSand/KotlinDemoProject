package ru.test.vknewsclient.domain.usecase

import ru.test.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetNextDataUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke() {
        repository.loadNextData()
    }
}