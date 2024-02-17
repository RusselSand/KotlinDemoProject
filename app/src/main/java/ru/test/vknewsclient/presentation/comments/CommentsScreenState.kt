package ru.test.vknewsclient.presentation.comments

import ru.test.vknewsclient.domain.entity.PostComment
import ru.test.vknewsclient.domain.entity.PostItem

sealed class CommentsScreenState {
    object Initial: CommentsScreenState()
    data class Comments(
        val feedPost: PostItem,
        val comments: List<PostComment>
    ): CommentsScreenState()
}