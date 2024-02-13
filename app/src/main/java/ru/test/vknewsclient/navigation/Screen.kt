package ru.test.vknewsclient.navigation

import android.net.Uri
import com.google.gson.Gson
import ru.test.vknewsclient.domain.PostItem

sealed class Screen (
    val route: String
){
    object NewsFeed: Screen(ROUTE_NEWS_FEED)
    object Favorite: Screen(ROUTE_FAVORITE)
    object Profile: Screen(ROUTE_PROFILE)
    object Home: Screen(ROUTE_HOME)
    object Comments: Screen(ROUTE_COMMENTS) {
        private const val ROUTE_FOR_ARGS = "comments"
        fun getRouteWithArgs(postItem: PostItem): String {
            val feedPostJson = Gson().toJson(postItem)
            return "$ROUTE_FOR_ARGS/${feedPostJson.encode()}"
        }
    }

    companion object {
        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVORITE = "favorite"
        const val ROUTE_PROFILE = "profile"
        const val ROUTE_HOME = "home"
        const val KEY_FEED_POST = "feed_post"
        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}"

    }
}

fun String.encode(): String {
    return Uri.encode(this)
}
