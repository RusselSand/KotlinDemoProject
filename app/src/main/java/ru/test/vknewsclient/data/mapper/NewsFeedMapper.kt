package ru.test.vknewsclient.data.mapper

import ru.test.vknewsclient.data.model.CommentsResponseDto
import ru.test.vknewsclient.data.model.NewsFeedResponseDto
import ru.test.vknewsclient.domain.entity.PostComment
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.entity.StatsItem
import ru.test.vknewsclient.domain.entity.StatsType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsFeedMapper @Inject constructor() {
    fun mapResponseToPosts(response: NewsFeedResponseDto): List<PostItem> {

        val result = mutableListOf<PostItem>()

        val posts = response.newsFeedContent.posts
        val groups = response.newsFeedContent.groups

        for(post in posts){
            val group = groups.find {
                it.id == post.groupId.absoluteValue
            } ?: continue
            val newPost = PostItem(
                id = post.id,
                communityId = post.groupId,
                communityName = group.name,
                publicationTime = mapTimestampToDate(post.date),
                communityImageUrl = group.imageUrl,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                contentText = post.text,
                statsItems = listOf(
                    StatsItem(type = StatsType.LIKES, post.likes.count),
                    StatsItem(type = StatsType.SHARES, post.reposts.count),
                    StatsItem(type = StatsType.VIEWS, post.views.count),
                    StatsItem(type = StatsType.COMMENTS, post.comments.count)
                ),
                isFavorite = post.likes.userLikes > 0
            )
            result.add(newPost)
        }
        return result
    }

    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment>{
        val result = mutableListOf<PostComment>()
        val comments = response.content.comments
        val profiles = response.content.profiles
        for(comment in comments) {
            val author = profiles.firstOrNull {
                it.id == comment.authorId.absoluteValue
            } ?: continue
            if(comment.text.isBlank()) continue
            val newComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date)
            )
            result.add(newComment)
        }
        return result
    }
    private fun mapTimestampToDate(timeStamp: Long): String{
        val date = Date(timeStamp  * 1000)
        return SimpleDateFormat("d MMMM yyyy, hh:MM", Locale.getDefault()).format(date)
    }
}