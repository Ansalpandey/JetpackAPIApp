package com.example.apiapp.data

import com.example.apiapp.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
  suspend fun getProductsList(): Flow<Result<List<Product>>>
}
