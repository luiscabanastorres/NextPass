package com.nextpass.nextiati.data.di

import android.content.Context
import com.nextpass.nextiati.data.dispatcher.DefaultDispatcher
import com.nextpass.nextiati.data.dispatcher.IoDispatcher
import com.nextpass.nextiati.data.dispatcher.MainDispatcher
import com.nextpass.nextiati.data.dispatcher.MainImmediateDispatcher
import com.nextpass.nextiati.data.preferences.Preferences
import com.nextpass.nextiati.data.preferences.PreferencesImpl
import com.nextpass.nextiati.data.remote.Api
import com.nextpass.nextiati.data.remote.RemoteDataSourceImpl
import com.nextpass.nextiati.data.remote.UrlEnviroment.Companion.BASE_URL
import com.nextpass.nextiati.domain.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModule {

}

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

}

@Module
@InstallIn(
    value = [
        ApplicationComponent::class,
    ],
)
class DataModuleProvides {

    @Provides
    @Singleton
    fun preferences(
        @ApplicationContext
        context: Context,
    ): Preferences =
        PreferencesImpl(context.getSharedPreferences("nextpass-preferences", Context.MODE_PRIVATE))

    @Provides
    fun providesCountersApi(): Api {
        val httpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        addLoggingInterceptor(httpBuilder)

        val httpClient = httpBuilder
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(Api::class.java)
    }

    private fun addLoggingInterceptor(httpBuilder: OkHttpClient.Builder) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpBuilder.addInterceptor(logging)
    }
}

@Module
@InstallIn(
    value = [
        ApplicationComponent::class,
    ],
)
object DispatcherModule {

    @Provides
    @Singleton
    @DefaultDispatcher
    fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @IoDispatcher
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @MainDispatcher
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    @MainImmediateDispatcher
    fun mainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
