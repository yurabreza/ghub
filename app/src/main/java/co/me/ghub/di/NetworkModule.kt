package co.me.ghub.di

import co.me.ghub.BuildConfig
import co.me.ghub.data.network.LoginApi
import co.me.ghub.data.network.RepositoryApi
import com.google.gson.Gson
import com.ihsanbal.logging.Level.BASIC
import com.ihsanbal.logging.LoggingInterceptor
import com.ihsanbal.logging.LoggingInterceptor.Builder
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single(Qualifier.SERVER_URL) { BuildConfig.BASE_URL }
    single { Gson() }
    single { GsonConverterFactory.create(get()) }
    single { createLoggingInterceptor() }
    single { createOkHttpClient(get()) }
    single { createRetrofit(get(Qualifier.SERVER_URL), get(), get()) }
    single { createLoginApi(get()) }
    single { createRepositoryApi(get()) }
}

object Qualifier {
    val SERVER_URL = StringQualifier("SERVER_URL")
}

private fun createLoggingInterceptor(): LoggingInterceptor? {
    return Builder()
        .loggable(BuildConfig.DEBUG)
        .setLevel(BASIC)
        .log(Platform.INFO)
        .request("Request")
        .response("Response")
        .enableAndroidStudio_v3_LogsHack(true)
        .build()
}

private fun createOkHttpClient(
    httpLoggingInterceptor: LoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

private fun createRetrofit(
    baseUrl: String,
    gsonConverterFactory: GsonConverterFactory,
    okHttpClient: OkHttpClient
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

private fun createLoginApi(retrofit: Retrofit) = retrofit.create(LoginApi::class.java)

private fun createRepositoryApi(retrofit: Retrofit) = retrofit.create(RepositoryApi::class.java)
