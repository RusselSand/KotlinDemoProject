package ru.test.vknewsclient.presentation.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.test.vknewsclient.R
import ru.test.vknewsclient.domain.PostItem
import ru.test.vknewsclient.domain.StatsItem
import ru.test.vknewsclient.domain.StatsType

@Composable
fun NewsCard(modifier: Modifier = Modifier,
             postItem: PostItem,
             onCommentClickListener: (StatsItem) -> Unit,
             onLikeClickListener: (StatsItem) -> Unit,
             onShareClickListener: (StatsItem) -> Unit,
             onViewsClickListener: (StatsItem) -> Unit,
             ) {
    Card(shape = RectangleShape,
        modifier = modifier){
        Column(
        ) {
            TopMenuBar(
                groupName = postItem.communityName,
                time = postItem.publicationTime,
                imageId = postItem.avatarResId)
            Spacer(modifier = Modifier.height(8.dp))
            CardBody(cardText = postItem.contentText, cardImage = postItem.contentImgResId)
            Spacer(modifier = Modifier.height(8.dp))
            CardFooter(
                statsItems = postItem.statsItems,
                onCommentClickListener = onCommentClickListener,
                onLikeClickListener = onLikeClickListener,
                onViewsClickListener = onViewsClickListener,
                onShareClickListener = onShareClickListener
            )
        }

    }
}


@Composable
private fun TopMenuBar(groupName: String, time: String, imageId: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(
            id = imageId),
            contentDescription = "group logo",
            modifier = Modifier
                .height(50.dp)
                .clip(shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = groupName, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = time, color = MaterialTheme.colorScheme.onSecondary)
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = "More",
            tint = MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
private fun CardBody(cardText: String, cardImage: Int){
    Text(
        text = cardText,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(8.dp))
    Image(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        painter = painterResource(id = cardImage),
        contentDescription = "Post image",
        contentScale = ContentScale.FillWidth
    )

}

@Composable
private fun CardFooter(
    statsItems: List<StatsItem>,
    onCommentClickListener: (StatsItem) -> Unit,
    onLikeClickListener: (StatsItem) -> Unit,
    onShareClickListener: (StatsItem) -> Unit,
    onViewsClickListener: (StatsItem) -> Unit,
){
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(modifier = Modifier.weight(1f)) {
            val viewsItem = statsItems.getItemByType(StatsType.VIEWS)
            IconWithText(
                iconId = R.drawable.ic_views_count,
                text = viewsItem.count,
                onItemClickListener = {
                    onViewsClickListener(viewsItem)
                }
            )
        }
        Row(modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween) {
            val repostsItem = statsItems.getItemByType(StatsType.SHARES)
            val commentsItem = statsItems.getItemByType(StatsType.COMMENTS)
            val likesItem = statsItems.getItemByType(StatsType.LIKES)
            IconWithText(iconId = R.drawable.ic_share, text = repostsItem.count,
                onItemClickListener = {
                    onShareClickListener(repostsItem)
                })
            IconWithText(iconId = R.drawable.ic_comment, text = commentsItem.count,
                onItemClickListener = {
                    onCommentClickListener(commentsItem)
                })
            IconWithText(iconId = R.drawable.ic_like, text = likesItem.count,
                onItemClickListener = {
                    onLikeClickListener(likesItem)
                })
        }
    }
}

private fun List<StatsItem>.getItemByType(type: StatsType): StatsItem {
    return this.find {it.type == type} ?: throw IllegalStateException("No icon type found")
}

@Composable
fun IconWithText(
    iconId: Int,
    text: Int,
    onItemClickListener: () -> Unit
){
    Row(
        modifier = Modifier.clickable {
            onItemClickListener()
        }
    ){
        Icon(
            painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$text",
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}