package ru.test.vknewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.usecase.GetCommentsUseCase
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val postItem: PostItem
): ViewModel() {

    val screenState = getCommentsUseCase.invoke(postItem)
        .map{CommentsScreenState.Comments(
            feedPost = postItem,
            comments = it
        )}
}