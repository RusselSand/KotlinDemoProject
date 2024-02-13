package ru.test.vknewsclient.presentation.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.test.vknewsclient.domain.PostItem

class CommentsViewModelFactory(
    private val postItem: PostItem
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(postItem) as T
    }
}