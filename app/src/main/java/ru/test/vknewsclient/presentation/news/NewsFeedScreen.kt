package ru.test.vknewsclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.test.vknewsclient.domain.PostItem

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (PostItem) -> Unit
){
    val viewModel: NewsFeedViewModel = viewModel()
    val screenState = viewModel.screenState.observeAsState(NewsFeedScreenState.Initial)

    when(val currentState = screenState.value){
        is NewsFeedScreenState.Posts -> FeedPosts(
            postItems = currentState.posts,
            paddingValues = paddingValues,
            viewModel = viewModel,
            onCommentClickListener = onCommentClickListener
        )
        NewsFeedScreenState.Initial -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    postItems: List<PostItem>,
    paddingValues: PaddingValues,
    viewModel: NewsFeedViewModel,
    onCommentClickListener: (PostItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 82.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            postItems, key = {it.id}
        ) {postItem ->
            val dismissState = rememberDismissState()
            if(dismissState.isDismissed(DismissDirection.EndToStart)){
                viewModel.remove(postItem)
            }
            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                background = {},
                dismissContent = {
                    NewsCard(
                        postItem = postItem,
                        onViewsClickListener = {statsItem ->
                            viewModel.updateCount(statsItem, postItem)
                        },
                        onLikeClickListener = {statsItem ->
                            viewModel.updateCount(statsItem, postItem)
                        },
                        onShareClickListener = {statsItem ->
                            viewModel.updateCount(statsItem, postItem)
                        },
                        onCommentClickListener = {statsItem ->
                            onCommentClickListener(postItem)
                        },
                    )
                },
                directions = setOf(DismissDirection.EndToStart)
            )
        }
    }
}