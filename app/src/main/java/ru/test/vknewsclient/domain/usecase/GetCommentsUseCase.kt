package ru.test.vknewsclient.domain.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.test.vknewsclient.domain.entity.PostComment
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(post: PostItem): StateFlow<List<PostComment>> {
        return repository.loadComments(post)
    }
}