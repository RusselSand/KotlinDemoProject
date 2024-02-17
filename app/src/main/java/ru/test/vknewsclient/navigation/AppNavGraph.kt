package ru.test.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.test.vknewsclient.domain.entity.PostItem

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedScreenContent: @Composable () -> Unit,
    favoriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (PostItem) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        homeScreenNavGraph(
            commentsScreenContent = commentsScreenContent,
            newsFeedScreenContent = newsFeedScreenContent)
        composable(Screen.Favorite.route){
            favoriteScreenContent()
        }
        composable(Screen.Profile.route){
            profileScreenContent()
        }
    }
}