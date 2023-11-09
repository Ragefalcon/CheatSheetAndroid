package ru.ragefalcon.cheatsheetandroid.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ragefalcon.cheatsheetandroid.viewmodels.UserPreferences
import ru.ragefalcon.database.logging.LogApi
import ru.ragefalcon.database.logging.LogApiBuider
import javax.inject.Singleton

/*
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}
*/

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

//    @Provides
//    fun appContext():Context = application

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideOrientationState(context: Context): OrientationState {
        return OrientationState(context.resources.configuration.orientation)
    }

    @Provides
    @Singleton
    fun provideUserPrefenrence(context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    fun provideLogApi(context: Context): LogApi = LogApiBuider.build(context)

}

data class OrientationState(val orientation: Int)

