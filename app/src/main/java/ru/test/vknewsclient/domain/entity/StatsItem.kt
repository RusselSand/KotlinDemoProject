package ru.test.vknewsclient.domain.entity

data class StatsItem(
    val type: StatsType,
    val count: Int = 0
)

enum class StatsType {
    VIEWS,
    COMMENTS,
    SHARES,
    LIKES
}