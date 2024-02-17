package ru.test.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import ru.test.vknewsclient.domain.entity.PostItem

fun NavGraphBuilder.homeScreenNavGraph(
    commentsScreenContent: @Composable (PostItem) -> Unit,
    newsFeedScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route) {
        composable(Screen.NewsFeed.route){
            newsFeedScreenContent()
        }
        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                navArgument(Screen.KEY_FEED_POST) {
                    type = NavType.StringType
                },
            )
        ){
            val postItemJson = it.arguments?.getString(Screen.KEY_FEED_POST) ?: ""
            val postItem = Gson().fromJson<PostItem>(postItemJson, PostItem::class.java)
            commentsScreenContent(postItem)
        }
    }
}