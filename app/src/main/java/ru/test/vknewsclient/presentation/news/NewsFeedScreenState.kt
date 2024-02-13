package ru.test.vknewsclient.presentation.news

import ru.test.vknewsclient.domain.PostItem

sealed class NewsFeedScreenState {
    object Initial: NewsFeedScreenState()
    data class Posts(val posts: List<PostItem>): NewsFeedScreenState()
}