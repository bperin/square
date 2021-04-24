package com.brianperin.squaredirectory.network

import com.brianperin.squaredirectory.BuildConfig
import com.brianperin.squaredirectory.util.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Our Api client for dispatching requests for basic header manipulation
 * or if we wanted to add auth at some point,
 * lazy load to reduce overhead on boot
 */
object ApiClient {

    private val converter: GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().create())

    val apiService: ApiInterface by lazy {

        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor { chain ->

                val original = chain.request()
                val builder = original.newBuilder()

                builder.header(Constants.ACCEPT, Constants.APPLICATION_JSON)
                builder.header(Constants.ACCEPT_ENCODING, Constants.APPLICATION_JSON)

                builder.method(original.method, original.body)

                val originalRequest = chain.proceed(builder.build())
                val newRequest = originalRequest.newBuilder()

                newRequest.build()
            }
            .build()


        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build().create(ApiInterface::class.java)
    }


}