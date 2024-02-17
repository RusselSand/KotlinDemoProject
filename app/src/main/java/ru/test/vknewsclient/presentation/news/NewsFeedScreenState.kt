package ru.test.vknewsclient.presentation.news

import ru.test.vknewsclient.domain.entity.PostItem

sealed class NewsFeedScreenState {
    object Initial: NewsFeedScreenState()
    object Loading: NewsFeedScreenState()
    data class Posts(
        val posts: List<PostItem>,
        val nextDataIsLoading: Boolean = false
    ): NewsFeedScreenState()
}