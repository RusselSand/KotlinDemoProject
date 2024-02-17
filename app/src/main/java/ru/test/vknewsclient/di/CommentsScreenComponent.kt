package ru.test.vknewsclient.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.test.vknewsclient.domain.entity.PostItem
import ru.test.vknewsclient.presentation.ViewModelFactory

@Subcomponent(
    modules = [
        CommentsViewModelModule::class
    ]
)
interface CommentsScreenComponent {
    fun getViewModelFactory(): ViewModelFactory
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance post: PostItem
        ): CommentsScreenComponent
    }
}