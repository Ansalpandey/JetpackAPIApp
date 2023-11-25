package com.example.apiapp.data

import com.example.apiapp.data.model.Products
import retrofit2.http.GET

interface ProductAPI {

  @GET("products") suspend fun getProductsList(): Products

  companion object {
    const val BASE_URL = "https://dummyjson.com/"
  }
}
