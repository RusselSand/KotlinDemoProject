package ru.test.vknewsclient.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.test.vknewsclient.domain.PostComment
import ru.test.vknewsclient.domain.PostItem

class CommentsViewModel(
    postItem: PostItem
): ViewModel() {

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    init {
        loadComments(postItem)
    }
    private fun loadComments(postItem:PostItem){
        val commentsList = mutableListOf<PostComment>().apply {
            repeat(15){
                add(PostComment(id = it))
            }
        }
        _screenState.value = CommentsScreenState.Comments(
            feedPost = postItem,
            comments = commentsList
        )
    }

//    fun showComments(postItem: PostItem) {
//        savedState = _screenState.value
//        _screenState.value = CommentsScreenState.Comments(
//            postItem, commentsList)
//    }
//
//    fun closeComments(){
//        _screenState.value = savedState!!
//    }
}