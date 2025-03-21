package com.comcostproject.di


import android.content.Context
import com.comcostproject.AppConstant
import com.comcostproject.BuildConfig
import com.comcostproject.data.api.ApiService
import com.comcostproject.data.datasource.AnimalDataSourceImpl
import com.comcostproject.data.datasource.AnimalRemoteDataResource
import com.comcostproject.data.repository.AnimalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AnimalAppModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val httpCacheDirectory = File(context.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize.toLong())
    }


   @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("X-Api-Key",BuildConfig.API_KEY)
                .build()
            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .readTimeout(60,TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(AppConstant.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAnimalRemoteDataSource(apiService: ApiService): AnimalRemoteDataResource {
        return AnimalDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun providesAnimalRepository(animalRemoteDataResource: AnimalRemoteDataResource): AnimalRepository {
        return AnimalRepository(animalRemoteDataResource)
    }

}