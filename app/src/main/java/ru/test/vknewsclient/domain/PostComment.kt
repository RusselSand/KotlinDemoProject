package ru.test.vknewsclient.domain

import ru.test.vknewsclient.R

data class PostComment(
    val id: Int,
    val authorName: String = "Petia",
    val authorAvatarId: Int = R.drawable.comment_author_avatar,
    val commentText: String = "Some strange comment",
    val publicationDate: String = "14:00"
)