package ru.test.vknewsclient.presentation.comments

import ru.test.vknewsclient.domain.PostComment
import ru.test.vknewsclient.domain.PostItem

sealed class CommentsScreenState {
    object Initial: CommentsScreenState()
    data class Comments(
        val feedPost: PostItem,
        val comments: List<PostComment>
    ): CommentsScreenState()
}