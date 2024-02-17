package ru.test.vknewsclient.data.repository

import com.vk.id.AccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import ru.test.vknewsclient.domain.entity.PreferencesKeyValueStorage
import ru.test.vknewsclient.data.mapper.NewsFeedMapper
import ru.test.vknewsclient.data.network.ApiService
import ru.test.vknewsclient.domain.entity.PostComment
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.domain.entity.StatsItem
import ru.test.vknewsclient.domain.entity.StatsType
import ru.test.vknewsclient.extensions.mergeWith
import ru.test.vknewsclient.domain.entity.AuthState
import ru.test.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: NewsFeedMapper,
    private val prefs: PreferencesKeyValueStorage,
): NewsFeedRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<PostItem>>()
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadRecommendations(getAccessToken())
            } else {
                apiService.loadRecommendations(getAccessToken(), startFrom)
            }
            nextFrom = response.newsFeedContent.nextFrom
            val posts = mapper.mapResponseToPosts(response)
            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)

    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val loggedIn = prefs.get(VK_TOKEN) != null
            val authState = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
            emit(authState)
        }
    }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = AuthState.Initial
        )
    private val _feedPosts = mutableListOf<PostItem>()

    private var nextFrom: String? = null
    private val feedPosts: List<PostItem>
        get() = _feedPosts.toList()

    private val recommendations: StateFlow<List<PostItem>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    override fun getAuthStateFlow(): StateFlow<AuthState> = authStateFlow
    override fun getRecommendations(): StateFlow<List<PostItem>> = recommendations
    override suspend fun saveAuthToken(token: AccessToken) {
        prefs.put(VK_TOKEN, token.token)
        checkAuthStateEvents.emit(Unit)
    }
    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }
    private fun getAccessToken(): String {
        return prefs.get(VK_TOKEN) ?: throw IllegalStateException()
    }

    override suspend fun changeLike(feedPost: PostItem) {
        val response = if(!feedPost.isFavorite){
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStats = feedPost.statsItems.toMutableList().apply {
            removeIf { it.type == StatsType.LIKES}
            add(StatsItem(type = StatsType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statsItems = newStats, isFavorite = !feedPost.isFavorite)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)
    }

   override suspend fun ignorePost(feedPost: PostItem) {
        apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }


   override fun loadComments(feedPost: PostItem): StateFlow<List<PostComment>> = flow {
        val response = apiService.getComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )
        emit(mapper.mapResponseToComments(response))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
       scope = coroutineScope,
       started = SharingStarted.Lazily,
       initialValue = listOf()
    )

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
        const val VK_TOKEN = "vk_token"
    }
}