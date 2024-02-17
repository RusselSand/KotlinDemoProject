package ru.test.vknewsclient.domain.usecase

import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class IgnorePostUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(post: PostItem) {
        repository.ignorePost(post)
    }
}