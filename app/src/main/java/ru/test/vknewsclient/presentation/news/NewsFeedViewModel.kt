package ru.test.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.usecase.ChangeLikeUseCase
import ru.test.vknewsclient.domain.usecase.GetNextDataUseCase
import ru.test.vknewsclient.domain.usecase.GetRecommendationsUseCase
import ru.test.vknewsclient.domain.usecase.IgnorePostUseCase
import ru.test.vknewsclient.extensions.mergeWith
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val getNextDataUseCase: GetNextDataUseCase,
    private val changeLikeUseCase: ChangeLikeUseCase,
    private val ignorePostUseCase: IgnorePostUseCase
): ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exceptionHandler")
    }

    private val recommendationsFlow = getRecommendationsUseCase.invoke()

    private val loadNextDataEvents = MutableSharedFlow<Unit>()

    private val loadNextDataFlow = flow {
        loadNextDataEvents.collect {
            emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationsFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }

    val screenState = recommendationsFlow
        .filter { it.isNotEmpty() }
        .map {
            NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState}
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextPosts(){
        viewModelScope.launch {
            loadNextDataEvents.emit(Unit)
            getNextDataUseCase.invoke()
        }
    }
    fun changeLikeStatus(feedPost: PostItem){
        viewModelScope.launch(exceptionHandler) {
            changeLikeUseCase.invoke(feedPost)
        }
    }

    fun remove(postItem: PostItem){
        viewModelScope.launch(exceptionHandler) {
            ignorePostUseCase.invoke(postItem)
        }
    }
}