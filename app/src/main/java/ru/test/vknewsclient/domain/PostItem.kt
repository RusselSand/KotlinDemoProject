package ru.test.vknewsclient.domain

import ru.test.vknewsclient.R

data class PostItem (
    val id: Int = 0,
    val communityName: String = "/dev/null",
    val publicationTime: String = "14:00",
    val avatarResId: Int = R.drawable.post_comunity_thumbnail,
    val contentText: String = "Some other strange text is here",
    val contentImgResId: Int = R.drawable.post_content_image,
    val statsItems: List<StatsItem> = listOf(
        StatsItem(type = StatsType.VIEWS, 966),
        StatsItem(type = StatsType.COMMENTS, 13),
        StatsItem(type = StatsType.LIKES, 27),
        StatsItem(type = StatsType.SHARES, 9),
    )
)
