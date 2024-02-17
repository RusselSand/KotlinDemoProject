package ru.test.vknewsclient.domain.entity

import kotlin.random.Random

data class PostItem(
    val id: Long,
    val communityId: Long,
    val communityName: String,
    val publicationTime: String,
    val communityImageUrl: String,
    val contentText: String,
    val contentImageUrl: String?,
    val statsItems: List<StatsItem>,
    val isFavorite: Boolean = Random.nextBoolean()
)
