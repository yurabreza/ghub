package co.me.ghub.di

import co.me.ghub.BuildConfig
import co.me.ghub.data.AUTH_HEADER
import co.me.ghub.data.AUTH_HEADER_PREFIX
import co.me.ghub.data.network.LoginApi
import co.me.ghub.data.network.RepositoryApi
import co.me.ghub.data.storage.prefs.IPreferencesStorage
import com.google.gson.Gson
import com.ihsanbal.logging.Level.BASIC
import com.ihsanbal.logging.LoggingInterceptor
import com.ihsanbal.logging.LoggingInterceptor.Builder
import okhttp3.Interceptor
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
    single(Qualifier.INTERCEPTOR_HEADER) { createLoginHeaderInterceptor(get()) }
    single(Qualifier.INTERCEPTOR_HTTP_LOGGING) { createLoggingInterceptor() }
    single { createOkHttpClient(get(Qualifier.INTERCEPTOR_HEADER), get(Qualifier.INTERCEPTOR_HTTP_LOGGING)) }
    single { createRetrofit(get(Qualifier.SERVER_URL), get(), get()) }
    single { createLoginApi(get()) }
    single { createRepositoryApi(get()) }
}

object Qualifier {
    val SERVER_URL = StringQualifier("SERVER_URL")
    val INTERCEPTOR_HEADER = StringQualifier("INTERCEPTOR_HEADER")
    val INTERCEPTOR_HTTP_LOGGING = StringQualifier("INTERCEPTOR_HTTP_LOGGING")
}

private fun createLoggingInterceptor(): LoggingInterceptor {
    return Builder()
        .loggable(BuildConfig.DEBUG)
        .setLevel(BASIC)
        .log(Platform.INFO)
        .request("Request")
        .response("Response")
        .enableAndroidStudio_v3_LogsHack(true)
        .build()
}

private fun createLoginHeaderInterceptor(prefs: IPreferencesStorage): Interceptor {
    return Interceptor { chain ->
        val original = chain.request()
        if (prefs.isUserTokenStored()) {
            val request = original.newBuilder()
                .header(AUTH_HEADER, "$AUTH_HEADER_PREFIX ${prefs.getGithubAuthHeaderToken()}")
                .build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }
}

private fun createOkHttpClient(
    authInterceptor: Interceptor,
    httpLoggingInterceptor: Interceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
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
