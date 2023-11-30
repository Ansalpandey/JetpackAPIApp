package com.example.apiapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.apiapp.data.ProductsRepositoryImpl
import com.example.apiapp.data.model.Product
import com.example.apiapp.presentation.ProductsViewModel
import com.example.apiapp.ui.theme.APIAppTheme
import com.example.apiapp.ui.theme.Black500
import kotlinx.coroutines.flow.collectLatest

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {
  private val viewModel by
    viewModels<ProductsViewModel> {
      object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return ProductsViewModel(ProductsRepositoryImpl(RetrofitInstance.api)) as T
        }
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      APIAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val productList = viewModel.products.collectAsState().value
          val context = LocalContext.current

          LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
            viewModel.showErrorToastChannel.collectLatest { show ->
              if (show) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
              }
            }
          }

          if (productList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              CircularProgressIndicator(color = Color.White)
            }
          } else {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              horizontalAlignment = Alignment.CenterHorizontally,
              contentPadding = PaddingValues(8.dp)
            ) {
              items(productList.size) { index ->
                Product(productList[index])
                Spacer(modifier = Modifier.height(16.dp))
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun Product(product: Product) {
  val imageState =
    rememberAsyncImagePainter(
        model =
          ImageRequest.Builder(LocalContext.current)
            .data(product.thumbnail)
            .size(Size.ORIGINAL)
            .build()
      )
      .state

  Column(
    modifier =
      Modifier.clip(RoundedCornerShape(12.dp)).height(310.dp).fillMaxWidth().background(Black500)
  ) {
    if (imageState is AsyncImagePainter.State.Error) {
      Box(modifier = Modifier.fillMaxWidth().height(310.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.White)
      }
    }

    if (imageState is AsyncImagePainter.State.Success) {
      Image(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        painter = imageState.painter,
        contentDescription = product.title,
        contentScale = ContentScale.Crop
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      text = "${product.title} -- Price: ${product.price}$",
      fontSize = 18.sp,
      color = Color.White,
      fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      text = product.description,
      maxLines = 4,
      color = Color.Gray,
      fontSize = 16.sp,
    )
  }
}
