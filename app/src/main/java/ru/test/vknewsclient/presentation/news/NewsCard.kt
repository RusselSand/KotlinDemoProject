package ru.test.vknewsclient.presentation.news

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.test.vknewsclient.R
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.entity.StatsItem
import ru.test.vknewsclient.domain.entity.StatsType

@Composable
fun NewsCard(modifier: Modifier = Modifier,
             postItem: PostItem,
             onCommentClickListener: (StatsItem) -> Unit,
             onLikeClickListener: (StatsItem) -> Unit,
             ) {
    Card(shape = RectangleShape,
        modifier = modifier){
        Column(
        ) {
            TopMenuBar(
                groupName = postItem.communityName,
                time = postItem.publicationTime,
                imageUrl = postItem.communityImageUrl)
            Spacer(modifier = Modifier.height(8.dp))
            CardBody(cardText = postItem.contentText, cardImage = postItem.contentImageUrl)
            Spacer(modifier = Modifier.height(8.dp))
            CardFooter(
                statsItems = postItem.statsItems,
                isFavorite = postItem.isFavorite,
                onCommentClickListener = onCommentClickListener,
                onLikeClickListener = onLikeClickListener,
            )
        }

    }
}


@Composable
private fun TopMenuBar(groupName: String, time: String, imageUrl: String){
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(model = imageUrl,
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
private fun CardBody(cardText: String, cardImage: String?){
    Text(
        text = cardText,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(8.dp))
    AsyncImage(model =cardImage,
        contentDescription = "Post image",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun CardFooter(
    statsItems: List<StatsItem>,
    isFavorite: Boolean,
    onCommentClickListener: (StatsItem) -> Unit,
    onLikeClickListener: (StatsItem) -> Unit,
){
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(modifier = Modifier.weight(1f).padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)) {
            val viewsItem = statsItems.getItemByType(StatsType.VIEWS)
            IconWithText(
                iconId = R.drawable.ic_views_count,
                text = viewsItem.count,
            )
        }
        Row(modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween) {
            val repostsItem = statsItems.getItemByType(StatsType.SHARES)
            val commentsItem = statsItems.getItemByType(StatsType.COMMENTS)
            val likesItem = statsItems.getItemByType(StatsType.LIKES)
            IconWithText(iconId = R.drawable.ic_share, text = repostsItem.count,
               )
            IconWithText(iconId = R.drawable.ic_comment, text = commentsItem.count,
                onItemClickListener = {
                    onCommentClickListener(commentsItem)
                })
            val icon = if(isFavorite) {R.drawable.ic_like_set} else {R.drawable.ic_like}
            val color = if(isFavorite) {Color.Red} else {MaterialTheme.colorScheme.onSecondary}
            IconWithText(iconId = icon, text = likesItem.count, tint = color,
                onItemClickListener = {
                    onLikeClickListener(likesItem)
                })
        }
    }
}

private fun formatStatsCount(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", (count / 1000))
    } else if(count > 1000) {
        String.format("%.1fK", (count / 1000f))
    } else {
        count.toString()
    }
}

private fun List<StatsItem>.getItemByType(type: StatsType): StatsItem {
    return this.find {it.type == type} ?: throw IllegalStateException("No icon type found")
}

@Composable
fun IconWithText(
    iconId: Int,
    text: Int,
    onItemClickListener: (() -> Unit) ?= null,
    tint: Color = MaterialTheme.colorScheme.onSecondary
){
    val modifier = if(onItemClickListener == null){
        Modifier
    } else {
        Modifier.clickable {
            onItemClickListener()
        }
    }
    Row(
        modifier = modifier
    ){
        Icon(
            painterResource(id = iconId),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.width(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = formatStatsCount(text),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}