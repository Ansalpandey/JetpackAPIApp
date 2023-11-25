package com.example.apiapp

import com.example.apiapp.data.ProductAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

  private val interceptor: HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

  private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

  val api: ProductAPI =
    Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl(ProductAPI.BASE_URL)
      .client(client)
      .build()
      .create(ProductAPI::class.java)
}
