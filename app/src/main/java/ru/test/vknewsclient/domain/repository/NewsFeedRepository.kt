package ru.test.vknewsclient.domain.repository

import com.vk.id.AccessToken
import kotlinx.coroutines.flow.StateFlow
import ru.test.vknewsclient.domain.entity.PostComment
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.entity.AuthState

interface NewsFeedRepository {
    fun getAuthStateFlow(): StateFlow<AuthState>
    fun getRecommendations(): StateFlow<List<PostItem>>
    suspend fun loadNextData()
    fun loadComments(feedPost: PostItem): StateFlow<List<PostComment>>
    suspend fun saveAuthToken(token: AccessToken)
    suspend fun changeLike(feedPost: PostItem)
    suspend fun ignorePost(feedPost: PostItem)
}