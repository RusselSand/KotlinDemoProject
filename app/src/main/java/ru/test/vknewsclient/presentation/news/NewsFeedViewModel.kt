package ru.test.vknewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.test.vknewsclient.domain.PostItem
import ru.test.vknewsclient.domain.StatsItem

class NewsFeedViewModel: ViewModel() {


    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    init {
        loadPosts()
    }
    fun loadPosts(){
        val sourceList = mutableListOf<PostItem>().apply {
            repeat(10){
                add(PostItem(id = it))
            }
        }
        _screenState.value = NewsFeedScreenState.Posts(posts = sourceList)
    }
    fun updateCount(item: StatsItem, postItem: PostItem) {
        val currentState = screenState.value
        if(currentState !is NewsFeedScreenState.Posts){
            return
        }
        val oldPosts = currentState.posts.toMutableList()
        val oldStats = postItem.statsItems
        val newStats = oldStats.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }
        val newPostItem = postItem.copy(statsItems = newStats)
        val newPosts = oldPosts.apply {
            replaceAll {
                if (it.id == newPostItem.id) {
                    newPostItem
                }
                else {
                    it
                }
            }
        }
        _screenState.value = NewsFeedScreenState.Posts(newPosts)
    }

    fun remove(postItem: PostItem){
        val currentState = screenState.value
        if(currentState !is NewsFeedScreenState.Posts){
            return
        }
        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(postItem)
        _screenState.value = NewsFeedScreenState.Posts(oldPosts)
    }

}