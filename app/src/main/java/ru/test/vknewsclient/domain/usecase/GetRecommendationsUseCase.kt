package ru.test.vknewsclient.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<List<PostItem>> {
        return repository.getRecommendations()
    }
}