package ru.test.vknewsclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.presentation.getApplicationComponent

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (PostItem) -> Unit,
){
    val viewModel: NewsFeedViewModel = viewModel(factory = getApplicationComponent().getViewModelFactory())
    val screenState = viewModel.screenState.collectAsState(initial = NewsFeedScreenState.Initial)
    NewsFeedScreenContent(
        screenState = screenState,
        paddingValues = paddingValues,
        onCommentClickListener = onCommentClickListener,
        viewModel = viewModel
    )
}

@Composable
private fun NewsFeedScreenContent(
    screenState: State<NewsFeedScreenState>,
    paddingValues: PaddingValues,
    onCommentClickListener: (PostItem) -> Unit,
    viewModel: NewsFeedViewModel
) {
    when(val currentState = screenState.value){
        is NewsFeedScreenState.Posts -> FeedPosts(
            postItems = currentState.posts,
            paddingValues = paddingValues,
            viewModel = viewModel,
            onCommentClickListener = onCommentClickListener,
            nextDataIsLoading = currentState.nextDataIsLoading
        )
        NewsFeedScreenState.Initial -> {

        }

        NewsFeedScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.DarkGray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    postItems: List<PostItem>,
    paddingValues: PaddingValues,
    viewModel: NewsFeedViewModel,
    onCommentClickListener: (PostItem) -> Unit,
    nextDataIsLoading: Boolean
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
                        onLikeClickListener = {statsItem ->
                            viewModel.changeLikeStatus(postItem)
                        },
                        onCommentClickListener = {statsItem ->
                            onCommentClickListener(postItem)
                        },
                    )
                },
                directions = setOf(DismissDirection.EndToStart)
            )
        }
        item {
            if(nextDataIsLoading){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.DarkGray)
                }
            } else {
                SideEffect {
                    viewModel.loadNextPosts()
                }
            }
        }
    }
}